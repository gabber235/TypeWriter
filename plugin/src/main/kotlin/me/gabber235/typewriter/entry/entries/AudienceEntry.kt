package me.gabber235.typewriter.entry.entries

import lirand.api.extensions.server.server
import me.gabber235.typewriter.adapters.Tags
import me.gabber235.typewriter.entry.StaticEntry
import org.bukkit.entity.Player
import java.util.*

@Tags("audience")
interface AudienceEntry : StaticEntry {
    /**
     * Get the audience id for the given player.
     *
     * If the player is not part of the audience any of the possible audiences, return null.
     */
    fun audienceId(player: Player): AudienceId?

    /**
     * Get the audience for a given id
     */
    fun audience(id: AudienceId): Audience {
        return Audience(server.onlinePlayers.filter { audienceId(it) == id })
    }

    /**
     * Get the audience where the player is part of.
     * If the player is not part of any audience, return null.
     */
    fun audience(player: Player): Audience? {
        val id = audienceId(player) ?: return null
        return audience(id)
    }
}

@JvmInline
value class AudienceId(val id: String) {
    constructor(uuid: UUID) : this(uuid.toString())
}

class Audience(val players: List<Player>)