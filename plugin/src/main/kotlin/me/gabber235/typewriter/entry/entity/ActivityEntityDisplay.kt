package me.gabber235.typewriter.entry.entity

import me.gabber235.typewriter.entry.entries.EntityDefinitionEntry
import java.util.UUID

interface ActivityEntityDisplay {
    val creator: EntityCreator
    val definition: EntityDefinitionEntry?
        get() = creator as? EntityDefinitionEntry
    fun playerHasEntity(playerId: UUID, entityId: Int): Boolean
}