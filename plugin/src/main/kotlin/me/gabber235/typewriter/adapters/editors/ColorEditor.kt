package me.gabber235.typewriter.adapters.editors

import com.google.gson.JsonPrimitive
import me.gabber235.typewriter.adapters.CustomEditor
import me.gabber235.typewriter.adapters.ObjectEditor
import me.gabber235.typewriter.utils.Color

@CustomEditor(Color::class)
fun ObjectEditor<Color>.color() = reference {
    default {
        JsonPrimitive(0)
    }

    jsonDeserialize { element, _, _ ->
        Color(element.asInt)
    }

    jsonSerialize { src, _, _ ->
        JsonPrimitive(src.color)
    }
}
