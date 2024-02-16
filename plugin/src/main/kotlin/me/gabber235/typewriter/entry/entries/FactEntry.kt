package me.gabber235.typewriter.entry.entries

import lirand.api.extensions.server.server
import me.gabber235.typewriter.adapters.Tags
import me.gabber235.typewriter.adapters.modifiers.Help
import me.gabber235.typewriter.adapters.modifiers.MultiLine
import me.gabber235.typewriter.entry.Ref
import me.gabber235.typewriter.entry.StaticEntry
import me.gabber235.typewriter.facts.FactData
import me.gabber235.typewriter.facts.FactDatabase
import me.gabber235.typewriter.facts.FactId
import org.bukkit.entity.Player
import org.koin.java.KoinJavaComponent.get
import java.util.*

@Tags("fact")
interface FactEntry : StaticEntry {
    @MultiLine
    @Help("A comment to keep track of what this fact is used for.")
    val comment: String

    @Help("The group that this fact is for.")
    val group: Ref<GroupEntry>

    fun identifier(player: Player): FactId? {
        val entry = group.get()

        val groupId = if (entry != null) {
            // If the player is not in an group, we don't want to do anything with this fact
            entry.groupId(player) ?: return null
        } else {
            // If no group entry is set, we assume that the player is the group for backwards compatibility
            GroupId(player.uniqueId)
        }

        return FactId(id, groupId)
    }
}

@Tags("readable-fact")
interface ReadableFactEntry : FactEntry {
    fun readForPlayersGroup(player: Player): FactData {
        val factId = identifier(player) ?: return FactData(0)
        return readForGroup(factId.groupId)
    }

    fun readForGroup(groupId: GroupId): FactData {
        val entry = group.get()
        if (entry == null) {
            // If no group entry is set, we assume that the player is the group for backwards compatibility
            val player = server.getPlayer(UUID.fromString(groupId.id)) ?: return FactData(0)
            return readSinglePlayer(player)
        }
        val group = entry.group(groupId)
        return group.players.map { readSinglePlayer(it) }.let { FactData(it.sumOf(FactData::value)) }
    }

    /**
     * This should **not** be used directly. Use [readForPlayersGroup] instead.
     */
    fun readSinglePlayer(player: Player): FactData
}

@Tags("writable-fact")
interface WritableFactEntry : FactEntry {
    fun write(player: Player, value: Int) {
        val factId = identifier(player) ?: return
        write(factId, value)
    }

    fun write(id: FactId, value: Int)
}

@Tags("cachable-fact")
interface CachableFactEntry : ReadableFactEntry, WritableFactEntry {

    override fun readForGroup(groupId: GroupId): FactData {
        return read(FactId(id, groupId))
    }

    override fun readSinglePlayer(player: Player): FactData {
        throw UnsupportedOperationException("This method should not be used directly. Use readForPlayer instead.")
    }

    fun read(id: FactId): FactData {
        val factDatabase: FactDatabase = get(FactDatabase::class.java)
        return factDatabase[id] ?: FactData(0)
    }

    override fun write(id: FactId, value: Int) {
        if (!canCache(id, read(id))) return
        val factDatabase: FactDatabase = get(FactDatabase::class.java)
        factDatabase[id] = FactData(value)
    }

    fun canCache(id: FactId, factData: FactData): Boolean = true
}

@Tags("persistable-fact")
interface PersistableFactEntry : CachableFactEntry {
    fun canPersist(id: FactId, data: FactData): Boolean = true
}

@Tags("expirable-fact")
interface ExpirableFactEntry : CachableFactEntry {
    fun hasExpired(id: FactId, data: FactData): Boolean = false
}