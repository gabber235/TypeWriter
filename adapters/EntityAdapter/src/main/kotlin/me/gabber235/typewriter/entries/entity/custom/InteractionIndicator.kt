package me.gabber235.typewriter.entries.entity.custom

import me.gabber235.typewriter.adapters.Colors
import me.gabber235.typewriter.adapters.Entry
import me.gabber235.typewriter.adapters.modifiers.OnlyTags
import me.gabber235.typewriter.entries.entity.minecraft.TextDisplayEntity
import me.gabber235.typewriter.entries.quest.InteractEntityObjective
import me.gabber235.typewriter.entry.Query
import me.gabber235.typewriter.entry.Ref
import me.gabber235.typewriter.entry.emptyRef
import me.gabber235.typewriter.entry.entity.FakeEntity
import me.gabber235.typewriter.entry.entity.SimpleEntityDefinition
import me.gabber235.typewriter.entry.entries.EntityData
import me.gabber235.typewriter.entry.entries.EntityDefinitionEntry
import me.gabber235.typewriter.entry.entries.LinesProperty
import me.gabber235.typewriter.entry.inAudience
import me.gabber235.typewriter.entry.quest.trackedQuest
import me.gabber235.typewriter.utils.Sound
import org.bukkit.entity.Player

@Entry(
    "interaction_indicator_definition",
    "Interaction Indicator",
    Colors.BLUE,
    "material-symbols:move-selection-up-rounded"
)
/**
 * The `InteractionIndicator` class is an entry that represents an interaction indicator.
 * When such an indicator is active, it will show an icon above any NPC.
 */
class InteractionIndicatorDefinition(
    override val id: String = "",
    override val name: String = "",
    override val displayName: String = "",
    override val sound: Sound = Sound.EMPTY,
    val definition: Ref<EntityDefinitionEntry> = emptyRef(),
    @OnlyTags("generic_entity_data", "display_data", "text_display_data")
    override val data: List<Ref<EntityData<*>>> = emptyList(),
) : SimpleEntityDefinition {
    override fun create(player: Player): FakeEntity = InteractionIndicatorEntity(player, definition)
}

class InteractionIndicatorEntity(
    player: Player,
    private val definition: Ref<out EntityDefinitionEntry>,
) : TextDisplayEntity(player) {

    override fun tick() {
        super.tick()
        consumeProperties(LinesProperty(indicator()))
    }

    private fun indicator(): String {
        val objectives = Query.findWhere<InteractEntityObjective> {
            it.entity == definition && player.inAudience(it)
        }
        if (objectives.isEmpty()) {
            return ""
        }
        // If one of them is for the tracked quest we want to display a different icon
        val trackedQuest = player.trackedQuest()
        val icon = if (trackedQuest != null && objectives.any { it.quest == trackedQuest }) {
            trackedInteractIndicator
        } else {
            interactIndicator
        }

        return icon
    }
}