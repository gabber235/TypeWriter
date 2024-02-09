package me.gabber235.typewriter.facts

import me.gabber235.typewriter.entry.entries.FactEntry
import me.gabber235.typewriter.entry.entries.GroupId
import java.time.LocalDateTime
import java.util.*


val FactEntry.formattedName: String
    get() = name.split(".")
        .joinToString(" | ") { part -> part.replaceFirstChar { if (it.isLowerCase()) it.titlecase(Locale.getDefault()) else it.toString() } }
        .split("_")
        .joinToString(" ") { part -> part.replaceFirstChar { if (it.isLowerCase()) it.titlecase(Locale.getDefault()) else it.toString() } }

data class FactData(val value: Int, val lastUpdate: LocalDateTime = LocalDateTime.now())

data class FactId(
    val entryId: String,
    val groupId: GroupId,
)