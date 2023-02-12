package me.gabber235.typewriter.utils

import org.bukkit.Bukkit
import org.bukkit.command.CommandMap
import java.lang.reflect.Field

val commandMap: CommandMap
    get() {
        var commandMap: CommandMap? = null
    
        try {
            val f: Field = Bukkit.getServer().javaClass.getDeclaredField("commandMap")
            f.isAccessible = true
            commandMap = f.get(Bukkit.getServer()) as CommandMap
        } catch (e: NoSuchFieldException) {
            e.printStackTrace()
        } catch (e: IllegalAccessException) {
            e.printStackTrace()
        }
    
        return commandMap?: throw IllegalStateException("Could not get command map")
    }
