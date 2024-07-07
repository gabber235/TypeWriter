package me.gabber235.typewriter.entries.instance

import me.gabber235.typewriter.adapters.Colors
import me.gabber235.typewriter.adapters.Entry
import me.gabber235.typewriter.entry.Ref
import me.gabber235.typewriter.entry.emptyRef
import me.gabber235.typewriter.entry.entity.GroupAdvancedEntityInstance
import me.gabber235.typewriter.entry.entity.IndividualAdvancedEntityInstance
import me.gabber235.typewriter.entry.entity.SharedAdvancedEntityInstance
import me.gabber235.typewriter.entry.entries.*
import org.bukkit.Location

@Entry(
    "shared_advanced_entity_instance",
    "An advanced instance of an entity",
    Colors.YELLOW,
    "material-symbols:settings-account-box"
)
/**
 * The `Shared Advanced Entity Instance` entry is an entity instance
 * that has the activity shared between all players viewing the instance.
 *
 * ## How could this be used?
 * Simple npc's where everyone needs to see the same npc.
 */
class SharedAdvancedEntityInstanceEntry(
    override val id: String = "",
    override val name: String = "",
    override val definition: Ref<out EntityDefinitionEntry> = emptyRef(),
    override val spawnLocation: Location = Location(null, 0.0, 0.0, 0.0),
    override val children: List<Ref<out AudienceEntry>> = emptyList(),
    override val activity: Ref<out SharedEntityActivityEntry> = emptyRef(),
) : SharedAdvancedEntityInstance

@Entry("group_advanced_entity_instance", "An advanced instance of an entity", Colors.YELLOW, "material-symbols:settings-account-box")
/**
 * The `Group Advanced Entity Instance` entry is an entity instance
 * that has the activity shared between a group of players viewing the instance.
 *
 * ## How could this be used?
 * For example, having a party sees the npc in the same place.
 */
class GroupAdvancedEntityInstanceEntry(
    override val id: String = "",
    override val name: String = "",
    override val definition: Ref<out EntityDefinitionEntry> = emptyRef(),
    override val spawnLocation: Location = Location(null, 0.0, 0.0, 0.0),
    override val children: List<Ref<out AudienceEntry>> = emptyList(),
    override val activity: Ref<out SharedEntityActivityEntry> = emptyRef(),
    override val group: Ref<out GroupEntry> = emptyRef(),
) : GroupAdvancedEntityInstance

@Entry(
    "individual_advanced_entity_instance",
    "An advanced instance of an entity",
    Colors.YELLOW,
    "material-symbols:settings-account-box"
)
/**
 * The `Individual Advanced Entity Instance` entry is an entity instance
 * that has the activity unique to each player viewing the instance.
 *
 * It means that two players can see the same instance in different places.
 *
 * ## How could this be used?
 * For escort quests where the player needs to follow the npc such as a guide.
 */
class IndividualAdvancedEntityInstanceEntry(
    override val id: String = "",
    override val name: String = "",
    override val definition: Ref<out EntityDefinitionEntry> = emptyRef(),
    override val spawnLocation: Location = Location(null, 0.0, 0.0, 0.0),
    override val children: List<Ref<out AudienceEntry>> = emptyList(),
    override val activity: Ref<out IndividualEntityActivityEntry> = emptyRef(),
) : IndividualAdvancedEntityInstance