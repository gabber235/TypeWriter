package com.typewritermc.mythicmobs.entries.event

import com.typewritermc.core.books.pages.Colors
import com.typewritermc.core.entries.Query
import com.typewritermc.core.entries.Ref
import com.typewritermc.core.extension.annotations.Entry
import com.typewritermc.core.extension.annotations.EntryListener
import com.typewritermc.core.extension.annotations.Help
import com.typewritermc.core.extension.annotations.Regex
import com.typewritermc.core.interaction.context
import com.typewritermc.engine.paper.entry.TriggerableEntry
import com.typewritermc.engine.paper.entry.entries.EventEntry
import com.typewritermc.engine.paper.entry.startDialogueWithOrNextDialogue
import io.lumine.mythic.bukkit.events.MythicMobInteractEvent

@Entry("mythicmobs_interact_event", "MythicMob Interact Event", Colors.YELLOW, "fa6-solid:dragon")
/**
 * The `MythicMob Interact Event` is fired when a player interacts with a MythicMob.
 *
 * ## How could this be used?
 *
 * This can be used to create a variety of interactions that can occur between a MythicMob and a player. For example, you could create a MythicMob that gives the player an item when they interact with it.
 */
class MythicMobInteractEventEntry(
    override val id: String = "",
    override val name: String = "",
    override val triggers: List<Ref<TriggerableEntry>> = emptyList(),
    @Help("The specific MythicMob type to listen for")
    @Regex
    val mobName: String = "",
) : EventEntry

@EntryListener(MythicMobInteractEventEntry::class)
fun onMythicMobInteractEvent(event: MythicMobInteractEvent, query: Query<MythicMobInteractEventEntry>) {
    query.findWhere {
        it.mobName.toRegex(RegexOption.IGNORE_CASE).matches(event.activeMobType.internalName)
    }.startDialogueWithOrNextDialogue(event.player, context())
}