package com.typewritermc.engine.paper.loader.serializers

import com.google.gson.*
import com.typewritermc.core.utils.point.Position
import com.typewritermc.engine.paper.utils.EmitterSoundSource
import com.typewritermc.engine.paper.utils.LocationSoundSource
import com.typewritermc.engine.paper.utils.SelfSoundSource
import com.typewritermc.engine.paper.utils.SoundSource
import com.typewritermc.core.serialization.DataSerializer
import java.lang.reflect.Type

class SoundSourceSerializer : DataSerializer<SoundSource> {
    override val type: Type = SoundSource::class.java

    override fun serialize(src: SoundSource?, typeOfSrc: Type?, context: JsonSerializationContext?): JsonElement {
        val obj = JsonObject()
        when (src) {
            is SelfSoundSource -> {
                obj.addProperty("type", "self")
            }

            is EmitterSoundSource -> {
                obj.addProperty("type", "emitter")
                obj.addProperty("entryId", src.entryId)
            }

            is LocationSoundSource -> {
                obj.addProperty("type", "location")
                obj.add("location", context?.serialize(src.position))
            }

            null -> {
                obj.addProperty("type", "self")
            }
        }
        return obj
    }

    override fun deserialize(json: JsonElement?, typeOfT: Type?, context: JsonDeserializationContext?): SoundSource {
        val obj = json?.asJsonObject ?: JsonObject()
        val type = obj.get("type")?.asString ?: "self"

        return when (type) {
            "self" -> SelfSoundSource
            "emitter" -> {
                val value = obj.get("entryId")?.asString ?: ""
                EmitterSoundSource(value)
            }

            "location" -> {
                val position: Position = context?.deserialize(obj.get("location"), Position::class.java)
                    ?: throw JsonParseException("Invalid location for LocationSoundSource")
                LocationSoundSource(position)
            }

            else -> throw JsonParseException("Invalid sound source type: $type")
        }
    }
}