package me.gabber235.typewriter.adapters.editors

import com.google.gson.JsonObject
import me.gabber235.typewriter.adapters.CustomEditor
import me.gabber235.typewriter.adapters.ObjectEditor

@CustomEditor(ClosedFloatingPointRange::class)
fun ObjectEditor<ClosedFloatingPointRange<Float>>.floatRange() = reference {
    default {
        val obj = JsonObject()
        obj.addProperty("start", 0f)
        obj.addProperty("end", 0f)

        obj
    }

    jsonDeserialize { element, _, _ ->
        val obj = element.asJsonObject
        val start = obj.get("start").asFloat
        val end = obj.get("end").asFloat
        start..end
    }

    jsonSerialize { src, _, _ ->
        val obj = JsonObject()
        obj.addProperty("start", src.start)
        obj.addProperty("end", src.endInclusive)

        obj
    }
}