package me.gabber235.typewriter.entry.entries

import lirand.api.extensions.events.unregister
import lirand.api.extensions.server.server
import me.gabber235.typewriter.adapters.Tags
import me.gabber235.typewriter.entry.AudienceManager
import me.gabber235.typewriter.entry.ManifestEntry
import me.gabber235.typewriter.entry.Ref
import me.gabber235.typewriter.plugin
import org.bukkit.entity.Player
import org.bukkit.event.Listener
import org.koin.java.KoinJavaComponent.get
import java.util.*
import java.util.concurrent.ConcurrentSkipListSet

@Tags("audience")
interface AudienceEntry : ManifestEntry {
    fun display(): AudienceDisplay
}

@Tags("audience_filter")
interface AudienceFilterEntry : AudienceEntry {
    val children: List<Ref<AudienceEntry>>
    override fun display(): AudienceFilter
}

abstract class AudienceDisplay : Listener {
    protected val playerIds: ConcurrentSkipListSet<UUID> = ConcurrentSkipListSet()
    open val players: List<Player> get() = server.onlinePlayers.filter { it.uniqueId in playerIds }

    open fun initialize() {
        server.pluginManager.registerEvents(this, plugin)
    }

    open fun dispose() {
        players.forEach { removePlayer(it) }
        this.unregister()
    }

    fun addPlayer(player: Player) {
        if (playerIds.isEmpty()) initialize()
        if (!playerIds.add(player.uniqueId)) return
        onPlayerAdd(player)
    }

    fun removePlayer(player: Player) {
        if (!playerIds.remove(player.uniqueId)) return
        onPlayerRemove(player)
        if (playerIds.isEmpty()) dispose()
    }

    abstract fun onPlayerAdd(player: Player)
    abstract fun onPlayerRemove(player: Player)

    open operator fun contains(player: Player): Boolean = contains(player.uniqueId)
    open operator fun contains(uuid: UUID): Boolean = uuid in playerIds
}

class PassThroughDisplay : AudienceDisplay() {
    override fun onPlayerAdd(player: Player) {}
    override fun onPlayerRemove(player: Player) {}
}

abstract class AudienceFilter(
    private val ref: Ref<out AudienceFilterEntry>
) : AudienceDisplay() {
    private val filteredPlayers: ConcurrentSkipListSet<UUID> = ConcurrentSkipListSet()
    override val players: List<Player> get() = server.onlinePlayers.filter { it.uniqueId in filteredPlayers }

    protected val consideredPlayers: List<Player> get() = super.players

    abstract fun filter(player: Player): Boolean

    fun Player.updateFilter(isFiltered: Boolean) {
        if (isFiltered) {
            if (filteredPlayers.add(uniqueId)) {
                get<AudienceManager>(AudienceManager::class.java).addPlayerToChildren(this, ref)
            }
        } else {
            if (filteredPlayers.remove(uniqueId)) {
                get<AudienceManager>(AudienceManager::class.java).removePlayerFromChildren(this, ref)
            }
        }
    }

    fun Player.refresh() = updateFilter(filter(this))

    override fun onPlayerAdd(player: Player) {
        player.refresh()
    }

    override fun onPlayerRemove(player: Player) {
        player.updateFilter(false)
    }

    fun canConsider(player: Player): Boolean = super.contains(player)
    fun canConsider(uuid: UUID): Boolean = super.contains(uuid)

    override fun contains(player: Player): Boolean = contains(player.uniqueId)
    override fun contains(uuid: UUID): Boolean = uuid in filteredPlayers
}

class PassThroughFilter(
    ref: Ref<out AudienceFilterEntry>
) : AudienceFilter(ref) {
    override fun filter(player: Player): Boolean = true
}

