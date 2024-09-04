package com.typewritermc.basic.entries.event

import dev.jorel.commandapi.CommandTree
import dev.jorel.commandapi.kotlindsl.playerExecutor
import com.typewritermc.core.books.pages.Colors
import com.typewritermc.core.extension.annotations.Entry
import com.typewritermc.core.entries.Ref
import com.typewritermc.engine.paper.entry.TriggerableEntry
import com.typewritermc.engine.paper.entry.entries.CustomCommandEntry
import com.typewritermc.engine.paper.entry.triggerAllFor

@Entry("on_run_command", "When a player runs a custom command", Colors.YELLOW, "mingcute:terminal-fill")
/**
 * The `Run Command Event` event is triggered when a command is run. This event can be used to add custom commands to the server.
 *
 * :::caution
 * This event is used for commands that **do not** already exist. If you are trying to detect when a player uses an already existing command, use the [`Detect Command Ran Event`](on_detect_command_ran) instead.
 * :::
 */
class RunCommandEventEntry(
    override val id: String = "",
    override val name: String = "",
    override val triggers: List<Ref<TriggerableEntry>> = emptyList(),
    override val command: String = "",
) : CustomCommandEntry {
    override fun CommandTree.builder() {
        playerExecutor { player, _ ->
            triggerAllFor(player)
        }
    }
}

