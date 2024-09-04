package com.typewritermc.engine.paper.entry.entity

import com.typewritermc.core.entries.Ref
import com.typewritermc.core.entries.priority
import com.typewritermc.core.entries.ref
import com.typewritermc.core.extension.annotations.Help
import com.typewritermc.core.extension.annotations.Tags
import com.typewritermc.core.utils.point.Position
import com.typewritermc.engine.paper.entry.descendants
import com.typewritermc.engine.paper.entry.entries.*
import com.typewritermc.engine.paper.logger

@Tags("shared_entity_instance")
interface SharedAdvancedEntityInstance : EntityInstanceEntry {
    val activity: Ref<out SharedEntityActivityEntry>

    override fun display(): AudienceFilter {
        val activityCreator = this.activity.get() ?: IdleActivity

        return toAdvancedEntityDisplay(
            activityCreator,
            ::SharedActivityEntityDisplay,
        )
    }
}

@Tags("group_entity_instance")
interface GroupAdvancedEntityInstance : EntityInstanceEntry {
    val activity: Ref<out SharedEntityActivityEntry>

    @Help("The group that this entity instance belongs to.")
    val group: Ref<out GroupEntry>

    override fun display(): AudienceFilter {
        val activityCreator = this.activity.get() ?: IdleActivity

        val group = this.group.get() ?: throw IllegalStateException("No group found for the group entity instance.")

        return toAdvancedEntityDisplay(
            activityCreator,
        ) { ref, definition, activityCreator, suppliers, spawnLocation ->
            GroupActivityEntityDisplay(ref, definition, activityCreator, suppliers, spawnLocation, group)
        }
    }
}

@Tags("individual_entity_instance")
interface IndividualAdvancedEntityInstance : EntityInstanceEntry {
    val activity: Ref<out IndividualEntityActivityEntry>

    override fun display(): AudienceFilter {
        val activityCreator = this.activity.get() ?: IdleActivity

        return toAdvancedEntityDisplay(
            activityCreator,
            ::IndividualActivityEntityDisplay,
        )
    }
}

private fun EntityInstanceEntry.toAdvancedEntityDisplay(
    activityCreator: ActivityCreator,
    creator: (Ref<out EntityInstanceEntry>, EntityDefinitionEntry, ActivityCreator, List<Pair<EntityData<*>, Int>>, Position) -> AudienceFilter,
): AudienceFilter {
    val definition = definition.get()
    if (definition == null) {
        logger.warning("You must specify a definition for $name")
        return PassThroughFilter(ref())
    }

    val baseSuppliers = definition.data.withPriority()

    val maxBaseSupplier = baseSuppliers.maxOfOrNull { it.second } ?: 0
    val overrideSuppliers = children.descendants(EntityData::class)
        .mapNotNull { it.get() }
        .map { it to (it.priority + maxBaseSupplier + 1) }

    val suppliers = (baseSuppliers + overrideSuppliers)

    return creator(
        ref(),
        definition,
        activityCreator,
        suppliers,
        spawnLocation,
    )
}