package com.typewritermc.engine.paper.entry.entity

import com.typewritermc.core.utils.point.Position
import com.typewritermc.engine.paper.entry.entries.EntityDefinitionEntry
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
    fun position(playerId: UUID): Position?

    /**
     * Whether the player can view the entity.
     * This is regardless of whether the entity is spawned in for the player.
     * Just that the player has the ability to see the entity.
     *
     * @param playerId The player to check.
     */
    fun canView(playerId: UUID): Boolean

    /**
     * Get the base entity id of the entity.
     * This might not be all of the entity ids that are displayed.
     * For example, the entity might be a child of another entity.
     *
     * If you need to check if the entity is visible to the player, use [playerSeesEntity].
     */
    fun entityId(playerId: UUID): Int
}