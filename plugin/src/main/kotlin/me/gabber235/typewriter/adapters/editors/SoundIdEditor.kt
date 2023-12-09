package me.gabber235.typewriter.adapters.editors

import com.google.gson.JsonObject
import me.gabber235.typewriter.adapters.CustomEditor
import me.gabber235.typewriter.adapters.ObjectEditor
import me.gabber235.typewriter.utils.DefaultSoundId
import me.gabber235.typewriter.utils.EntrySoundId
import me.gabber235.typewriter.utils.SoundId

@CustomEditor(SoundId::class)
fun ObjectEditor<SoundId>.soundId() = reference {
    default {
        val obj = JsonObject()

        obj.addProperty("type", "default")
        obj.addProperty("value", "")

        obj
    }

    jsonDeserialize { jsonElement, _, _ ->
        val obj = jsonElement.asJsonObject
        val type = obj.get("type").asString
        val value = obj.get("value").asString

        when (type) {
            "default" -> DefaultSoundId(value)
            "entry" -> EntrySoundId(value)
            else -> throw IllegalArgumentException("Invalid sound id type: $type")
        }
    }

    jsonSerialize { soundId, _, _ ->
        val obj = JsonObject()

        when (soundId) {
            is DefaultSoundId -> {
                obj.addProperty("type", "default")
                obj.addProperty("value", soundId.namespacedKey.toString())
            }

            is EntrySoundId -> {
                obj.addProperty("type", "entry")
                obj.addProperty("value", soundId.entryId)
            }
        }

        obj
    }
}