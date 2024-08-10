package me.gabber235.typewriter.entries.event

import me.gabber235.typewriter.adapters.Colors
import me.gabber235.typewriter.adapters.Entry
import me.gabber235.typewriter.entry.Ref
import me.gabber235.typewriter.entry.TriggerableEntry
import me.gabber235.typewriter.entry.entries.FireTriggerEventEntry

@Entry("fire_trigger_event", "Trigger the event when a player runs `/tw fire <entry id/name> [player]`", Colors.YELLOW, "mingcute:firework-fill")
/**
 * The `FireTriggerEventEntry` is an event that fires its triggers when the player runs `/tw fire <entry id/name> [player]`
 *
 * ## How could this be used?
 * This could be used to trigger an event when a player runs `/tw fire <entry id/name> [player]`
 */
class FireTriggerEventEntry(
    override val id: String = "",
    override val name: String = "",
    override val triggers: List<Ref<TriggerableEntry>> = emptyList(),
) : FireTriggerEventEntry