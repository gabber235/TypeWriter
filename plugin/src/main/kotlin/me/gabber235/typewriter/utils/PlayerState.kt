package me.gabber235.typewriter.utils

import org.bukkit.GameMode
import org.bukkit.Location
import org.bukkit.entity.Player
import java.util.*


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
	;

	override fun store(player: Player): Any = player.store()
	override fun restore(player: Player, value: Any) = player.restore(value)
}

fun Player.state(vararg keys: PlayerStateProvider): PlayerState = state(keys)

@JvmName("stateArray")
fun Player.state(keys: Array<out PlayerStateProvider>): PlayerState {
	return PlayerState(keys.associateWith { it.store(this) })
}

fun Player.restore(state: PlayerState?) {
	state?.state?.forEach { (key, value) -> key.restore(this, value) }
}