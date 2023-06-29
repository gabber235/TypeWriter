package me.gabber235.typewriter.capture.capturers

import io.papermc.paper.event.player.PlayerArmSwingEvent
import lirand.api.extensions.events.SimpleListener
import lirand.api.extensions.events.listen
import me.gabber235.typewriter.capture.RecordedCapturer
import me.gabber235.typewriter.plugin
import org.bukkit.entity.Player
import org.bukkit.event.HandlerList
import org.bukkit.event.Listener
import org.bukkit.inventory.EquipmentSlot

enum class ArmSwing {
    LEFT, RIGHT, BOTH;

    val swingLeft: Boolean get() = this == LEFT || this == BOTH
    val swingRight: Boolean get() = this == RIGHT || this == BOTH
}

class SwingTapeCapturer(override val title: String) : RecordedCapturer<Tape<ArmSwing>> {
    private val tape = mutableTapeOf<ArmSwing>()
    private var listener: Listener? = null

    private var swing: ArmSwing? = null

    override fun startRecording(player: Player) {
        listener = SimpleListener()

        plugin.listen<PlayerArmSwingEvent>(listener = listener!!) {
            if (it.player.uniqueId != player.uniqueId) return@listen
            addSwing(
                when (it.hand) {
                    EquipmentSlot.OFF_HAND -> ArmSwing.LEFT
                    EquipmentSlot.HAND -> ArmSwing.RIGHT
                    else -> ArmSwing.BOTH
                }
            )
        }
    }

    private fun addSwing(swing: ArmSwing) {
        if (this.swing != null && this.swing != swing) {
            this.swing = ArmSwing.BOTH
            return
        }

        this.swing = swing
    }

    override fun captureFrame(player: Player, frame: Int) {
        val swing = this.swing ?: return
        tape[frame] = swing
        this.swing = null
    }

    override fun stopRecording(player: Player): Tape<ArmSwing> {
        listener?.let { HandlerList.unregisterAll(it) }
        return tape
    }
}