package com.typewritermc.engine.paper.loader.serializers

import com.google.gson.JsonDeserializationContext
import com.google.gson.JsonElement
import com.google.gson.JsonObject
import com.google.gson.JsonSerializationContext
import com.google.gson.reflect.TypeToken
import com.typewritermc.loader.DataSerializer
import java.lang.reflect.Type

class FloatRangeSerializer : DataSerializer<ClosedFloatingPointRange<Float>> {
    override val type: Type = object : TypeToken<ClosedFloatingPointRange<Float>>() {}.type

    override fun serialize(
        src: ClosedFloatingPointRange<Float>?,
        typeOfSrc: Type?,
        context: JsonSerializationContext?
    ): JsonElement {
        val obj = JsonObject()
        obj.addProperty("start", src?.start ?: 0f)
        obj.addProperty("end", src?.endInclusive ?: 0f)
        return obj
    }

    override fun deserialize(
        json: JsonElement?,
        typeOfT: Type?,
        context: JsonDeserializationContext?
    ): ClosedFloatingPointRange<Float> {
        val obj = json?.asJsonObject ?: JsonObject()
        val start = obj.get("start")?.asFloat ?: 0f
        val end = obj.get("end")?.asFloat ?: 0f
        return start..end
    }
}