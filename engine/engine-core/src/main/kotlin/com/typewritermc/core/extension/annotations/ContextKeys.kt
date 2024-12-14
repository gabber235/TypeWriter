package com.typewritermc.core.extension.annotations

import com.typewritermc.core.interaction.EntryContextKey
import kotlin.reflect.KClass

@Target(AnnotationTarget.CLASS)
@Retention(AnnotationRetention.RUNTIME)
annotation class ContextKeys(val klass: KClass<out EntryContextKey>)

// Enum value
@Target(AnnotationTarget.FIELD)
@Retention(AnnotationRetention.RUNTIME)
annotation class KeyType(val type: KClass<*>)

@Target(AnnotationTarget.CLASS)
@Retention(AnnotationRetention.RUNTIME)
annotation class GlobalKey(val klass: KClass<*>)