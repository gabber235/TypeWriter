package me.gabber235.typewriter.entries.action

import com.google.common.io.ByteStreams
import me.gabber235.typewriter.adapters.Colors
import me.gabber235.typewriter.adapters.Entry
import me.gabber235.typewriter.adapters.modifiers.Help
import me.gabber235.typewriter.entry.Criteria
import me.gabber235.typewriter.entry.Modifier
import me.gabber235.typewriter.entry.Ref
import me.gabber235.typewriter.entry.TriggerableEntry
import me.gabber235.typewriter.entry.entries.ActionEntry
import me.gabber235.typewriter.plugin
import org.bukkit.entity.Player

@Entry("switch_server_action", "Switches the player to another server", Colors.RED, "fluent:server-link-16-filled")
/**
 * The `Switch Server Action` is an action that switches the player to another server.
 *
 * ## How could this be used?
 *
 * This could be used to switch a player from one server to another when interacting with an entity, maybe clicking a button.
 */
class SwitchServerActionEntry(
    override val id: String = "",
    override val name: String = "",
    override val criteria: List<Criteria>,
    override val modifiers: List<Modifier>,
    override val triggers: List<Ref<TriggerableEntry>> = emptyList(),
    @Help("The server the player has switched to")
    val server: String = "",
): ActionEntry {
    override fun execute(player: Player) {
        super.execute(player)

        plugin.server.messenger.registerOutgoingPluginChannel(plugin, "BungeeCord")

        val out = ByteStreams.newDataOutput()
        out.writeUTF("Connect")
        out.writeUTF(server)
        player.sendPluginMessage(plugin, "BungeeCord", out.toByteArray())

        plugin.server.messenger.unregisterOutgoingPluginChannel(plugin, "BungeeCord")
    }
}