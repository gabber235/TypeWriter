package me.gabber235.typewriter.entries.event

import me.gabber235.typewriter.adapters.Colors
import me.gabber235.typewriter.adapters.Entry
import me.gabber235.typewriter.entry.Ref
import me.gabber235.typewriter.entry.TriggerableEntry
import me.gabber235.typewriter.entry.entries.CustomCommandEntry

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
) : CustomCommandEntry

