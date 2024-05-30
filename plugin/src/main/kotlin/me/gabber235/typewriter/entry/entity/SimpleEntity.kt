package me.gabber235.typewriter.entry.entity

import me.gabber235.typewriter.adapters.Tags
import me.gabber235.typewriter.adapters.modifiers.Help
import me.gabber235.typewriter.adapters.modifiers.OnlyTags
import me.gabber235.typewriter.entry.Ref
import me.gabber235.typewriter.entry.entries.*
import me.gabber235.typewriter.entry.priority
import me.gabber235.typewriter.entry.ref
import me.gabber235.typewriter.utils.logErrorIfNull

interface SimpleEntityDefinition : EntityDefinitionEntry

@Tags("shared_entity_instance")
interface SimpleEntityInstance : EntityInstanceEntry {
    override val definition: Ref<out SimpleEntityDefinition>
    val data: List<Ref<EntityData<*>>>
    @Help("What the entity will do.")
    @OnlyTags("shared_entity_activity")
    val activity: Ref<out EntityActivityEntry>

    override val children: List<Ref<out AudienceEntry>>
        get() = data

    override fun display(): AudienceFilter {
        val definition = definition.get().logErrorIfNull("You must specify a definition for $name")
            ?: return PassThroughFilter(ref())

        val activity = this.activity.get() ?: IdleActivity

        val definitionData = definition.data.withPriority()

        val maxDefinitionData = definitionData.maxOfOrNull { it.second } ?: 0

        val instanceData = data.mapNotNull {
            val data = it.get() ?: return@mapNotNull null
            data to (data.priority + maxDefinitionData + 1)
        }

        return SharedActivityEntityDisplay(
            ref(),
            definition,
            activity,
            (definitionData + instanceData),
            spawnLocation,
        )
    }
}

fun List<Ref<EntityData<*>>>.withPriority(): List<Pair<EntityData<*>, Int>> = mapNotNull {
    val data = it.get() ?: return@mapNotNull null
    data to data.priority
}
