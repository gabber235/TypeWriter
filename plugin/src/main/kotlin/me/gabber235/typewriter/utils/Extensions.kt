package me.gabber235.typewriter.utils

import me.gabber235.typewriter.Typewriter
import me.gabber235.typewriter.logger
import org.bukkit.entity.Player
import org.geysermc.floodgate.api.FloodgateApi
import org.koin.java.KoinJavaComponent.get
import java.io.File
import java.time.Duration

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