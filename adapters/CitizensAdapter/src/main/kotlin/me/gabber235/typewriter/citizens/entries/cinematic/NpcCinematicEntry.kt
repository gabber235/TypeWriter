package me.gabber235.typewriter.citizens.entries.cinematic

import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import me.gabber235.typewriter.adapters.Colors
import me.gabber235.typewriter.adapters.modifiers.Capture
import me.gabber235.typewriter.adapters.modifiers.Help
import me.gabber235.typewriter.adapters.modifiers.Segments
import me.gabber235.typewriter.capture.capturers.*
import me.gabber235.typewriter.citizens.entries.artifact.CitizensNpcMovementArtifact
import me.gabber235.typewriter.citizens.entries.artifact.NpcFrame
import me.gabber235.typewriter.citizens.entries.artifact.NpcRecordedDataCapturer
import me.gabber235.typewriter.entry.AssetManager
import me.gabber235.typewriter.entry.Ref
import me.gabber235.typewriter.entry.emptyRef
import me.gabber235.typewriter.entry.entries.*
import me.gabber235.typewriter.utils.ThreadType
import org.bukkit.Location
import org.bukkit.entity.Player
import org.bukkit.inventory.EquipmentSlot
import org.bukkit.inventory.EquipmentSlot.*
import org.bukkit.inventory.ItemStack
import org.koin.core.qualifier.named
import org.koin.java.KoinJavaComponent

interface NpcCinematicEntry : CinematicEntry {
    @Help("Recorded segments of the NPC's interactions")
    @Segments(Colors.PINK, "fa6-solid:person-walking")
    val recordedSegments: List<NpcRecordedSegment>
}

class NpcRecordedSegment(
    override val startFrame: Int = 0,
    override val endFrame: Int = 0,
    @Help("The artifact for the recorded interactions data")
    @Capture(NpcRecordedDataCapturer::class)
    val artifact: Ref<CitizensNpcMovementArtifact> = emptyRef(),
) : Segment

interface NpcData<N> {
    val threadType: ThreadType
        get() = ThreadType.REMAIN

    /// Create a new NPC with the data.
    fun create(player: Player, location: Location): N

    /// Spawn the NPC in the world.
    fun spawn(player: Player, npc: N, location: Location)

    fun handleMovement(player: Player, npc: N, location: Location)
    fun handleSneaking(player: Player, npc: N, sneaking: Boolean)
    fun handlePunching(player: Player, npc: N, punching: ArmSwing)
    fun handleInventory(player: Player, npc: N, slot: EquipmentSlot, itemStack: ItemStack)

    /// Ran when the cinematic is stopped.
    fun teardown(player: Player, npc: N)
}

class NpcCinematicAction<N>(
    private val player: Player,
    private val entry: NpcCinematicEntry,
    private val data: NpcData<N>,
) :
    CinematicAction {
    private val assetManager: AssetManager by KoinJavaComponent.inject(AssetManager::class.java)
    private val gson: Gson by KoinJavaComponent.inject(Gson::class.java, named("bukkitDataParser"))

    private var npc: N? = null
    private var recordings: Map<String, Tape<NpcFrame>> = emptyMap()

    override suspend fun setup() {
        super.setup()

        recordings = entry.recordedSegments
            .associate { it.artifact.id to it.artifact.get() }
            .filterValues { it != null }
            .mapValues { assetManager.fetchAsset(it.value!!) }
            .filterValues { it != null }
            .mapValues { gson.fromJson(it.value, object : TypeToken<Tape<NpcFrame>>() {}.type) }


        val firstLocation =
            entry.recordedSegments.asSequence()
                .mapNotNull { recording -> recordings[recording.artifact.id]?.firstNotNullWhere { it.location } }
                .firstOrNull() ?: player.location

        withCorrectContext {
            npc = data.create(player, firstLocation).apply {
                data.spawn(player, this, firstLocation)
            }
        }
    }

    override suspend fun tick(frame: Int) {
        super.tick(frame)

        val segment = (entry.recordedSegments activeSegmentAt frame) ?: return
        val recording = recordings[segment.artifact.id] ?: return
        val segmentFrame = frame - segment.startFrame
        withCorrectContext {
            npc?.let {
                handleMovement(player, it, recording.getFrame(segmentFrame) { location })
                handleSneaking(player, it, recording.getFrame(segmentFrame) { sneaking })
                handlePunching(player, it, recording.getExactFrame(segmentFrame) { swing })

                handleInventory(player, it, HAND, recording.getExactFrame(segmentFrame) { mainHand })
                handleInventory(player, it, OFF_HAND, recording.getExactFrame(segmentFrame) { offHand })
                handleInventory(player, it, HEAD, recording.getExactFrame(segmentFrame) { helmet })
                handleInventory(player, it, CHEST, recording.getExactFrame(segmentFrame) { chestplate })
                handleInventory(player, it, LEGS, recording.getExactFrame(segmentFrame) { leggings })
                handleInventory(player, it, FEET, recording.getExactFrame(segmentFrame) { boots })
            }
        }
    }

    private fun handleMovement(player: Player, npc: N, location: Location?) {
        if (location == null) return
        data.handleMovement(player, npc, location)
    }

    private fun handleSneaking(player: Player, npc: N, sneaking: Boolean?) {
        if (sneaking == null) return
        data.handleSneaking(player, npc, sneaking)
    }

    private fun handlePunching(player: Player, npc: N, punching: ArmSwing?) {
        if (punching == null) return
        data.handlePunching(player, npc, punching)
    }

    private fun handleInventory(player: Player, npc: N, slot: EquipmentSlot, itemStack: ItemStack?) {
        if (itemStack == null) return
        data.handleInventory(player, npc, slot, itemStack)
    }

    override suspend fun teardown() {
        super.teardown()
        withCorrectContext {
            npc?.let { data.teardown(player, it) }
        }
    }

    private suspend inline fun withCorrectContext(noinline block: suspend () -> Unit) {
        data.threadType.switchContext(block)
    }

    override fun canFinish(frame: Int): Boolean = entry.recordedSegments canFinishAt frame
}