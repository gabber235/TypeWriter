package me.gabber235.typewriter.entries.event

import com.github.retrooper.packetevents.protocol.player.InteractionHand
import com.github.retrooper.packetevents.wrapper.play.client.WrapperPlayClientInteractEntity
import me.gabber235.typewriter.adapters.Colors
import me.gabber235.typewriter.adapters.Entry
import me.gabber235.typewriter.entry.*
import me.gabber235.typewriter.entry.entries.EntityDefinitionEntry
import me.gabber235.typewriter.entry.entries.EventEntry
import me.gabber235.typewriter.events.AsyncEntityDefinitionInteract

@Entry(
    "entity_interact_event",
    "When the player clicks on an entity",
    Colors.YELLOW,
    "fa6-solid:people-robbery"
)
/**
 * The `EntityInteractEvent` entry is an event that is triggered when a player interacts with a specific entity.
 *
 * ## How could this be used?
 * This could be used to start dialogue when a player interacts with an entity.
 */
class EntityInteractEventEntry(
    override val id: String = "",
    override val name: String = "",
    override val triggers: List<Ref<TriggerableEntry>> = emptyList(),
    val definition: Ref<EntityDefinitionEntry> = emptyRef(),
) : EventEntry

@EntryListener(EntityInteractEventEntry::class)
fun onEntityInteract(event: AsyncEntityDefinitionInteract, query: Query<EntityInteractEventEntry>) {
    if (event.hand != InteractionHand.MAIN_HAND || event.action == WrapperPlayClientInteractEntity.InteractAction.INTERACT_AT) return
    val definition = event.definition.ref()
    query.findWhere {it.definition == definition } startDialogueWithOrNextDialogue event.player
}