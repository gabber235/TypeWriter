package me.gabber235.typewriter.entry.entries

import me.gabber235.typewriter.adapters.Tags
import me.gabber235.typewriter.adapters.modifiers.Help
import me.gabber235.typewriter.adapters.modifiers.WithRotation
import me.gabber235.typewriter.entry.*
import me.gabber235.typewriter.entry.entity.*
import me.gabber235.typewriter.utils.Sound
import org.bukkit.Location
import org.bukkit.entity.Player
import org.checkerframework.checker.units.qual.A
import kotlin.reflect.KClass

@Tags("speaker")
interface SpeakerEntry : PlaceholderEntry {
    @Help("The name of the entity that will be displayed in the chat (e.g. 'Steve' or 'Alex').")
    val displayName: String

    @Help("The sound that will be played when the entity speaks.")
    val sound: Sound

    override fun display(player: Player?): String? = displayName
}

/**
 * Must override equals and hashCode to compare the data.
 *
 * Must have a companion object that implements `PropertyCollectorSupplier`.
 */
interface EntityProperty

interface PropertyCollectorSupplier<P : EntityProperty> {
    val type: KClass<P>
    fun collector(suppliers: List<PropertySupplier<out P>>): PropertyCollector<P>
}

interface PropertySupplier<P : EntityProperty> {
    fun type(): KClass<P>
    fun build(player: Player): P
    fun canApply(player: Player): Boolean
}

interface PropertyCollector<P : EntityProperty> {
    val type: KClass<P>
    fun collect(player: Player): P?
}

@Tags("entity_data")
interface EntityData<P : EntityProperty> : AudienceEntry, PropertySupplier<P>, PriorityEntry {
    override fun canApply(player: Player): Boolean = player.inAudience(this)
    override fun display(): AudienceDisplay = PassThroughDisplay()
}

@Tags("generic_entity_data")
interface GenericEntityData<P : EntityProperty> : EntityData<P>


@Tags("living_entity_data")
interface LivingEntityData<P : EntityProperty> : EntityData<P>


@Tags("entity_definition")
interface EntityDefinitionEntry : ManifestEntry, SpeakerEntry, EntityCreator {
    val data: List<Ref<EntityData<*>>>
}

@Tags("entity_instance")
interface EntityInstanceEntry : AudienceFilterEntry {
    val definition: Ref<out EntityDefinitionEntry>

    @WithRotation
    val spawnLocation: Location
}

@Tags("entity_activity")
interface EntityActivityEntry : ActivityCreator, ManifestEntry, PriorityEntry

@Tags("shared_entity_activity")
interface SharedEntityActivityEntry : EntityActivityEntry {
    override fun create(
        context: ActivityContext,
        currentLocation: LocationProperty
    ): EntityActivity<ActivityContext> {
        if (context !is SharedActivityContext) throw WrongActivityContextException(context, SharedActivityContext::class, this)
        return create(context, currentLocation) as EntityActivity<ActivityContext>
    }
    fun create(context: SharedActivityContext, currentLocation: LocationProperty): EntityActivity<SharedActivityContext>
}

@Tags("individual_entity_activity")
interface IndividualEntityActivityEntry : EntityActivityEntry {
    override fun create(
        context: ActivityContext,
        currentLocation: LocationProperty
    ): EntityActivity<ActivityContext> {
        if (context !is IndividualActivityContext) throw WrongActivityContextException(context, IndividualActivityContext::class, this)
        return create(context, currentLocation) as EntityActivity<ActivityContext>
    }
    fun create(context: IndividualActivityContext, currentLocation: LocationProperty): EntityActivity<IndividualActivityContext>
}

@Tags("generic_entity_activity")
interface GenericEntityActivityEntry : SharedEntityActivityEntry, IndividualEntityActivityEntry {
    override fun create(
        context: ActivityContext,
        currentLocation: LocationProperty
    ): EntityActivity<ActivityContext>

    override fun create(
        context: SharedActivityContext,
        currentLocation: LocationProperty
    ): EntityActivity<SharedActivityContext> {
        return create(context as ActivityContext, currentLocation) as EntityActivity<SharedActivityContext>
    }

    override fun create(
        context: IndividualActivityContext,
        currentLocation: LocationProperty
    ): EntityActivity<IndividualActivityContext> {
        return create(context as ActivityContext, currentLocation) as EntityActivity<IndividualActivityContext>
    }
}

class WrongActivityContextException(context: ActivityContext, expected: KClass<out ActivityContext>, entry: EntityActivityEntry) : IllegalStateException("""
    |The activity context for ${entry.name} is not of the expected type.
    |Expected: $expected
    |Actual: $context
    |
    |This happens when you try to mix shared and individual activities.
    |For example, you can't use a shared activity on an individual entity.
    |And you can't use an individual activity on a shared entity.
    |
    |To fix this, you need to make sure that the activity matches the entity visibility.
    |If you need more help, please join the TypeWriter Discord! https://discord.gg/gs5QYhfv9x
""".trimMargin())