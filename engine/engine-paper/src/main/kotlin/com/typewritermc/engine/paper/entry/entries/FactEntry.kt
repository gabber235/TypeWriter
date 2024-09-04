package com.typewritermc.engine.paper.entry.entries

import com.typewritermc.core.entries.Ref
import com.typewritermc.core.extension.annotations.Help
import com.typewritermc.core.extension.annotations.MultiLine
import com.typewritermc.core.extension.annotations.Tags
import com.typewritermc.engine.paper.entry.PlaceholderEntry
import com.typewritermc.engine.paper.entry.StaticEntry
import com.typewritermc.engine.paper.facts.FactData
import com.typewritermc.engine.paper.facts.FactDatabase
import com.typewritermc.engine.paper.facts.FactId
import lirand.api.extensions.server.server
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
            com.typewritermc.engine.paper.entry.entries.GroupId(player.uniqueId)
        }

        return FactId(id, groupId)
    }
}

@Tags("readable-fact")
interface ReadableFactEntry : FactEntry, PlaceholderEntry {
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

    override fun display(player: Player?): String? {
        if (player == null) return null
        return readForPlayersGroup(player).value.toString()
    }
}

@Tags("writable-fact")
interface WritableFactEntry : com.typewritermc.engine.paper.entry.entries.FactEntry {
    fun write(player: Player, value: Int) {
        val factId = identifier(player) ?: return
        write(factId, value)
    }

    fun write(id: FactId, value: Int)
}

@Tags("cachable-fact")
interface CachableFactEntry : com.typewritermc.engine.paper.entry.entries.ReadableFactEntry,
    com.typewritermc.engine.paper.entry.entries.WritableFactEntry {

    override fun readForGroup(groupId: com.typewritermc.engine.paper.entry.entries.GroupId): FactData {
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
interface PersistableFactEntry : com.typewritermc.engine.paper.entry.entries.CachableFactEntry {
    fun canPersist(id: FactId, data: FactData): Boolean = true
}

@Tags("expirable-fact")
interface ExpirableFactEntry : com.typewritermc.engine.paper.entry.entries.CachableFactEntry {
    fun hasExpired(id: FactId, data: FactData): Boolean = false
}