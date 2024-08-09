package me.gabber235.typewriter.adapters.editors

import com.google.gson.JsonObject
import me.gabber235.typewriter.adapters.CustomEditor
import me.gabber235.typewriter.adapters.ObjectEditor
import me.gabber235.typewriter.utils.Vector

@CustomEditor(Vector::class)
fun ObjectEditor<Vector>.vector() = reference {
    default {
        val obj = JsonObject()
        obj.addProperty("x", 0.0)
        obj.addProperty("y", 0.0)
        obj.addProperty("z", 0.0)

        obj
    }

    jsonDeserialize { element, _, _ ->
        val obj = element.asJsonObject
        val x = obj.getAsJsonPrimitive("x").asDouble
        val y = obj.getAsJsonPrimitive("y").asDouble
        val z = obj.getAsJsonPrimitive("z").asDouble

        Vector(x, y, z)
    }

    jsonSerialize { src, _, _ ->
        val obj = JsonObject()
        obj.addProperty("x", src.x)
        obj.addProperty("y", src.y)
        obj.addProperty("z", src.z)

        obj
    }
}