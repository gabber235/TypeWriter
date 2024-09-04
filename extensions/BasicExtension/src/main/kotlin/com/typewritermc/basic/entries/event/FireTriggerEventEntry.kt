package com.typewritermc.basic.entries.event

import com.typewritermc.core.books.pages.Colors
import com.typewritermc.core.extension.annotations.Entry
import com.typewritermc.core.entries.Ref
import com.typewritermc.engine.paper.entry.TriggerableEntry
import com.typewritermc.engine.paper.entry.entries.FireTriggerEventEntry

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