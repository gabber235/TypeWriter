package me.gabber235.typewriter.entries.event

import de.oliver.fancynpcs.api.events.NpcInteractEvent
import me.gabber235.typewriter.adapters.Colors
import me.gabber235.typewriter.adapters.Entry
import me.gabber235.typewriter.adapters.modifiers.Help
import me.gabber235.typewriter.entries.entity.FancyNpc
import me.gabber235.typewriter.entry.*
import me.gabber235.typewriter.entry.entries.EventEntry

@Deprecated("Use the EntityAdapter instead")
@Entry("fancy_on_npc_interact", "When a player clicks on an NPC", Colors.YELLOW, "fa6-solid:people-robbery")
/**
 * The `NPC Interact Event` is fired when a player interacts with an NPC.
 *
 * ## How could this be used?
 *
 * This can be used to create a variety of interactions that can occur between an NPC and a player. For example, you could create an NPC that gives the player an item when they interact with it.
 */
class NpcInteractEventEntry(
    override val id: String = "",
    override val name: String = "",
    override val triggers: List<Ref<TriggerableEntry>> = emptyList(),
    @Help("The identifier of the NPC.")
    // The NPC that needs to be interacted with.
    val identifier: Ref<FancyNpc> = emptyRef(),
) : EventEntry


@EntryListener(NpcInteractEventEntry::class)
fun onNpcInteract(event: NpcInteractEvent, query: Query<NpcInteractEventEntry>) {
    val entries: List<FancyNpc> = Query findWhere { it.id == event.npc.data.id }
    val identifiers = entries.map { it.id }

    query findWhere {
        it.identifier.id in identifiers
    } startDialogueWithOrNextDialogue event.player
}
