package lirand.api.extensions.world

import org.bukkit.Effect
import org.bukkit.Instrument
import org.bukkit.Location
import org.bukkit.Note
import org.bukkit.Particle
import org.bukkit.Sound
import org.bukkit.SoundCategory
import org.bukkit.block.data.BlockData
import org.bukkit.entity.Player
import org.bukkit.inventory.ItemStack
import org.bukkit.inventory.PlayerInventory
import org.bukkit.plugin.Plugin
import org.bukkit.util.Vector

fun PlayerInventory.clearArmor() {
	setArmorContents(arrayOfNulls<ItemStack?>(4))
}

fun PlayerInventory.clearAll() {
	clear()
	clearArmor()
}

fun Player.playSound(
	sound: Sound,
	category: SoundCategory = SoundCategory.MASTER,
	volume: Float = 1f,
	pitch: Float = 1f
) = playSound(location, sound, category, volume, pitch)

fun Player.playNote(instrument: Instrument, note: Note) {
	playNote(location, instrument, note)
}

fun Player.spawnParticle(
	particle: Particle,
	location: Location,
	amount: Int = 1,
	offset: Vector = Vector(),
	extra: Number = 1.0,
	data: BlockData? = null
) = spawnParticle(
	particle, location, amount,
	offset.x, offset.y, offset.z,
	extra.toDouble(), data
)

fun <T> Player.playEffect(effect: Effect, data: T? = null) {
	playEffect(location, effect, data)
}


/**
 * Hides the player for all onlinePlayers.
 */
fun Player.disappear(plugin: Plugin) {
	server.onlinePlayers
		.filter { it != this }
		.forEach { it.hidePlayer(plugin, this) }
}

/**
 * Shows the player for all onlinePlayers.
 */
fun Player.appear(plugin: Plugin) {
	server.onlinePlayers
		.filter { it != this }
		.forEach { it.showPlayer(plugin, this) }
}

/**
 * Hides all online players from this player.
 */
fun Player.hideOnlinePlayers(plugin: Plugin) {
	server.onlinePlayers.filter { it != this }.forEach { hidePlayer(plugin, it) }
}

/**
 * Shows all online players to this player.
 */
fun Player.showOnlinePlayers(plugin: Plugin) {
	server.onlinePlayers.filter { it != this }.forEach { showPlayer(plugin, it) }
}