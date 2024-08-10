package me.gabber235.typewriter.entries.quest

import me.gabber235.typewriter.adapters.Colors
import me.gabber235.typewriter.adapters.Entry
import me.gabber235.typewriter.adapters.modifiers.Help
import me.gabber235.typewriter.entry.Criteria
import me.gabber235.typewriter.entry.Ref
import me.gabber235.typewriter.entry.emptyRef
import me.gabber235.typewriter.entry.entries.AudienceEntry
import me.gabber235.typewriter.entry.entries.EntityDefinitionEntry
import me.gabber235.typewriter.entry.entries.ObjectiveEntry
import me.gabber235.typewriter.entry.entries.QuestEntry
import me.gabber235.typewriter.snippets.snippet
import java.util.*

private val displayTemplate by snippet("quest.objective.interact_entity", "Interact with <entity>")

@Entry("interact_entity_objective", "Interact with an entity", Colors.BLUE_VIOLET, "ph:hand-tap-fill")
/**
 * The `InteractEntityObjective` class is an entry that represents an objective to interact with an entity.
 * When such an objective is active, it will show an icon above any NPC.
 */
class InteractEntityObjective(
    override val id: String = "",
    override val name: String = "",
    override val children: List<Ref<out AudienceEntry>> = emptyList(),
    override val quest: Ref<QuestEntry> = emptyRef(),
    override val criteria: List<Criteria> = emptyList(),
    @Help("The entity that the player needs to interact with.")
    val entity: Ref<out EntityDefinitionEntry> = emptyRef(),
    @Help("The objective display that will be shown to the player. Use &lt;entity&gt; to replace the entity name.")
    val overrideDisplay: Optional<String> = Optional.empty(),
    override val priorityOverride: Optional<Int> = Optional.empty(),
) : ObjectiveEntry {
    override val display: String
        get() = overrideDisplay.orElseGet { displayTemplate }.run {
            val entityName = entity.get()?.displayName ?: ""
            replace("<entity>", entityName)
        }
}