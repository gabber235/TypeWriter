package com.typewritermc.core.utils.point

import com.google.gson.Gson
import com.google.gson.JsonElement
import com.google.gson.JsonObject
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject
import org.koin.core.qualifier.named
import kotlin.reflect.KClass

class Generic(
    val data: JsonElement,
) : KoinComponent {
    companion object {
        val Empty = Generic(JsonObject())
    }

    private val gson: Gson by inject(named("dataSerializer"))

    fun <T> get(klass: Class<T>): T? {
        return gson.fromJson(data, klass)
    }

    fun <T : Any> get(klass: KClass<T>): T? {
        return gson.fromJson(data, klass.java)
    }
}

inline fun <reified T> Generic.get(): T? {
    return get(T::class.java)
}
