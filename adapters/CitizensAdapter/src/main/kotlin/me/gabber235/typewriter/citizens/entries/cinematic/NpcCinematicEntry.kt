package me.gabber235.typewriter.citizens.entries.cinematic

import com.github.shynixn.mccoroutine.bukkit.launch
import com.github.shynixn.mccoroutine.bukkit.minecraftDispatcher
import com.github.shynixn.mccoroutine.bukkit.ticks
import kotlinx.coroutines.delay
import kotlinx.coroutines.withContext
import me.gabber235.typewriter.adapters.Colors
import me.gabber235.typewriter.adapters.modifiers.Help
import me.gabber235.typewriter.adapters.modifiers.Segments
import me.gabber235.typewriter.adapters.modifiers.WithRotation
import me.gabber235.typewriter.citizens.CitizensAdapter.temporaryRegistry
import me.gabber235.typewriter.entry.entries.*
import me.gabber235.typewriter.logger
import me.gabber235.typewriter.plugin
import me.gabber235.typewriter.utils.Icons
import net.citizensnpcs.api.CitizensAPI
import net.citizensnpcs.api.npc.NPC
import net.citizensnpcs.api.trait.trait.PlayerFilter
import net.citizensnpcs.trait.SkinTrait
import org.bukkit.Location
import org.bukkit.entity.EntityType
import org.bukkit.entity.Player

interface NpcCinematicEntry : CinematicEntry {
    @Help("The location to spawn the NPC at.")
    @WithRotation
    val startLocation: Location

    @Help("Start the NPC to move to a location or orientation.")
    @Segments(Colors.PINK, Icons.PERSON_WALKING)
    val movementSegments: List<NpcMovementSegment>
}

data class NpcMovementSegment(
    override val startFrame: Int = 0,
    override val endFrame: Int = 0,

    @Help("The location to move the NPC to.")
    @WithRotation
    val location: Location = Location(null, 0.0, 0.0, 0.0, 0.0f, 0.0f),

    /// If set to true, the NPC will teleport to the location instead of navigating to it.
    /// If the location is in a different world, the NPC will always teleport.
    @Help("Does the NPC navigate to the location or teleport?")
    val teleport: Boolean = false,
) : Segment

interface NpcData {
    /// Create a new NPC with the data.
    fun create(): NPC

    /// Ran when the cinematic is started.
    fun setup(player: Player, npc: NPC) {
        npc.getOrAddTrait(PlayerFilter::class.java).only(player.uniqueId)
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

        return npc
    }

    override fun setup(player: Player, npc: NPC) {
        super.setup(player, npc)

        val original =
            CitizensAPI.getNPCRegistry().getById(id) ?: throw IllegalArgumentException("NPC with id $id not found.")
        original.getOrAddTrait(PlayerFilter::class.java).hide(player.uniqueId)
    }

    override fun teardown(player: Player, npc: NPC) {
        super.teardown(player, npc)

        val original =
            CitizensAPI.getNPCRegistry().getById(id) ?: throw IllegalArgumentException("NPC with id $id not found.")
        original.getOrAddTrait(PlayerFilter::class.java).unhide(player.uniqueId)
    }
}

class NpcCinematicAction(
    private val player: Player,
    private val entry: NpcCinematicEntry,
    private val data: NpcData,
) :
    CinematicAction {

    private var npc: NPC? = null
    override suspend fun setup() {
        super.setup()

        withContext(plugin.minecraftDispatcher) {
            npc = data.create().apply {
                data.setup(player, this)

                spawn(entry.startLocation)
            }
        }
    }

    private var previousMovementSegment: NpcMovementSegment? = null

    override suspend fun tick(frame: Int) {
        super.tick(frame)
        handleMovement(frame)
    }

    private suspend fun handleMovement(frame: Int) {
        val segment = entry.movementSegments activeSegmentAt frame

        if (segment == previousMovementSegment) return

        previousMovementSegment?.let { stopMovement(it) }

        segment?.let { startMovement(it) }
    }

    private suspend fun startMovement(segment: NpcMovementSegment) {
        previousMovementSegment = segment

        withContext(plugin.minecraftDispatcher) {
            if (segment.teleport || segment.location.world != npc?.entity?.world) {
                npc?.teleport(segment.location, org.bukkit.event.player.PlayerTeleportEvent.TeleportCause.PLUGIN)
            } else {
                navigateToLocation(segment.location)
            }
        }
    }

    private fun navigateToLocation(location: Location) {
        npc?.navigator?.let { navigator ->
            navigator.localParameters.useNewPathfinder(true)
            navigator.localParameters.pathDistanceMargin(0.2)
            navigator.localParameters.distanceMargin(0.5)
//            navigator.localParameters.destinationTeleportMargin(0.2)
            navigator.localParameters.addSingleUseCallback {
                // TODO Check if segment is still active
                if (isAtLocation(location)) return@addSingleUseCallback
                // If the npc is too far away from the target location, retry navigating to it
                plugin.launch {
                    delay(2.ticks)
                    navigateToLocation(location)
                }
            }
            navigator.setTarget(location)
        }
    }

    private fun isAtLocation(location: Location): Boolean {
        val maxDistance = 1.0
        val npcLocation = npc?.entity?.location ?: return false
        return npcLocation.distanceSquared(location) < maxDistance * maxDistance
    }


    private suspend fun stopMovement(segment: NpcMovementSegment) {
        previousMovementSegment = null
        withContext(plugin.minecraftDispatcher) {
            npc?.navigator?.cancelNavigation()

            // Check if the npc is close enough to the target location
            if (!segment.teleport && !isAtLocation(segment.location)) {
                npc?.teleport(segment.location, org.bukkit.event.player.PlayerTeleportEvent.TeleportCause.PLUGIN)
                logger.warning("NPC ${npc?.name} was teleported to ${segment.location} because it was too far away from the target location. You may want to increase the duration of the movement segment.")
            }
        }
    }

    override suspend fun teardown() {
        super.teardown()
        withContext(plugin.minecraftDispatcher) {
            npc?.let { data.teardown(player, it) }
        }
    }

    override fun canFinish(frame: Int): Boolean = entry.movementSegments canFinishAt frame

}