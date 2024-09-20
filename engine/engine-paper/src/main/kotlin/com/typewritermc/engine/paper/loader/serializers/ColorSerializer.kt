package com.typewritermc.engine.paper.loader.serializers

import com.google.gson.JsonDeserializationContext
import com.google.gson.JsonElement
import com.google.gson.JsonPrimitive
import com.google.gson.JsonSerializationContext
import com.typewritermc.engine.paper.utils.Color
import com.typewritermc.core.serialization.DataSerializer
import java.lang.reflect.Type

class ColorSerializer : DataSerializer<Color> {
    override val type: Type = Color::class.java

    override fun serialize(src: Color?, typeOfSrc: Type?, context: JsonSerializationContext?): JsonElement {
        return JsonPrimitive(src?.color ?: 0)
    }

    override fun deserialize(json: JsonElement?, typeOfT: Type?, context: JsonDeserializationContext?): Color {
        return Color(json?.asInt ?: 0)
    }
}