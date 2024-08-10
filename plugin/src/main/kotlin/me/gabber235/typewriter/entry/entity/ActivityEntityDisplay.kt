package me.gabber235.typewriter.entry.entity

import me.gabber235.typewriter.entry.entries.EntityDefinitionEntry
import org.bukkit.Location
import java.util.UUID

interface ActivityEntityDisplay {
    val creator: EntityCreator
    val definition: EntityDefinitionEntry?
        get() = creator as? EntityDefinitionEntry

    fun playerSeesEntity(playerId: UUID, entityId: Int): Boolean

    /**
     * The location of the entity for the player.
     */
    fun location(playerId: UUID): Location?

    /**
     * Whether the player can view the entity.
     * This is regardless of whether the entity is spawned in for the player.
     * Just that the player has the ability to see the entity.
     *
     * @param playerId The player to check.
     */
    fun canView(playerId: UUID): Boolean
}