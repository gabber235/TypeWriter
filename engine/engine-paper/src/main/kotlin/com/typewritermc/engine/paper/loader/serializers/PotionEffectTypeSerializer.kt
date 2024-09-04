package com.typewritermc.engine.paper.loader.serializers

import com.google.gson.JsonDeserializationContext
import com.google.gson.JsonElement
import com.google.gson.JsonPrimitive
import com.google.gson.JsonSerializationContext
import com.typewritermc.loader.DataSerializer
import org.bukkit.potion.PotionEffectType
import java.lang.reflect.Type

class PotionEffectTypeSerializer : DataSerializer<PotionEffectType> {
    override val type: Type = PotionEffectType::class.java

    override fun serialize(src: PotionEffectType?, typeOfSrc: Type?, context: JsonSerializationContext?): JsonElement {
        return JsonPrimitive(src?.name ?: "speed")
    }

    override fun deserialize(
        json: JsonElement?,
        typeOfT: Type?,
        context: JsonDeserializationContext?
    ): PotionEffectType {
        return PotionEffectType.getByName(json?.asString ?: "speed") ?: PotionEffectType.SPEED
    }
}