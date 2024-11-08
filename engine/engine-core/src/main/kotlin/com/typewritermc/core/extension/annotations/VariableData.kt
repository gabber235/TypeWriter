package com.typewritermc.core.extension.annotations

import kotlin.reflect.KClass

@Target(AnnotationTarget.CLASS)
annotation class VariableData(val type: KClass<*>)