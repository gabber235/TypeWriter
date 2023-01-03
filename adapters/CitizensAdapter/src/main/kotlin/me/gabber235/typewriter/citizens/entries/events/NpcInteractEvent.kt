package me.gabber235.typewriter.citizens.entries.events

import me.gabber235.typewriter.Typewriter
import me.gabber235.typewriter.adapters.Colors
import me.gabber235.typewriter.adapters.Entry
import me.gabber235.typewriter.adapters.modifiers.StaticEntryIdentifier
import me.gabber235.typewriter.citizens.TypewriterTrait
import me.gabber235.typewriter.citizens.entries.entities.Npc
import me.gabber235.typewriter.entry.*
import me.gabber235.typewriter.entry.entries.EventEntry
import me.gabber235.typewriter.utils.Icons
import net.citizensnpcs.api.event.NPCLeftClickEvent
import net.citizensnpcs.api.event.NPCRightClickEvent
import org.bukkit.entity.Player

@Entry("on_npc_interact", "When a player clicks on an NPC", Colors.YELLOW, Icons.PEOPLE_ROBBERY)
class NpcInteractEventEntry(
	override val id: String = "",
	override val name: String = "",
	override val triggers: List<String> = emptyList(),
	@StaticEntryIdentifier(Npc::class)
	val identifier: String = "",
) : EventEntry

private fun onNpcInteract(player: Player, identifier: String, query: Query<NpcInteractEventEntry>) {
	val speaker = Query(Npc::class) findByName identifier
	if (speaker == null) {
		Typewriter.plugin.logger.warning("Could not find a speaker with name $identifier.")
		return
	}
	query.findWhere {
		it.identifier == speaker.id
	}.startInteractionWithOrTrigger(player, "system.dialogue.next")
}

@EntryListener(NpcInteractEventEntry::class)
fun onNpcRightClick(event: NPCRightClickEvent, query: Query<NpcInteractEventEntry>) {
	val identifier = event.npc.getTraitNullable(TypewriterTrait::class.java)?.identifier ?: return
	onNpcInteract(event.clicker, identifier, query)
}

@EntryListener(NpcInteractEventEntry::class)
fun onNpcLeftClick(event: NPCLeftClickEvent, query: Query<NpcInteractEventEntry>) {
	val identifier = event.npc.getTraitNullable(TypewriterTrait::class.java)?.identifier ?: return
	onNpcInteract(event.clicker, identifier, query)
}