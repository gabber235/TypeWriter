package me.gabber235.typewriter.entries.instance

import me.gabber235.typewriter.adapters.Colors
import me.gabber235.typewriter.adapters.Entry
import me.gabber235.typewriter.entry.Ref
import me.gabber235.typewriter.entry.emptyRef
import me.gabber235.typewriter.entry.entity.IndividualAdvancedEntityInstance
import me.gabber235.typewriter.entry.entity.SharedAdvancedEntityInstance
import me.gabber235.typewriter.entry.entries.AudienceEntry
import me.gabber235.typewriter.entry.entries.EntityDefinitionEntry
import me.gabber235.typewriter.entry.entries.IndividualEntityActivityEntry
import me.gabber235.typewriter.entry.entries.SharedEntityActivityEntry
import org.bukkit.Location

@Entry(
    "shared_advanced_entity_instance",
    "An advanced instance of an entity",
    Colors.YELLOW,
    "material-symbols:settings-account-box"
)
class SharedAdvancedEntityInstanceEntry(
    override val id: String = "",
    override val name: String = "",
    override val definition: Ref<out EntityDefinitionEntry> = emptyRef(),
    override val spawnLocation: Location = Location(null, 0.0, 0.0, 0.0),
    override val children: List<Ref<out AudienceEntry>> = emptyList(),
    override val activity: Ref<out SharedEntityActivityEntry> = emptyRef(),
) : SharedAdvancedEntityInstance

@Entry(
    "individual_advanced_entity_instance",
    "An advanced instance of an entity",
    Colors.YELLOW,
    "material-symbols:settings-account-box"
)
class IndividualAdvancedEntityInstanceEntry(
    override val id: String = "",
    override val name: String = "",
    override val definition: Ref<out EntityDefinitionEntry> = emptyRef(),
    override val spawnLocation: Location = Location(null, 0.0, 0.0, 0.0),
    override val children: List<Ref<out AudienceEntry>> = emptyList(),
    override val activity: Ref<out IndividualEntityActivityEntry> = emptyRef(),
) : IndividualAdvancedEntityInstance