package com.typewritermc.basic.entries.action

import com.typewritermc.core.books.pages.Colors
import com.typewritermc.core.entries.Ref
import com.typewritermc.core.extension.annotations.Colored
import com.typewritermc.core.extension.annotations.Entry
import com.typewritermc.core.extension.annotations.MultiLine
import com.typewritermc.core.extension.annotations.Placeholder
import com.typewritermc.engine.paper.entry.Criteria
import com.typewritermc.engine.paper.entry.Modifier
import com.typewritermc.engine.paper.entry.TriggerableEntry
import com.typewritermc.engine.paper.entry.entries.ActionEntry
import com.typewritermc.engine.paper.entry.entries.ActionTrigger
import com.typewritermc.engine.paper.entry.entries.ConstVar
import com.typewritermc.engine.paper.entry.entries.Var
import com.typewritermc.engine.paper.extensions.placeholderapi.parsePlaceholders
import com.typewritermc.engine.paper.snippets.snippet
import com.typewritermc.engine.paper.utils.sendMiniWithResolvers
import net.kyori.adventure.text.minimessage.tag.resolver.Placeholder.parsed
import org.bukkit.entity.Player

private val simpleMessageFormat by snippet("action.simple_message.format", "<message>")

@Entry("simple_message", "Send a message to a player", Colors.RED, "flowbite:message-dots-solid")
/**
 * The `Simple Message Action` is an action that sends a message to the player.
 * This allows you to send a message **without** a speaker.
 *
 * ## How could this be used?
 * Send a simple message to a player without a speaker.
 */
class SimpleMessageActionEntry(
    override val id: String = "",
    override val name: String = "",
    override val criteria: List<Criteria> = emptyList(),
    override val modifiers: List<Modifier> = emptyList(),
    override val triggers: List<Ref<TriggerableEntry>> = emptyList(),
    @Placeholder
    @Colored
    @MultiLine
    val message: Var<String> = ConstVar(""),
) : ActionEntry {
    override fun ActionTrigger.execute() {
        player.sendMiniWithResolvers(simpleMessageFormat, parsed("message", message.get(player, context).parsePlaceholders(player)))
    }
}