package me.gabber235.typewriter.facts

import me.gabber235.typewriter.entry.entries.FactEntry
import java.time.LocalDateTime
import java.util.*


val FactEntry.formattedName: String
    get() = name.split(".")
        .joinToString(" | ") { part -> part.replaceFirstChar { if (it.isLowerCase()) it.titlecase(Locale.getDefault()) else it.toString() } }
        .split("_")
        .joinToString(" ") { part -> part.replaceFirstChar { if (it.isLowerCase()) it.titlecase(Locale.getDefault()) else it.toString() } }

data class Fact(val id: String, val value: Int, val lastUpdate: LocalDateTime = LocalDateTime.now()) {
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other !is Fact) return false

        return id == other.id
    }

    override fun hashCode(): Int = id.hashCode()
}