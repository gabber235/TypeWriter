package com.typewritermc.core.extension.annotations

import kotlin.reflect.KClass

@Target(AnnotationTarget.CLASS)
@Repeatable
annotation class GenericConstraint(val type: KClass<*>)
