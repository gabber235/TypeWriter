package me.gabber235.typewriter.utils

import me.gabber235.typewriter.Typewriter
import org.bukkit.entity.Player
import org.geysermc.floodgate.api.FloodgateApi
import java.io.File
import java.time.Duration

operator fun File.get(name: String): File = File(this, name)

val Player.isFloodgate: Boolean
    get() {
        if (!Typewriter.plugin.isFloodgateInstalled) return false
        return FloodgateApi.getInstance().isFloodgatePlayer(this.uniqueId)
    }


fun <T> T?.logErrorIfNull(message: String): T? {
    if (this == null) Typewriter.plugin.logger.severe(message)
    return this
}

infix fun <T> Boolean.then(t: T): T? = if (this) t else null


fun Duration.toTicks(): Long = this.toMillis() / 50