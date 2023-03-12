package me.gabber235.typewriter.adyeshach.entries.event

import ink.ptms.adyeshach.core.event.AdyeshachEntityInteractEvent
import me.gabber235.typewriter.Typewriter
import me.gabber235.typewriter.adapters.Colors
import me.gabber235.typewriter.adapters.Entry
import me.gabber235.typewriter.adapters.modifiers.EntryIdentifier
import me.gabber235.typewriter.adapters.modifiers.Help
import me.gabber235.typewriter.adyeshach.entries.entity.Npc
import me.gabber235.typewriter.adyeshach.entries.entity.ReferenceNpcEntry
import me.gabber235.typewriter.entry.*
import me.gabber235.typewriter.entry.entries.EventEntry
import me.gabber235.typewriter.utils.Icons
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
		Typewriter.plugin.logger.warning("Could not find a speaker with name $identifier.")
		return
	}
	query findWhere {
		it.identifier == speaker.id
	} startDialogueWithOrNextDialogue player
}

private fun onReferenceNpcInteract(player: Player, npcId: String, query: Query<NpcInteractEventEntry>) {
	val references: List<ReferenceNpcEntry> = Query findWhere { it.npcId.equals(npcId) }
	val identifiers = references.map { it.id }

	query.findWhere {
		it.identifier in identifiers
	} startDialogueWithOrNextDialogue player
}

@EntryListener(NpcInteractEventEntry::class)
fun onNpcClick(event: AdyeshachEntityInteractEvent, query: Query<NpcInteractEventEntry>) {
	val identifier = event.entity.uniqueId

	onReferenceNpcInteract(event.player, identifier, query)

	// onNpcInteract(event.player, identifier, query)
}

