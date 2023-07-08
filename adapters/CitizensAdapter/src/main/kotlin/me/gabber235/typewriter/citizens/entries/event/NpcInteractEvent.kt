package me.gabber235.typewriter.citizens.entries.event

import me.gabber235.typewriter.adapters.Colors
import me.gabber235.typewriter.adapters.Entry
import me.gabber235.typewriter.adapters.modifiers.EntryIdentifier
import me.gabber235.typewriter.adapters.modifiers.Help
import me.gabber235.typewriter.citizens.TypewriterTrait
import me.gabber235.typewriter.citizens.entries.entity.Npc
import me.gabber235.typewriter.citizens.entries.entity.ReferenceNpcEntry
import me.gabber235.typewriter.entry.EntryListener
import me.gabber235.typewriter.entry.Query
import me.gabber235.typewriter.entry.entries.EventEntry
import me.gabber235.typewriter.entry.startDialogueWithOrNextDialogue
import me.gabber235.typewriter.logger
import me.gabber235.typewriter.utils.Icons
import net.citizensnpcs.api.event.NPCLeftClickEvent
import net.citizensnpcs.api.event.NPCRightClickEvent
import org.bukkit.entity.Player

@Entry("on_npc_interact", "When a player clicks on an NPC", Colors.YELLOW, Icons.PEOPLE_ROBBERY)
class NpcInteractEventEntry(
    override val id: String = "",
    override val name: String = "",
    override val triggers: List<String> = emptyList(),
    @EntryIdentifier(Npc::class)
    @Help("The identifier of the NPC.")
    val identifier: String = "",
) : EventEntry

private fun onNpcInteract(player: Player, identifier: String, query: Query<NpcInteractEventEntry>) {
    val speaker: Npc? = Query findByName identifier
    if (speaker == null) {
        logger.warning("Could not find a speaker with name $identifier.")
        return
    }
    query findWhere {
        it.identifier == speaker.id
    } startDialogueWithOrNextDialogue player
}

private fun onReferenceNpcInteract(player: Player, npcId: Int, query: Query<NpcInteractEventEntry>) {
    val references: List<ReferenceNpcEntry> = Query findWhere { it.npcId == npcId }
    val identifiers = references.map { it.id }

    query.findWhere {
        it.identifier in identifiers
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