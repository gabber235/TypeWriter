package com.typewritermc.engine.paper.loader.serializers

import com.google.gson.JsonDeserializationContext
import com.google.gson.JsonElement
import com.google.gson.JsonObject
import com.google.gson.JsonSerializationContext
import com.typewritermc.engine.paper.utils.DefaultSoundId
import com.typewritermc.engine.paper.utils.EntrySoundId
import com.typewritermc.engine.paper.utils.SoundId
import com.typewritermc.core.serialization.DataSerializer
import java.lang.reflect.Type

class SoundIdSerializer : DataSerializer<SoundId> {
    override val type: Type = SoundId::class.java

    override fun serialize(src: SoundId?, typeOfSrc: Type?, context: JsonSerializationContext?): JsonElement {
        val obj = JsonObject()
        when (src) {
            is DefaultSoundId -> {
                obj.addProperty("type", "default")
                obj.addProperty("value", src.namespacedKey.toString())
            }

            is EntrySoundId -> {
                obj.addProperty("type", "entry")
                obj.addProperty("value", src.entryId)
            }

            null -> {
                obj.addProperty("type", "default")
                obj.addProperty("value", "")
            }
        }
        return obj
    }

    override fun deserialize(json: JsonElement?, typeOfT: Type?, context: JsonDeserializationContext?): SoundId {
        val obj = json?.asJsonObject ?: JsonObject()
        val type = obj.get("type")?.asString ?: "default"
        val value = obj.get("value")?.asString ?: ""

        return when (type) {
            "default" -> DefaultSoundId(value)
            "entry" -> EntrySoundId(value)
            else -> throw IllegalArgumentException("Invalid sound id type: $type")
        }
    }
}