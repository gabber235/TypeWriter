package me.gabber235.typewriter.utils

import me.gabber235.typewriter.plugin
import org.bukkit.GameMode
import org.bukkit.Location
import org.bukkit.entity.Player
import org.bukkit.potion.PotionEffect
import org.bukkit.potion.PotionEffectType

interface PlayerStateProvider {
    fun store(player: Player): Any
    fun restore(player: Player, value: Any)
}

data class PlayerState(
    val state: Map<PlayerStateProvider, Any>
)

enum class GenericPlayerStateProvider(private val store: Player.() -> Any, private val restore: Player.(Any) -> Unit) :
    PlayerStateProvider {
    LOCATION({ location }, { teleport(it as Location) }),
    GAME_MODE({ gameMode }, { gameMode = it as GameMode }),
    EXP({ exp }, { exp = it as Float }),
    LEVEL({ level }, { level = it as Int }),
    ALLOW_FLIGHT({ allowFlight }, { allowFlight = it as Boolean }),
    FLYING({ isFlying }, { isFlying = it as Boolean }),

    // All Players that are visible to the player
    VISIBLE_PLAYERS({
        server.onlinePlayers.filter { it != this && canSee(it) }.map { it.uniqueId.toString() }.toList()
    }, { data ->
        val visible = data as List<*>
        server.onlinePlayers.filter { it != this && it.uniqueId.toString() in visible }
            .forEach { showPlayer(plugin, it) }
    }),

    // All Players that can see the player
    SHOWING_PLAYER({
        server.onlinePlayers.filter { it != this && it.canSee(this) }.map { it.uniqueId.toString() }.toList()
    }, { data ->
        val showing = data as List<*>
        server.onlinePlayers.filter { it != this && it.uniqueId.toString() in showing }
            .forEach { it.showPlayer(plugin, this) }
    })
    ;

    override fun store(player: Player): Any = player.store()
    override fun restore(player: Player, value: Any) = player.restore(value)
}

data class EffectStateProvider(
    private val effect: PotionEffectType,
) : PlayerStateProvider {
    override fun store(player: Player): Any {
        return player.getPotionEffect(effect) ?: return false
    }

    override fun restore(player: Player, value: Any) {
        if (value !is PotionEffect) {
            player.removePotionEffect(effect)
            return
        }
        player.addPotionEffect(value)
    }
}

fun Player.state(vararg keys: PlayerStateProvider): PlayerState = state(keys)

@JvmName("stateArray")
fun Player.state(keys: Array<out PlayerStateProvider>): PlayerState {
    return PlayerState(keys.associateWith { it.store(this) })
}

fun Player.restore(state: PlayerState?) {
    state?.state?.forEach { (key, value) -> key.restore(this, value) }
}