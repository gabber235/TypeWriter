package me.gabber235.typewriter.adapters.editors

import com.google.common.reflect.TypeToken
import com.google.gson.JsonPrimitive
import me.gabber235.typewriter.adapters.CustomEditor
import me.gabber235.typewriter.adapters.FieldModifier
import me.gabber235.typewriter.adapters.ObjectEditor
import me.gabber235.typewriter.adapters.Tags
import me.gabber235.typewriter.entry.Entry
import me.gabber235.typewriter.entry.Ref
import java.lang.reflect.ParameterizedType
import kotlin.reflect.KClass

@CustomEditor(Ref::class)
fun ObjectEditor<Ref<*>>.entryReference() = reference {
    default {
        JsonPrimitive("")
    }

    jsonDeserialize { element, type, _ ->
        val subType = (type as ParameterizedType).actualTypeArguments[0]
        val clazz = TypeToken.of(subType).rawType
        val klass = clazz.kotlin

        if (element.isJsonNull) {
            return@jsonDeserialize Ref("", klass as KClass<Entry>)
        }
        val id = element.asString

        Ref(id, klass as KClass<Entry>)
    }

    jsonSerialize { src, _, _ ->
        JsonPrimitive(src.id)
    }

    modifier { token, _ ->
        val subType = (token.type as ParameterizedType).actualTypeArguments[0]
        val clazz = TypeToken.of(subType).rawType
        val klass = clazz.kotlin
        val tag = klass.annotations.find { it is Tags }?.let { (it as Tags).tags.firstOrNull() }
        if (tag == null) {
            throw IllegalArgumentException("Entry ${klass.simpleName} does not have a tag. It is needed for the StaticEntryIdentifier")
        }

        FieldModifier.DynamicModifier("entry", tag)
    }
}