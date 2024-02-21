package me.gabber235.typewriter.entry.entity

import me.gabber235.typewriter.entry.Ref
import me.gabber235.typewriter.entry.descendants
import me.gabber235.typewriter.entry.entries.*
import me.gabber235.typewriter.entry.priority
import me.gabber235.typewriter.entry.ref
import me.gabber235.typewriter.logger

interface AdvancedEntityInstance : EntityInstanceEntry {
    val definition: Ref<out EntityDefinitionEntry>

    override fun display(): AudienceFilter {
        val definition = definition.get()
        if (definition == null) {
            logger.warning("You must specify a definition for $name")
            return PassThroughFilter(ref())
        }

        return children.toAdvancedEntityDisplay(
            ref(),
            definition,

            definition.data.mapNotNull { it.get() }.map { it to it.priority })
    }
}

/**
 * If the activities are only linked from the root with only other activities, we can use a `CommonActivityEntityDisplay`.
 * Otherwise, we need to have a `PlayerSpecificActivityEntityDisplay`.
 */
fun List<Ref<out AudienceEntry>>.toAdvancedEntityDisplay(
    ref: Ref<out AudienceFilterEntry>,
    creator: EntityCreator,
    baseSuppliers: List<Pair<EntityData<*>, Int>> = emptyList(),
): AudienceFilter {
    val activityCreators = descendants(EntityActivityEntry::class)
        .mapNotNull { it.get() }


    val maxBaseSupplier = baseSuppliers.maxOfOrNull { it.second } ?: 0
    val overrideSuppliers = descendants(EntityData::class)
        .mapNotNull { it.get() }
        .map { it to (it.priority + maxBaseSupplier + 1) }

    val suppliers = (baseSuppliers + overrideSuppliers)

    val activitiesOnlyConnectedByActivities = activityOnlyConnectedByActivity(true)

    return if (activitiesOnlyConnectedByActivities) CommonActivityEntityDisplay(
        ref,
        creator,
        activityCreators,
        suppliers
    )
    else PlayerSpecificActivityEntityDisplay(ref, creator, activityCreators, suppliers)
}

private fun List<Ref<out AudienceEntry>>.activityOnlyConnectedByActivity(
    seenOnlySeenActivities: Boolean,
): Boolean {
    return all { ref ->
        val entry = ref.get() ?: return@all true
        when {
            entry is EntityActivityEntry && seenOnlySeenActivities -> entry.children
                .activityOnlyConnectedByActivity(true)

            entry is EntityActivityEntry -> false
            entry is AudienceFilterEntry -> entry.children
                .activityOnlyConnectedByActivity(false)

            else -> true
        }
    }
}