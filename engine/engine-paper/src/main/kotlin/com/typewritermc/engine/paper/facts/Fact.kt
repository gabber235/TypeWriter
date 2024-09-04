package com.typewritermc.engine.paper.facts

import com.typewritermc.engine.paper.entry.entries.GroupId
import java.time.LocalDateTime

data class FactData(val value: Int, val lastUpdate: LocalDateTime = LocalDateTime.now())

data class FactId(
    val entryId: String,
    val groupId: GroupId,
)