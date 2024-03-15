package me.gabber235.typewriter.citizens.entries.event

import me.gabber235.typewriter.adapters.Colors
import me.gabber235.typewriter.adapters.Entry
import me.gabber235.typewriter.adapters.modifiers.Help
import me.gabber235.typewriter.citizens.TypewriterTrait
import me.gabber235.typewriter.citizens.entries.entity.CitizensNpc
import me.gabber235.typewriter.entry.*
import me.gabber235.typewriter.entry.entries.EventEntry
import me.gabber235.typewriter.logger
import net.citizensnpcs.api.event.NPCLeftClickEvent
import net.citizensnpcs.api.event.NPCRightClickEvent
import org.bukkit.entity.Player

@Entry("on_npc_interact", "When a player clicks on an NPC", Colors.YELLOW, "fa6-solid:people-robbery")
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
    val identifier: Ref<CitizensNpc>,
) : EventEntry

private fun onNpcInteract(player: Player, identifier: String, query: Query<NpcInteractEventEntry>) {
    val speaker: CitizensNpc? = Query findByName identifier
    if (speaker == null) {
        logger.warning("Could not find a speaker with name $identifier.")
        return
    }
    query findWhere {
        it.identifier.id == speaker.id
    } startDialogueWithOrNextDialogue player
}

private fun onReferenceNpcInteract(player: Player, npcId: Int, query: Query<NpcInteractEventEntry>) {
    val references: List<CitizensNpc> = Query findWhere { it.npcId == npcId }
    val identifiers = references.map { it.id }

    query.findWhere {
        it.identifier.id in identifiers
    } startDialogueWithOrNextDialogue player
}

@EntryListener(NpcInteractEventEntry::class)
fun onNpcRightClick(event: NPCRightClickEvent, query: Query<NpcInteractEventEntry>) {
    val identifier = event.npc.getTraitNullable(TypewriterTrait::class.java)?.identifier

    if (identifier == null) {
        onReferenceNpcInteract(event.clicker, event.npc.id, query)
        return
    }

    onNpcInteract(event.clicker, identifier, query)
}

@EntryListener(NpcInteractEventEntry::class)
fun onNpcLeftClick(event: NPCLeftClickEvent, query: Query<NpcInteractEventEntry>) {
    val identifier = event.npc.getTraitNullable(TypewriterTrait::class.java)?.identifier

    if (identifier == null) {
        onReferenceNpcInteract(event.clicker, event.npc.id, query)
        return
    }

    onNpcInteract(event.clicker, identifier, query)
}