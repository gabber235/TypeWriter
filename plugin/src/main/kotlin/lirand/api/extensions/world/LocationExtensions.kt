package lirand.api.extensions.world

import org.bukkit.BlockChangeDelegate
import org.bukkit.Effect
import org.bukkit.Location
import org.bukkit.Particle
import org.bukkit.Sound
import org.bukkit.SoundCategory
import org.bukkit.TreeType
import org.bukkit.block.data.BlockData
import org.bukkit.entity.AbstractArrow
import org.bukkit.entity.Entity
import org.bukkit.inventory.ItemStack
import org.bukkit.util.Vector

fun Location.dropItem(item: ItemStack) =
	world!!.dropItem(this, item)

fun Location.dropItemNaturally(item: ItemStack) =
	world!!.dropItemNaturally(this, item)


fun Location.spawnArrow(direction: Vector, speed: Float, spread: Float) =
	world!!.spawnArrow(this, direction, speed, spread)


fun Location.generateTree(type: TreeType) =
	world!!.generateTree(this, type) ?: false

fun Location.generateTree(type: TreeType, delegate: BlockChangeDelegate) =
	world!!.generateTree(this, type, delegate) ?: false


fun Location.strikeLightning() =
	world!!.strikeLightning(this)

fun Location.strikeLightningEffect() =
	world!!.strikeLightningEffect(this)

fun Location.getNearbyEntities(x: Double, y: Double, z: Double): Collection<Entity>? =
	world!!.getNearbyEntities(this, x, y, z)


fun Location.createExplosion(power: Float) =
	world!!.createExplosion(this, power) ?: false

fun Location.createExplosion(power: Float, setFire: Boolean) =
	world!!.createExplosion(this, power, setFire) ?: false


inline fun <reified E : Entity> Location.spawn(noinline builder: (E.() -> Unit)?) =
	world!!.spawn(this, E::class.java, builder)


fun Location.spawnFallingBlock(blockData: BlockData) =
	world!!.spawnFallingBlock(this, blockData)

inline fun <reified A : AbstractArrow> Location.spawnArrow(direction: Vector, speed: Float, spread: Float): A? =
	world!!.spawnArrow(this, direction, speed, spread, A::class.java)


fun Location.playEffect(effect: Effect, data: Int) =
	world!!.playEffect(this, effect, data)

fun Location.playEffect(effect: Effect, data: Int, radius: Int) =
	world!!.playEffect(this, effect, data, radius)

fun <T> Location.playEffect(effect: Effect, data: T) =
	world!!.playEffect(this, effect, data)

fun <T> Location.playEffect(effect: Effect, data: T, radius: Int) =
	world!!.playEffect(this, effect, data, radius)


/**
 * Plays the sound at the location. It
 * will be audible for everyone near it.
 */
fun Location.playSound(
	sound: Sound,
	category: SoundCategory = SoundCategory.MASTER,
	volume: Float = 1f,
	pitch: Float = 1f
) = world!!.playSound(this, sound, category, volume, pitch)

/**
 * Spawns the particle at the location. It
 * will be visible for everyone near it.
 */
fun Location.spawnParticle(
	particle: Particle,
	amount: Int = 1,
	offset: Vector = Vector(),
	extra: Number = 1.0,
	data: BlockData? = null,
	force: Boolean = false
) = world!!.spawnParticle(
	particle, this, amount,
	offset.x, offset.y, offset.z, 
	extra.toDouble(),
	data, force
)