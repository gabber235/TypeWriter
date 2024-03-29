package me.gabber235.typewriter.facts

import me.gabber235.typewriter.entry.entries.GroupId
import java.time.LocalDateTime

data class FactData(val value: Int, val lastUpdate: LocalDateTime = LocalDateTime.now())

data class FactId(
    val entryId: String,
    val groupId: GroupId,
)