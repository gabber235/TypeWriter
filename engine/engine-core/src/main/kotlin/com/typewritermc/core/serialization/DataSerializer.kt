package com.typewritermc.core.serialization

import com.google.gson.*
import com.google.gson.internal.Streams
import com.google.gson.reflect.TypeToken
import com.google.gson.stream.JsonReader
import com.google.gson.stream.JsonWriter
import com.typewritermc.core.utils.RuntimeTypeAdapterFactory
import com.typewritermc.loader.DependencyInjectionClassInfo
import com.typewritermc.loader.DependencyInjectionInfo
import com.typewritermc.loader.DependencyInjectionMethodInfo
import java.lang.reflect.Type

interface DataSerializer<T : Any> : JsonSerializer<T>, JsonDeserializer<T> {
    val type: Type
}

fun createDataSerializerGson(serializers: List<DataSerializer<*>>): Gson {
    var builder = GsonBuilder()
        .serializeNulls()
        .registerTypeAdapterFactory(AlgebraicSerializationFactory())
        .registerTypeAdapterFactory(
            RuntimeTypeAdapterFactory.of(DependencyInjectionInfo::class.java, "kind")
                .registerSubtype(DependencyInjectionClassInfo::class.java, "class")
                .registerSubtype(DependencyInjectionMethodInfo::class.java, "method")
        )

    serializers.forEach {
        val typeToken = TypeToken.get(it.type)
        val matchRawType = typeToken.type == typeToken.rawType
        builder = builder.registerTypeAdapterFactory(TypeAdapterFactoryDataSerializer(it, typeToken, matchRawType))
    }

    return builder
        .create()
}

/**
 * Thank you GSON for not allowing JSON serialization of null values :|
 * Now I have to write all of this boilerplate myself...
 */
private class TypeAdapterFactoryDataSerializer<T : Any>(
    private val serializer: DataSerializer<T>,
    private val typeToken: TypeToken<*>,
    private val matchRawType: Boolean = false,
) : TypeAdapterFactory {
    override fun <R : Any> create(gson: Gson, type: TypeToken<R>): TypeAdapter<R>? {
        val matches = typeToken == type || matchRawType && typeToken.type === type.rawType
        if (!matches) return null
        val type = type as TypeToken<T>
        return TypeAdapterDataSerializer(serializer, gson, type) as TypeAdapter<R>?
    }
}

private class TypeAdapterDataSerializer<T : Any>(
    private val serializer: DataSerializer<T>,
    private val gson: Gson,
    private val typeToken: TypeToken<T>,
) : TypeAdapter<T>() {
    override fun write(out: JsonWriter, value: T?) {
        val tree = serializer.serialize(value, typeToken.type, GsonContextImpl(gson))
        Streams.write(tree, out)
    }

    override fun read(`in`: JsonReader?): T {
        val element = Streams.parse(`in`)
        return serializer.deserialize(element, typeToken.type, GsonContextImpl(gson))
    }
}

private class GsonContextImpl(
    private val gson: Gson,
) : JsonSerializationContext, JsonDeserializationContext {
    override fun serialize(src: Any): JsonElement {
        return gson.toJsonTree(src)
    }

    override fun serialize(src: Any, typeOfSrc: Type): JsonElement {
        return gson.toJsonTree(src, typeOfSrc)
    }

    @Throws(JsonParseException::class)
    override fun <R> deserialize(json: JsonElement, typeOfT: Type): R {
        return gson.fromJson<R>(json, typeOfT)
    }
}
