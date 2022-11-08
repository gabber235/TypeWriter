package me.gabber235.typewriter.utils

import me.gabber235.typewriter.Typewriter
import org.bukkit.entity.Player
import org.geysermc.floodgate.api.FloodgateApi
import java.io.File

operator fun File.get(name: String): File = File(this, name)

val Player.isFloodgate: Boolean
	get() {
		if (!Typewriter.plugin.isFloodgateInstalled) return false
		return FloodgateApi.getInstance().isFloodgatePlayer(this.uniqueId)
	}

