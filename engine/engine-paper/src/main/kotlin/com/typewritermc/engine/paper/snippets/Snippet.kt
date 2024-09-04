package com.typewritermc.engine.paper.snippets

import org.koin.core.component.KoinComponent
import org.koin.core.component.inject
import kotlin.properties.ReadOnlyProperty
import kotlin.reflect.KClass
import kotlin.reflect.KProperty


inline fun <reified T : Any> snippet(path: String, defaultValue: T, comment: String = ""): ReadOnlyProperty<Nothing?, T> {
    return snippet(path, T::class, defaultValue, comment)
}

fun <T : Any> snippet(path: String, klass: KClass<T>, defaultValue: T, comment: String = ""): ReadOnlyProperty<Nothing?, T> {
    return Snippet(path, klass, defaultValue, comment)
}

class Snippet<T : Any>(private val path: String, private val klass: KClass<T>, private val defaultValue: T, private val comment: String) :
    ReadOnlyProperty<Nothing?, T>, KoinComponent {
    private val snippetDatabase: SnippetDatabase by inject()

    init {
        snippetDatabase.registerSnippet(path, defaultValue, comment)
    }

    override fun getValue(thisRef: Nothing?, property: KProperty<*>): T {
        return snippetDatabase.getSnippet(path, klass, defaultValue)
    }
}