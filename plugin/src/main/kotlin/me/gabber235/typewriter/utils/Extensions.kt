package me.gabber235.typewriter.utils

import com.destroystokyo.paper.profile.PlayerProfile
import lirand.api.extensions.server.server
import me.gabber235.typewriter.Typewriter
import me.gabber235.typewriter.logger
import net.kyori.adventure.audience.Audience
import net.kyori.adventure.key.Key
import net.kyori.adventure.sound.Sound
import net.kyori.adventure.text.Component
import org.bukkit.Color
import org.bukkit.Location
import org.bukkit.Particle
import org.bukkit.Particle.DustOptions
import org.bukkit.enchantments.Enchantment
import org.bukkit.entity.Player
import org.bukkit.inventory.ItemFlag
import org.bukkit.inventory.meta.ItemMeta
import org.bukkit.inventory.meta.SkullMeta
import org.bukkit.profile.PlayerTextures
import org.geysermc.floodgate.api.FloodgateApi
import org.koin.java.KoinJavaComponent.get
import java.io.File
import java.net.MalformedURLException
import java.net.URL
import java.time.Duration
import java.util.*
import kotlin.math.*


operator fun File.get(name: String): File = File(this, name)

val Player.isFloodgate: Boolean
    get() {
        if (!get<Typewriter>(Typewriter::class.java).isFloodgateInstalled) return false
        return FloodgateApi.getInstance().isFloodgatePlayer(this.uniqueId)
    }


fun <T> T?.logErrorIfNull(message: String): T? {
    if (this == null) logger.severe(message)
    return this
}

infix fun <T> Boolean.then(t: T): T? = if (this) t else null


fun Duration.toTicks(): Long = this.toMillis() / 50
operator fun Duration.times(other: Double): Duration = Duration.ofMillis((this.toMillis() * other).roundToLong())

fun Audience.playSound(
    sound: String,
    source: Sound.Source = Sound.Source.MASTER,
    volume: Float = 1.0f,
    pitch: Float = 1.0f
) = playSound(Sound.sound(Key.key(sound), source, volume, pitch))



val Player.isHighUp: Boolean
    get() = this.location.isHighUp
val Location.isHighUp: Boolean
    get() = this.y >= highUpLocation.y

val Location.highUpLocation: Location
    get() {
        val location = toHighestLocation()
        location.y += 200
        return location
    }

fun Location.distanceSqrt(other: Location): Double? {
    if (world != other.world) return null
    val dx = x - other.x
    val dy = y - other.y
    val dz = z - other.z
    return dx * dx + dy * dy + dz * dz
}

fun Location.lerp(other: Location, amount: Double): Location {
    val percentage = amount.coerceIn(0.0, 1.0)
    val x = this.x + (other.x - this.x) * percentage
    val y = this.y + (other.y - this.y) * percentage
    val z = this.z + (other.z - this.z) * percentage
    return Location(world, x, y, z)
}

val Location.up: Location
    get() = clone().apply { y += 1 }

val Location.down: Location
    get() = clone().apply { y -= 1 }

val Location.firstWalkableLocationBelow: Location
    get() = clone().apply {
        while (block.isPassable) y--
        // We want to be on top of the block
        y++
    }

operator fun Location.component1(): Double = x
operator fun Location.component2(): Double = y
operator fun Location.component3(): Double = z

fun Location.particleSphere(
    player: Player,
    radius: Double,
    color: Color,
    phiDivisions : Int = 16,
    thetaDivisions : Int = 8,
    ) {
    var phi = 0.0
    while (phi < Math.PI) {
        phi += Math.PI / phiDivisions
        var theta = 0.0
        while (theta < 2 * Math.PI) {
            theta += Math.PI / thetaDivisions
            val x = radius * sin(phi) * cos(theta)
            val y = radius * cos(phi)
            val z = radius * sin(phi) * sin(theta)
            player.spawnParticle(
                Particle.REDSTONE,
                this.x + x,
                this.y + y,
                this.z + z,
                1,
                0.0,
                0.0,
                0.0,
                0.0,
                DustOptions(color, sqrt(radius/3).toFloat())
            )
        }
    }
}

fun Double.round(decimals: Int): Double {
    var multiplier = 1.0
    repeat(decimals) { multiplier *= 10 }

    return round(this * multiplier) / multiplier
}

fun Float.round(decimals: Int): Float {
    var multiplier = 1.0
    repeat(decimals) { multiplier *= 10 }

    return (round(this * multiplier) / multiplier).toFloat()
}

val Int.digits: Int
    get() = if (this == 0) 1 else log10(abs(this.toDouble())).toInt() + 1

fun String.rightPad(length: Int, padChar: Char = ' '): String {
    return if (this.length >= length) this else this + padChar.toString().repeat(length - this.length)
}

val String.lineCount: Int
    get() = this.count { it == '\n' } + 1

val <T : Any> Optional<T>?.optional: Optional<T> get() = Optional.ofNullable(this?.orElse(null))
val <T : Any> T?.optional: Optional<T> get() = Optional.ofNullable(this)

var ItemMeta.loreString: String?
    get() = lore()?.joinToString("\n") { it.asMini() }
    set(value) {
        lore(value?.split("\n")?.map { "<!i><white>$it".asMini() })
    }

var ItemMeta.name: String?
    get() = if (hasDisplayName()) displayName()?.asMini() else null
    set(value) = displayName(if (!value.isNullOrEmpty()) "<!i>$value".asMini() else Component.text(" "))

fun ItemMeta.unClickable(): ItemMeta {
    addEnchant(Enchantment.BINDING_CURSE, 1, true)
    addItemFlags(ItemFlag.HIDE_ENCHANTS)
    return this
}

private val RANDOM_UUID =
    UUID.fromString("92864445-51c5-4c3b-9039-517c9927d1b4") // We reuse the same "random" UUID all the time

private fun getProfile(url: String): PlayerProfile {
    val profile: PlayerProfile = server.createProfile(RANDOM_UUID) // Get a new player profile
    val textures: PlayerTextures = profile.textures
    textures.skin = try {
        URL(url) // The URL to the skin, for example: https://textures.minecraft.net/texture/18813764b2abc94ec3c3bc67b9147c21be850cdf996679703157f4555997ea63a
    } catch (exception: MalformedURLException) {
        throw RuntimeException("Invalid URL", exception)
    }
    profile.setTextures(textures) // Set the textures back to the profile
    return profile
}

fun SkullMeta.applySkinUrl(url: String) {
    playerProfile = getProfile(url)
}
