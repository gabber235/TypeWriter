package lirand.api.extensions.world

import org.bukkit.World
import org.bukkit.entity.Entity
import org.bukkit.entity.Firework
import org.bukkit.entity.Projectile
import org.bukkit.inventory.meta.FireworkMeta
import org.bukkit.projectiles.ProjectileSource
import org.bukkit.util.Vector


inline fun Firework.meta(crossinline block: FireworkMeta.() -> Unit) = apply {
	fireworkMeta = fireworkMeta.apply(block)
}

inline fun <reified T : Projectile> ProjectileSource.launchProjectile() = launchProjectile(T::class.java)
inline fun <reified T : Projectile> ProjectileSource.launchProjectile(vector: Vector) =
	launchProjectile(T::class.java, vector)


inline fun <reified E : Entity> World.getEntitiesByClass(): Collection<E> {
	return getEntitiesByClass(E::class.java)
}