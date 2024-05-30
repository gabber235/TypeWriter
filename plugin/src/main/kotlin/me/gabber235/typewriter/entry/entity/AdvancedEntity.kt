package me.gabber235.typewriter.entry.entity

import me.gabber235.typewriter.adapters.Tags
import me.gabber235.typewriter.entry.Ref
import me.gabber235.typewriter.entry.descendants
import me.gabber235.typewriter.entry.entries.*
import me.gabber235.typewriter.entry.priority
import me.gabber235.typewriter.entry.ref
import me.gabber235.typewriter.logger
import org.bukkit.Location

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
    creator: (Ref<out EntityInstanceEntry>, EntityDefinitionEntry, ActivityCreator, List<Pair<EntityData<*>, Int>>, Location) -> AudienceFilter,
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