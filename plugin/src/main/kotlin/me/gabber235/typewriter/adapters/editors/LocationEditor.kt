package me.gabber235.typewriter.adapters.editors

import com.google.gson.JsonObject
import lirand.api.extensions.server.server
import me.gabber235.typewriter.adapters.CustomEditor
import me.gabber235.typewriter.adapters.ObjectEditor
import me.gabber235.typewriter.utils.logErrorIfNull
import org.bukkit.Location

@CustomEditor(Location::class)
fun ObjectEditor<Location>.location() = reference {
	default {
		val obj = JsonObject()
		obj.addProperty("world", "")
		obj.addProperty("x", 0.0)
		obj.addProperty("y", 0.0)
		obj.addProperty("z", 0.0)
		obj.addProperty("yaw", 0.0)
		obj.addProperty("pitch", 0.0)

		obj
	}

	jsonDeserialize { element, _, _ ->
		val obj = element.asJsonObject
		val world = obj.getAsJsonPrimitive("world").asString
		val x = obj.getAsJsonPrimitive("x").asDouble
		val y = obj.getAsJsonPrimitive("y").asDouble
		val z = obj.getAsJsonPrimitive("z").asDouble
		val yaw = obj.getAsJsonPrimitive("yaw")?.asFloat ?: 0f
		val pitch = obj.getAsJsonPrimitive("pitch")?.asFloat ?: 0f

		val bukkitWorld =
			server.getWorld(world).logErrorIfNull("Could not find world $world for location, so picking default world")
				?: server.worlds.firstOrNull()
		Location(bukkitWorld, x, y, z, yaw, pitch)
	}

	jsonSerialize { src, _, _ ->
		val obj = JsonObject()
		obj.addProperty("world", src.world.name)
		obj.addProperty("x", src.x)
		obj.addProperty("y", src.y)
		obj.addProperty("z", src.z)
		obj.addProperty("yaw", src.yaw)
		obj.addProperty("pitch", src.pitch)

		obj
	}
}