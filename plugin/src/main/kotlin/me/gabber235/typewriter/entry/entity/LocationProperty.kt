package me.gabber235.typewriter.entry.entity

import me.gabber235.typewriter.entry.entries.EntityProperty
import org.bukkit.Location

data class LocationProperty(
    val location: Location,
) : EntityProperty