package me.gabber235.typewriter.entry.entries

import lirand.api.extensions.server.server
import me.gabber235.typewriter.adapters.Tags
import me.gabber235.typewriter.entry.StaticEntry
import org.bukkit.entity.Player
import java.util.*

@Tags("group")
interface GroupEntry : StaticEntry {
    /**
     * Get the group id for the given player.
     *
     * If the player is not part of the group any of the possible groups, return null.
     */
    fun groupId(player: Player): GroupId?

    /**
     * Get the group for a given id
     */
    fun group(id: GroupId): Group {
        return Group(server.onlinePlayers.filter { groupId(it) == id })
    }

    /**
     * Get the group where the player is part of.
     * If the player is not part of any group, return null.
     */
    fun group(player: Player): Group? {
        val id = groupId(player) ?: return null
        return group(id)
    }
}

@JvmInline
value class GroupId(val id: String) {
    constructor(uuid: UUID) : this(uuid.toString())
}

class Group(val players: List<Player>)