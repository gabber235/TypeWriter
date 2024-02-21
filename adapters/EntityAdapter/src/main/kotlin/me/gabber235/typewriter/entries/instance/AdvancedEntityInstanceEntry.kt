package me.gabber235.typewriter.entries.instance

import me.gabber235.typewriter.adapters.Colors
import me.gabber235.typewriter.adapters.Entry
import me.gabber235.typewriter.entry.Ref
import me.gabber235.typewriter.entry.emptyRef
import me.gabber235.typewriter.entry.entity.AdvancedEntityInstance
import me.gabber235.typewriter.entry.entries.AudienceEntry
import me.gabber235.typewriter.entry.entries.EntityDefinitionEntry

@Entry(
    "advanced_entity_instance",
    "An advanced instance of an entity",
    Colors.YELLOW,
    "material-symbols:settings-account-box"
)
class AdvancedEntityInstanceEntry(
    override val id: String = "",
    override val name: String = "",
    override val definition: Ref<out EntityDefinitionEntry> = emptyRef(),
    override val children: List<Ref<out AudienceEntry>> = emptyList(),
) : AdvancedEntityInstance