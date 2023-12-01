package me.gabber235.typewriter.citizens.entries.cinematic

import com.github.shynixn.mccoroutine.bukkit.minecraftDispatcher
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import kotlinx.coroutines.withContext
import me.gabber235.typewriter.adapters.Colors
import me.gabber235.typewriter.adapters.modifiers.Capture
import me.gabber235.typewriter.adapters.modifiers.EntryIdentifier
import me.gabber235.typewriter.adapters.modifiers.Help
import me.gabber235.typewriter.adapters.modifiers.Segments
import me.gabber235.typewriter.capture.capturers.*
import me.gabber235.typewriter.citizens.CitizensAdapter.temporaryRegistry
import me.gabber235.typewriter.citizens.entries.artifact.NpcFrame
import me.gabber235.typewriter.citizens.entries.artifact.NpcMovementArtifact
import me.gabber235.typewriter.citizens.entries.artifact.NpcRecordedDataCapturer
import me.gabber235.typewriter.entry.AssetManager
import me.gabber235.typewriter.entry.Query
import me.gabber235.typewriter.entry.entries.*
import me.gabber235.typewriter.plugin
import me.gabber235.typewriter.utils.Icons
import net.citizensnpcs.api.CitizensAPI
import net.citizensnpcs.api.npc.NPC
import net.citizensnpcs.api.trait.trait.Equipment
import net.citizensnpcs.api.trait.trait.Equipment.EquipmentSlot.*
import net.citizensnpcs.api.trait.trait.MobType
import net.citizensnpcs.api.trait.trait.PlayerFilter
import net.citizensnpcs.trait.HologramTrait
import net.citizensnpcs.trait.SkinTrait
import net.citizensnpcs.trait.SneakTrait
import org.bukkit.Location
import org.bukkit.Material
import org.bukkit.entity.EntityType
import org.bukkit.entity.LivingEntity
import org.bukkit.entity.Player
import org.bukkit.inventory.ItemStack
import org.koin.core.qualifier.named
import org.koin.java.KoinJavaComponent.inject

interface NpcCinematicEntry : CinematicEntry {
    @Help("Recorded segments of the NPC's interactions")
    @Segments(Colors.PINK, Icons.PERSON_WALKING)
    val recordedSegments: List<NpcRecordedSegment>
}

class NpcRecordedSegment(
    override val startFrame: Int = 0,
    override val endFrame: Int = 0,
    @Help("The artifact for the recorded interactions data")
    @EntryIdentifier(NpcMovementArtifact::class)
    @Capture(NpcRecordedDataCapturer::class)
    val artifact: String = "",
) : Segment


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

        npc.getOrAddTrait(Equipment::class.java).apply {
            set(HELMET, player.inventory.helmet)
            set(CHESTPLATE, player.inventory.chestplate)
            set(LEGGINGS, player.inventory.leggings)
            set(BOOTS, player.inventory.boots)
        }

        npc.data()[NPC.Metadata.NAMEPLATE_VISIBLE] = false

        return npc
    }
}

data class ReferenceNpcData(val id: Int) : NpcData {
    override fun create(): NPC {
        val original =
            CitizensAPI.getNPCRegistry().getById(id) ?: throw IllegalArgumentException("NPC with id $id not found.")

        val type = original.getOrAddTrait(MobType::class.java).type
        val npc = temporaryRegistry.createNPC(type, original.name)

        if (original.hasTrait(SkinTrait::class.java)) {
            val originalSkin = original.getOrAddTrait(SkinTrait::class.java)

            npc.getOrAddTrait(SkinTrait::class.java)
                .setSkinPersistent(originalSkin.skinName ?: "", originalSkin.signature, originalSkin.texture)
        }

        if (original.requiresNameHologram()) {
            npc.setAlwaysUseNameHologram(true)
            npc.name = original.fullName
        }

        if (original.hasTrait(HologramTrait::class.java)) {
            val originalHologram = original.getOrAddTrait(HologramTrait::class.java)

            npc.getOrAddTrait(HologramTrait::class.java).apply {
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

        if (filter.isAllowlist) {
            filter.removePlayer(player.uniqueId)
        } else {
            filter.addPlayer(player.uniqueId)
        }
    }

    override fun teardown(player: Player, npc: NPC) {
        super.teardown(player, npc)

        val original =
            CitizensAPI.getNPCRegistry().getById(id) ?: throw IllegalArgumentException("NPC with id $id not found.")
        val filter = original.getOrAddTrait(PlayerFilter::class.java)
        if (filter.isAllowlist) {
            filter.addPlayer(player.uniqueId)
        } else {
            filter.removePlayer(player.uniqueId)
        }
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

        val segment = (entry.recordedSegments activeSegmentAt frame) ?: return
        val recording = recordings[segment.artifact] ?: return
        val segmentFrame = frame - segment.startFrame
        withContext(plugin.minecraftDispatcher) {
            npc?.let {
                handleMovement(it, recording.getFrame(segmentFrame) { location })
                handleSneaking(it, recording.getFrame(segmentFrame) { sneaking })
                handlePunching(it, recording.getExactFrame(segmentFrame) { swing })

                handleInventory(it, HAND, recording.getExactFrame(segmentFrame) { mainHand })
                handleInventory(it, OFF_HAND, recording.getExactFrame(segmentFrame) { offHand })
                handleInventory(it, HELMET, recording.getExactFrame(segmentFrame) { helmet })
                handleInventory(it, CHESTPLATE, recording.getExactFrame(segmentFrame) { chestplate })
                handleInventory(it, LEGGINGS, recording.getExactFrame(segmentFrame) { leggings })
                handleInventory(it, BOOTS, recording.getExactFrame(segmentFrame) { boots })
            }
        }
    }

    private fun handleMovement(npc: NPC, location: Location?) {
        if (location == null) return
        npc.entity?.teleport(location)
    }

    private fun handleSneaking(npc: NPC, sneaking: Boolean?) {
        if (sneaking == null) return
        val sneakingTrait = npc.getOrAddTrait(SneakTrait::class.java)
        if (sneakingTrait.isSneaking != sneaking) {
            sneakingTrait.isSneaking = sneaking
        }
    }

    private fun handlePunching(npc: NPC, punching: ArmSwing?) {
        if (punching == null) return
        val entity = npc.entity ?: return
        if (entity !is LivingEntity) return
        if (punching.swingLeft) entity.swingOffHand()
        if (punching.swingRight) entity.swingMainHand()
    }

    private fun handleInventory(npc: NPC, slot: Equipment.EquipmentSlot, itemStack: ItemStack?) {
        if (itemStack == null) return
        val equipmentTrait: Equipment = npc.getOrAddTrait(Equipment::class.java)
        equipmentTrait.set(slot, itemStack)

        if (itemStack.type == Material.DIAMOND_SWORD) {
            println(itemStack.serialize())
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