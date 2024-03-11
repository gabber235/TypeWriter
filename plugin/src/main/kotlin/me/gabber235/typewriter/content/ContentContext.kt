package me.gabber235.typewriter.content

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

val ContentContext.pageId by property<String>("pageId")
val ContentContext.entryId by property<String>("entryId")
val ContentContext.fieldPath by property<String>("fieldPath")