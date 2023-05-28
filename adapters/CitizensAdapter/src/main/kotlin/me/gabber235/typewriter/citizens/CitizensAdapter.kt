package me.gabber235.typewriter.citizens

import App
import me.gabber235.typewriter.adapters.Adapter
import me.gabber235.typewriter.adapters.TypewriteAdapter
import me.gabber235.typewriter.capture.MultiTapeRecordedCapturer
import me.gabber235.typewriter.capture.capturers.LocationTapeCapturer
import me.gabber235.typewriter.logger
import me.gabber235.typewriter.plugin
import net.citizensnpcs.api.CitizensAPI
import net.citizensnpcs.api.npc.MemoryNPCDataStore
import net.citizensnpcs.api.npc.NPCRegistry
import net.citizensnpcs.api.trait.TraitInfo
import org.bukkit.Location

@Adapter("Citizens", "For the Citizens plugin", App.VERSION)
object CitizensAdapter : TypewriteAdapter() {
    private var tmpRegistry: NPCRegistry? = null
    val temporaryRegistry: NPCRegistry
        get() = tmpRegistry ?: CitizensAPI.createAnonymousNPCRegistry(MemoryNPCDataStore()).also { tmpRegistry = it }

    override fun initialize() {
        if (!plugin.server.pluginManager.isPluginEnabled("Citizens")) {
            logger.warning("Citizens plugin not found, try installing it or disabling the Citizens adapter")
            return
        }

        CitizensAPI.getTraitFactory().registerTrait(TraitInfo.create(TypewriterTrait::class.java))
        tmpRegistry = CitizensAPI.createAnonymousNPCRegistry(MemoryNPCDataStore())
//
//        plugin.listen<PlayerItemHeldEvent> { event ->
//            val player = event.player
//            if (event.newSlot == 0) {
//                if (player.isRecording) return@listen
//                plugin.launch {
//                    val recorder = CinematicRecorder(
//                        player,
//                        TestTapeCapturer("Test Location Tape"),
//                        50..200,
//                        "hotel_intro_cinematic"
//                    )
//                    val data = Recorders.record(player, recorder)
//
//                    player.msg("Recorded frames: ${data.minFrame} - ${data.maxFrame} (${data.duration})")
//
//                    val firstFrame = data.firstFrame ?: return@launch
//                    val startLocation = firstFrame.location
//                    val npc = temporaryRegistry.createNPC(EntityType.PLAYER, player.name, startLocation)
//                    npc.isFlyable = true
//
//                    for (i in data.minFrame..data.maxFrame) {
//                        val frame = data[i] ?: continue
//                        val location = frame.location ?: npc.entity.location
//
//                        npc.entity.teleport(location)
//
//                        player.spawnParticle(Particle.VILLAGER_HAPPY, location, 1)
//                        player.spawnParticle(Particle.SOUL_FIRE_FLAME, npc.storedLocation, 1, 0.0, 0.0, 0.0, 0.0)
//
//                        delay(1.ticks)
//                    }
//
//                    npc.destroy()
//                }
//            }
//        }
    }

    override fun shutdown() {
        CitizensAPI.getTraitFactory().deregisterTrait(TraitInfo.create(TypewriterTrait::class.java))
        tmpRegistry?.deregisterAll()
        tmpRegistry = null
    }
}


private data class Test(
    val location: Location?,
)

private class TestTapeCapturer(title: String) : MultiTapeRecordedCapturer<Test>(title) {
    private val location by tapeCapturer(::LocationTapeCapturer)

    override fun combineFrame(frame: Int): Test {
        return Test(
            location = location[frame],
        )
    }
}