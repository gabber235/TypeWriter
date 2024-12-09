package com.typewritermc.engine.paper.entry.dialogue

import com.destroystokyo.paper.event.player.PlayerJumpEvent
import com.typewritermc.engine.paper.plugin
import com.typewritermc.engine.paper.utils.config
import com.typewritermc.engine.paper.utils.reloadable
import lirand.api.extensions.events.listen
import org.bukkit.event.Cancellable
import org.bukkit.event.EventPriority
import org.bukkit.event.Listener
import org.bukkit.event.player.PlayerEvent
import org.bukkit.event.player.PlayerSwapHandItemsEvent
import org.bukkit.event.player.PlayerToggleSneakEvent
import java.util.*

private val confirmationKeyString by config(
    "confirmationKey", ConfirmationKey.SWAP_HANDS.name, comment = """
    |The key that should be pressed to confirm a dialogue option.
    |Possible values: ${ConfirmationKey.entries.joinToString(", ") { it.name }}
""".trimMargin()
)

val confirmationKey: ConfirmationKey by reloadable {
    val key = ConfirmationKey.fromString(confirmationKeyString)
    if (key == null) {
        plugin.logger.warning("Invalid confirmation key '$confirmationKeyString'. Using default key '${ConfirmationKey.SWAP_HANDS.name}' instead.")
        return@reloadable ConfirmationKey.SWAP_HANDS
    }
    key
}


enum class ConfirmationKey(val keybind: String) {
    SWAP_HANDS("<key:key.swapOffhand>"),
    JUMP("<key:key.jump>"),
    SNEAK("<key:key.sneak>"),
    ;

    private inline fun <reified E : PlayerEvent> listenEvent(
        listener: Listener,
        playerUUID: UUID,
        crossinline block: () -> Unit
    ) {
        plugin.listen(
            listener,
            EventPriority.HIGHEST,
        ) event@{ event: E ->
            if (event.player.uniqueId != playerUUID) return@event
            if (event is PlayerToggleSneakEvent && !event.isSneaking) return@event // Otherwise the event is fired twice
            block()
            if (event is Cancellable) event.isCancelled = true
        }
    }

    fun listen(listener: Listener, playerUUID: UUID, block: () -> Unit) {
        when (this) {
            SWAP_HANDS -> listenEvent<PlayerSwapHandItemsEvent>(listener, playerUUID, block)
            JUMP -> listenEvent<PlayerJumpEvent>(listener, playerUUID, block)
            SNEAK -> listenEvent<PlayerToggleSneakEvent>(listener, playerUUID, block)
        }
    }

    companion object {
        fun fromString(string: String): ConfirmationKey? {
            return entries.find { it.name.equals(string, true) }
        }
    }
}
