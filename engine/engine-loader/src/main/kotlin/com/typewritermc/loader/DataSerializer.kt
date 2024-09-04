package com.typewritermc.loader

import com.google.gson.Gson
import com.google.gson.GsonBuilder
import com.google.gson.JsonDeserializer
import com.google.gson.JsonSerializer
import java.lang.reflect.Type

interface DataSerializer<T : Any> : JsonSerializer<T>, JsonDeserializer<T> {
    val type: Type
}

fun createDataSerializerGson(serializers: List<DataSerializer<*>>): Gson {
    var builder = GsonBuilder()

    serializers.forEach {
        builder = builder.registerTypeAdapter(it.type, it)
    }

    return builder
        .create()
}
