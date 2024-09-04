package lirand.api.extensions.server

import org.bukkit.Bukkit
import org.bukkit.Server
import org.bukkit.WorldCreator

val server get() = Bukkit.getServer()

val craftBukkitPackage = server.javaClass.getPackage().name

val Server.mainWorld get() = worlds[0]!!

fun WorldCreator.create() = server.createWorld(this)