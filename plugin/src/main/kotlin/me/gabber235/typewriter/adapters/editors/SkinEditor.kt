package me.gabber235.typewriter.adapters.editors

import com.google.gson.JsonObject
import me.gabber235.typewriter.adapters.CustomEditor
import me.gabber235.typewriter.adapters.ObjectEditor
import me.gabber235.typewriter.entry.entity.SkinProperty

@CustomEditor(SkinProperty::class)
fun ObjectEditor<SkinProperty>.skin() = reference {
    default {
        val obj = JsonObject()
        obj.addProperty("texture", "")
        obj.addProperty("signature", "")

        obj
    }

    jsonDeserialize { element, _, _ ->
        val obj = element.asJsonObject
        if (obj.has("texture") && obj.has("signature")) {
            SkinProperty(obj.get("texture").asString, obj.get("signature").asString)
        } else {
            SkinProperty()
        }
    }

    jsonSerialize { src, _, _ ->
        val obj = JsonObject()
        obj.addProperty("texture", src.texture)
        obj.addProperty("signature", src.signature)

        obj
    }
}