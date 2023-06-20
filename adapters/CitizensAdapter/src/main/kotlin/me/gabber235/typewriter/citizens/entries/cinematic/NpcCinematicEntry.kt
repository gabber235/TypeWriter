package me.gabber235.typewriter.citizens.entries.cinematic

import com.github.shynixn.mccoroutine.bukkit.minecraftDispatcher
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import kotlinx.coroutines.withContext
import me.gabber235.typewriter.adapters.Colors
import me.gabber235.typewriter.adapters.Entry
import me.gabber235.typewriter.adapters.Tags
import me.gabber235.typewriter.adapters.modifiers.Capture
import me.gabber235.typewriter.adapters.modifiers.EntryIdentifier
import me.gabber235.typewriter.adapters.modifiers.Help
import me.gabber235.typewriter.adapters.modifiers.Segments
import me.gabber235.typewriter.capture.AssetCapturer
import me.gabber235.typewriter.capture.CapturerCreator
import me.gabber235.typewriter.capture.MultiTapeRecordedCapturer
import me.gabber235.typewriter.capture.RecorderRequestContext
import me.gabber235.typewriter.capture.capturers.LocationTapeCapturer
import me.gabber235.typewriter.capture.capturers.Tape
import me.gabber235.typewriter.capture.capturers.firstNotNullWhere
import me.gabber235.typewriter.citizens.CitizensAdapter.temporaryRegistry
import me.gabber235.typewriter.entry.AssetManager
import me.gabber235.typewriter.entry.Query
import me.gabber235.typewriter.entry.entries.*
import me.gabber235.typewriter.plugin
import me.gabber235.typewriter.utils.Icons
import me.gabber235.typewriter.utils.onFail
import net.citizensnpcs.api.CitizensAPI
import net.citizensnpcs.api.npc.NPC
import net.citizensnpcs.api.trait.trait.PlayerFilter
import net.citizensnpcs.trait.HologramTrait
import net.citizensnpcs.trait.SkinTrait
import org.bukkit.Location
import org.bukkit.entity.EntityType
import org.bukkit.entity.Player
import org.koin.core.qualifier.named
import org.koin.java.KoinJavaComponent.inject

interface NpcCinematicEntry : CinematicEntry {
    @Help("Recorded segments of the NPC's interactions")
    @Segments(Colors.PINK, Icons.PERSON_WALKING)
    val recordedSegments: List<NpcRecordedSegment>
}

@Tags("npc_movement_artifact")
@Entry("npc_movement_artifact", "Movement data for an npc", Colors.PINK, Icons.PERSON_WALKING)
data class NpcMovementArtifact(
    override val id: String = "",
    override val name: String = "",
    override val artifactId: String = "",
) : ArtifactEntry

data class NpcRecordedSegment(
    override val startFrame: Int = 0,
    override val endFrame: Int = 0,
    @Help("The artifact for the recorded interactions data")
    @EntryIdentifier(NpcMovementArtifact::class)
    @Capture(NpcRecordedDataCapturer::class)
    val artifact: String = "",
) : Segment

data class NpcFrame(
    val location: Location?
)

class NpcRecordedDataCapturer(title: String, entry: AssetEntry) :
    AssetCapturer<Tape<NpcFrame>>(title, entry, NpcTapeCapturer(title)) {
    companion object : CapturerCreator<NpcRecordedDataCapturer> {
        override fun create(context: RecorderRequestContext): Result<NpcRecordedDataCapturer> {
            val entry = getAssetFromFieldValue(context.fieldValue) onFail { return it }

            return Result.success(NpcRecordedDataCapturer(context.title, entry))
        }
    }
}

class NpcTapeCapturer(title: String) : MultiTapeRecordedCapturer<NpcFrame>(title) {
    private val location by tapeCapturer(::LocationTapeCapturer)
    override fun combineFrame(frame: Int): NpcFrame {
        return NpcFrame(
            location = location[frame],
        )
    }
}


interface NpcData {
    /// Create a new NPC with the data.
    fun create(): NPC

    /// Ran when the cinematic is started.
    fun setup(player: Player, npc: NPC) {
        val filter = npc.getOrAddTrait(PlayerFilter::class.java)
        filter.setAllowlist()
        filter.addPlayer(player.uniqueId)
    }

    /// Ran when the cinematic is stopped.
    fun teardown(player: Player, npc: NPC) {
        npc.despawn()
        npc.destroy()
    }
}

class PlayerNpcData(private val player: Player) : NpcData {
    override fun create(): NPC {
        val npc = temporaryRegistry.createNPC(EntityType.PLAYER, player.name)
        npc.getOrAddTrait(SkinTrait::class.java).skinName = player.name

        return npc
    }
}

data class ReferenceNpcData(val id: Int) : NpcData {
    override fun create(): NPC {
        val original =
            CitizensAPI.getNPCRegistry().getById(id) ?: throw IllegalArgumentException("NPC with id $id not found.")

        val npc = temporaryRegistry.createNPC(original.entity.type, original.name)

        if (original.hasTrait(SkinTrait::class.java)) {
            val originalSkin = original.getOrAddTrait(SkinTrait::class.java)

            npc.getOrAddTrait(SkinTrait::class.java)
                .setSkinPersistent(originalSkin.skinName, originalSkin.signature, originalSkin.texture)
        }

        if (original.requiresNameHologram()) {
            npc.setAlwaysUseNameHologram(true)
            npc.name = original.fullName
        }

        if (original.hasTrait(HologramTrait::class.java)) {
            val originalHologram = original.getOrAddTrait(HologramTrait::class.java)

            npc.getOrAddTrait(HologramTrait::class.java).apply {
                direction = originalHologram.direction
                lineHeight = originalHologram.lineHeight
                originalHologram.lines.forEach { addLine(it) }
            }
        }

        return npc
    }

    override fun setup(player: Player, npc: NPC) {
        super.setup(player, npc)

        val original =
            CitizensAPI.getNPCRegistry().getById(id) ?: throw IllegalArgumentException("NPC with id $id not found.")
        val filter = original.getOrAddTrait(PlayerFilter::class.java)
        filter.setDenylist()
        original.getOrAddTrait(PlayerFilter::class.java).addPlayer(player.uniqueId)
    }

    override fun teardown(player: Player, npc: NPC) {
        super.teardown(player, npc)

        val original =
            CitizensAPI.getNPCRegistry().getById(id) ?: throw IllegalArgumentException("NPC with id $id not found.")
        original.getOrAddTrait(PlayerFilter::class.java).removePlayer(player.uniqueId)
    }
}

class NpcCinematicAction(
    private val player: Player,
    private val entry: NpcCinematicEntry,
    private val data: NpcData,
) :
    CinematicAction {
    private val assetManager: AssetManager by inject(AssetManager::class.java)
    private val gson: Gson by inject(Gson::class.java, named("bukkitDataParser"))

    private var npc: NPC? = null
    private var recordings: Map<String, Tape<NpcFrame>> = emptyMap()

    override suspend fun setup() {
        super.setup()

        recordings = entry.recordedSegments
            .associate { it.artifact to it.artifact }
            .mapValues { Query.findById<ArtifactEntry>(it.value) }
            .filterValues { it != null }
            .mapValues { assetManager.fetchAsset(it.value!!) }
            .filterValues { it != null }
            .mapValues { gson.fromJson(it.value, object : TypeToken<Tape<NpcFrame>>() {}.type) }


        val firstLocation =
            entry.recordedSegments.asSequence()
                .mapNotNull { recording -> recordings[recording.artifact]?.firstNotNullWhere { it.location } }
                .firstOrNull() ?: player.location

        withContext(plugin.minecraftDispatcher) {
            npc = data.create().apply {
                data.setup(player, this)

                spawn(firstLocation)
            }
        }
    }

    override suspend fun tick(frame: Int) {
        super.tick(frame)
        handleMovement(frame)
    }

    private suspend fun handleMovement(frame: Int) {
        val segment = entry.recordedSegments activeSegmentAt frame
        val npcFrame = segment?.let { recordings[it.artifact]?.get(frame - segment.startFrame) } ?: return

        withContext(plugin.minecraftDispatcher) {
            npc?.let {
                if (npcFrame.location != null) {
                    it.entity.teleport(npcFrame.location)
                }
            }
        }
    }

    override suspend fun teardown() {
        super.teardown()
        withContext(plugin.minecraftDispatcher) {
            npc?.let { data.teardown(player, it) }
        }
    }

    override fun canFinish(frame: Int): Boolean = entry.recordedSegments canFinishAt frame
}