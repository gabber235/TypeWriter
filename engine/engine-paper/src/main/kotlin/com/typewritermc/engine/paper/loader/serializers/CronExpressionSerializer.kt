package com.typewritermc.engine.paper.loader.serializers

import com.google.gson.JsonDeserializationContext
import com.google.gson.JsonElement
import com.google.gson.JsonPrimitive
import com.google.gson.JsonSerializationContext
import com.typewritermc.engine.paper.utils.CronExpression
import com.typewritermc.core.serialization.DataSerializer
import java.lang.reflect.Type

class CronExpressionSerializer : DataSerializer<CronExpression> {
    override val type: Type = CronExpression::class.java

    override fun serialize(src: CronExpression?, typeOfSrc: Type?, context: JsonSerializationContext?): JsonElement {
        return JsonPrimitive(src?.expression ?: CronExpression.default().expression)
    }

    override fun deserialize(
        json: JsonElement?,
        typeOfT: Type?,
        context: JsonDeserializationContext?
    ): CronExpression {
        return CronExpression.createDynamic(json?.asString ?: CronExpression.default().expression)
    }
}