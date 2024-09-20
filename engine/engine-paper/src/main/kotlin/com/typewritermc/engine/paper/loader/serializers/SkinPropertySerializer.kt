package com.typewritermc.engine.paper.loader.serializers

import com.google.gson.JsonDeserializationContext
import com.google.gson.JsonElement
import com.google.gson.JsonObject
import com.google.gson.JsonSerializationContext
import com.typewritermc.engine.paper.entry.entity.SkinProperty
import com.typewritermc.core.serialization.DataSerializer
import java.lang.reflect.Type

class SkinPropertySerializer : DataSerializer<SkinProperty> {
    override val type: Type = SkinProperty::class.java

    override fun serialize(src: SkinProperty?, typeOfSrc: Type?, context: JsonSerializationContext?): JsonElement {
        val obj = JsonObject()
        obj.addProperty("texture", src?.texture ?: "")
        obj.addProperty("signature", src?.signature ?: "")
        return obj
    }

    override fun deserialize(json: JsonElement?, typeOfT: Type?, context: JsonDeserializationContext?): SkinProperty {
        val obj = json?.asJsonObject ?: JsonObject()
        return if (obj.has("texture") && obj.has("signature")) {
            SkinProperty(obj.get("texture").asString, obj.get("signature").asString)
        } else {
            SkinProperty()
        }
    }
}