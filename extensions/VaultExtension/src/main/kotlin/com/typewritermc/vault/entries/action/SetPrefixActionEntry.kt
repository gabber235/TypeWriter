package com.typewritermc.vault.entries.action

import com.typewritermc.vault.VaultInitializer
import com.typewritermc.core.books.pages.Colors
import com.typewritermc.core.extension.annotations.Entry
import com.typewritermc.core.extension.annotations.Help
import com.typewritermc.engine.paper.entry.Criteria
import com.typewritermc.engine.paper.entry.Modifier
import com.typewritermc.core.entries.Ref
import com.typewritermc.engine.paper.entry.TriggerableEntry
import com.typewritermc.engine.paper.entry.entries.ActionEntry
import com.typewritermc.engine.paper.entry.entries.ActionTrigger
import com.typewritermc.engine.paper.entry.entries.ConstVar
import com.typewritermc.engine.paper.entry.entries.Var
import net.milkbowl.vault.chat.Chat
import org.bukkit.entity.Player
import org.koin.java.KoinJavaComponent

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
    private val prefix: Var<String> = ConstVar(""),
) : ActionEntry {
    override fun ActionTrigger.execute() {
        val chat: Chat = KoinJavaComponent.get<VaultInitializer>(VaultInitializer::class.java).chat ?: return

        chat.setPlayerPrefix(player, prefix.get(player, context))
    }
}