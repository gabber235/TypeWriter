package me.gabber235.typewriter.entry.entity

import me.gabber235.typewriter.entry.Ref
import me.gabber235.typewriter.entry.entries.*
import me.gabber235.typewriter.entry.priority
import me.gabber235.typewriter.entry.ref
import me.gabber235.typewriter.utils.logErrorIfNull

interface SimpleEntityDefinition : EntityDefinitionEntry

interface SimpleEntityInstance : EntityInstanceEntry {
    val definition: Ref<out SimpleEntityDefinition>
    val data: List<Ref<EntityData<*>>>
    val activities: List<Ref<out EntityActivityEntry>>

    override val children: List<Ref<out AudienceEntry>>
        get() = data + activities

    override fun display(): AudienceFilter {
        val definition = definition.get().logErrorIfNull("You must specify a definition for $name")
            ?: return PassThroughFilter(ref())

        val activities = this.activities.mapNotNull { it.get() }.sortedByDescending { it.priority }

        val definitionData = definition.data.mapNotNull {
            val data = it.get() ?: return@mapNotNull null
            data to data.priority
        }

        val maxDefinitionData = definitionData.maxOfOrNull { it.second } ?: 0

        val instanceData = data.mapNotNull {
            val data = it.get() ?: return@mapNotNull null
            data to (data.priority + maxDefinitionData + 1)
        }

        return CommonActivityEntityDisplay(
            ref(),
            definition,
            activities,
            (definitionData + instanceData),
            spawnLocation,
        )
    }
}