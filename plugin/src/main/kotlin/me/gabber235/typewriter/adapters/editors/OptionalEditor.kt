package me.gabber235.typewriter.adapters.editors

import com.google.gson.JsonNull
import com.google.gson.JsonObject
import com.google.gson.reflect.TypeToken
import me.gabber235.typewriter.adapters.*
import java.lang.reflect.ParameterizedType
import java.util.*


@CustomEditor(Optional::class)
fun ObjectEditor<Optional<*>>.optional() = reference {
    default {
        val obj = JsonObject()

        obj.addProperty("enabled", false)

        val default = generateFieldInfo(it)?.default() ?: JsonNull.INSTANCE
        obj.add("value", default)

        obj
    }

    jsonDeserialize { element, type, context ->
        val obj = element.asJsonObject
        val enabled = obj.get("enabled").asBoolean
        if (!enabled) return@jsonDeserialize Optional.empty<Any>()

        val valueElement = obj.get("value")

        val value: Any? = context.deserialize(valueElement, (type as ParameterizedType).actualTypeArguments[0])
        Optional.ofNullable(value)
    }

    jsonSerialize { src, _, context ->
        val obj = JsonObject()

        obj.addProperty("enabled", src.isPresent)

        if (src.isPresent) {
            val value = src.get()
            val valueElement = context.serialize(value)
            obj.add("value", valueElement)
        }

        obj
    }

    fieldInfo {
        val type = it.type as ParameterizedType
        val actualType = type.actualTypeArguments[0]
        FieldInfo.fromTypeToken(TypeToken.get(actualType))
    }


}