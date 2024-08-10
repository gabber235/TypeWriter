package com.caleb.typewriter.vault.entries.action

import com.caleb.typewriter.vault.VaultAdapter
import me.gabber235.typewriter.adapters.Colors
import me.gabber235.typewriter.adapters.Entry
import me.gabber235.typewriter.adapters.modifiers.Help
import me.gabber235.typewriter.entry.Criteria
import me.gabber235.typewriter.entry.Modifier
import me.gabber235.typewriter.entry.Ref
import me.gabber235.typewriter.entry.TriggerableEntry
import me.gabber235.typewriter.entry.entries.ActionEntry
import net.milkbowl.vault.chat.Chat
import org.bukkit.entity.Player

@Entry("set_prefix", "Set Prefix", Colors.RED, "fa6-solid:user-tag")
/**
 * The `Set Prefix Action` action sets the prefix of a player's message
 *
 * ## How could this be used?
 *
 * This could be used for a badge system.
 * When a player completes a certain task, like killing a boss,
 * they could be given a prefix that shows up in chat, like `[Deamon Slayer]`
 */
class SetPrefixActionEntry(
    override val id: String = "",
    override val name: String = "",
    override val criteria: List<Criteria> = emptyList(),
    override val modifiers: List<Modifier> = emptyList(),
    override val triggers: List<Ref<TriggerableEntry>> = emptyList(),
    @Help("The prefix to set.")
    private val prefix: String = "",
) : ActionEntry {
    override fun execute(player: Player) {
        super.execute(player)

        val chat: Chat = VaultAdapter.chat ?: return

        chat.setPlayerPrefix(player, prefix)
    }
}