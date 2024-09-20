package com.typewritermc.engine.paper.loader.serializers

import com.google.gson.JsonDeserializationContext
import com.google.gson.JsonElement
import com.google.gson.JsonPrimitive
import com.google.gson.JsonSerializationContext
import com.typewritermc.core.utils.point.World
import com.typewritermc.core.serialization.DataSerializer
import lirand.api.extensions.server.server
import java.lang.reflect.Type

class WorldSerializer : DataSerializer<World> {
    override val type: Type = World::class.java

    override fun serialize(src: World?, typeOfSrc: Type?, context: JsonSerializationContext?): JsonElement {
        return JsonPrimitive(src?.identifier ?: "")
    }

    override fun deserialize(json: JsonElement?, typeOfT: Type?, context: JsonDeserializationContext?): World {
        val world = json?.asString ?: ""

        val bukkitWorld = server.getWorld(world)
            ?: server.worlds.firstOrNull { it.name.equals(world, true) }
            ?: server.worlds.firstOrNull()
            ?: throw IllegalArgumentException("Could not find world '$world' for location, and no default world available.")

        return World(bukkitWorld.uid.toString())
    }
}