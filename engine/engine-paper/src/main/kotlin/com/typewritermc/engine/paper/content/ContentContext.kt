package com.typewritermc.engine.paper.content

import kotlin.properties.ReadOnlyProperty
import kotlin.reflect.KClass
import kotlin.reflect.KProperty
import kotlin.reflect.full.safeCast

data class ContentContext(
    val data: Map<String, Any> = emptyMap()
)

class ContentContextPropertyDelegate<T : Any>(
    private val key: String,
    private val klass: KClass<T>,
): ReadOnlyProperty<ContentContext, T?> {
    override fun getValue(thisRef: ContentContext, property: KProperty<*>): T? {
        return klass.safeCast(thisRef.data[key])
    }
}

inline fun <reified T : Any> property(
    key: String,
): ReadOnlyProperty<ContentContext, T?> {
    return ContentContextPropertyDelegate(key, T::class)
}


class MappingContentContextPropertyDelegate<T : Any, V: Any>(
    private val key: String,
    private val klass: KClass<T>,
    private val mapper: (T) -> V,
): ReadOnlyProperty<ContentContext, V?> {
    override fun getValue(thisRef: ContentContext, property: KProperty<*>): V? {
        val t = klass.safeCast(thisRef.data[key]) ?: return null
        return mapper(t)
    }
}

inline fun <reified T : Any, reified V : Any> mappedProperty(
    key: String,
    noinline mapper: (T) -> V,
): ReadOnlyProperty<ContentContext, V?> {
    return MappingContentContextPropertyDelegate(key, T::class, mapper)
}

val ContentContext.pageId by property<String>("pageId")
val ContentContext.entryId by property<String>("entryId")
val ContentContext.fieldPath by property<String>("fieldPath")
val ContentContext.fieldValue by property<String>("fieldValue")

// Segment properties
val ContentContext.startFrame by mappedProperty<Double, Int>("startFrame") { it.toInt() }
val ContentContext.endFrame by mappedProperty<Double, Int>("endFrame") { it.toInt() }