package lirand.api.extensions.server

import org.bukkit.Bukkit
import org.bukkit.Server
import org.bukkit.WorldCreator

val server get() = Bukkit.getServer()

val nmsVersion = server.javaClass.getPackage().name
	.split(".")[3].substring(1)

val nmsNumberVersion: Int = nmsVersion.split("_")[1].toInt()


val Server.mainWorld get() = worlds[0]!!

var Server.whitelist: Boolean
	get() = hasWhitelist()
	set(value) = setWhitelist(value)

fun WorldCreator.create() = server.createWorld(this)