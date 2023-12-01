package me.gabber235.typewriter.adapters.editors

import com.google.gson.JsonObject
import me.gabber235.typewriter.adapters.CustomEditor
import me.gabber235.typewriter.adapters.ObjectEditor
import me.gabber235.typewriter.utils.EmitterSoundSource
import me.gabber235.typewriter.utils.LocationSoundSource
import me.gabber235.typewriter.utils.SelfSoundSource
import me.gabber235.typewriter.utils.SoundSource
import org.bukkit.Location

@CustomEditor(SoundSource::class)
fun ObjectEditor<SoundSource>.soundSource() = reference {
    default {
        JsonObject().apply {
            addProperty("type", "self")
            addProperty("entryId", "")
            add("location", JsonObject().apply {
                addProperty("world", "")
                addProperty("x", 0.0)
                addProperty("y", 0.0)
                addProperty("z", 0.0)
                addProperty("yaw", 0.0)
                addProperty("pitch", 0.0)
            })
        }
    }

    jsonDeserialize { jsonElement, _, context ->
        val obj = jsonElement.asJsonObject
        val type = obj.get("type").asString

        when (type) {
            "self" -> SelfSoundSource
            "emitter" -> {
                val value = obj.get("entryId").asString
                EmitterSoundSource(value)
            }

            "location" -> {
                val location: Location = context.deserialize(obj.get("location"), Location::class.java)
                LocationSoundSource(location)
            }

            else -> throw IllegalArgumentException("Invalid sound source type: $type")
        }
    }

    jsonSerialize { soundSource, _, context ->
        val obj = JsonObject()

        when (soundSource) {
            is SelfSoundSource -> {
                obj.addProperty("type", "self")
            }

            is EmitterSoundSource -> {
                obj.addProperty("type", "emitter")
                obj.addProperty("entryId", soundSource.entryId)
            }

            is LocationSoundSource -> {
                obj.addProperty("type", "location")
                obj.add("location", context.serialize(soundSource.location))
            }
        }

        obj
    }
}