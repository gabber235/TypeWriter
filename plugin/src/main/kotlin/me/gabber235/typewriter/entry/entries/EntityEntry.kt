package me.gabber235.typewriter.entry.entries

import me.gabber235.typewriter.adapters.Tags
import me.gabber235.typewriter.adapters.modifiers.Help
import me.gabber235.typewriter.entry.*
import me.gabber235.typewriter.entry.entity.ActivityCreator
import me.gabber235.typewriter.entry.entity.EntityCreator
import me.gabber235.typewriter.utils.Sound
import org.bukkit.entity.Player
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
    val type: KClass<P>
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
interface EntityInstanceEntry : AudienceFilterEntry

@Tags("entity_activity")
@ChildOnly
interface EntityActivityEntry : AudienceFilterEntry, ActivityCreator, PriorityEntry {
    override val children: List<Ref<out AudienceEntry>>
        get() = emptyList()

    override fun display(): AudienceFilter = PassThroughFilter(ref())
}