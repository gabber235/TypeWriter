package com.typewritermc.core.extension.annotations

@Target(AnnotationTarget.CLASS)
@Retention(AnnotationRetention.RUNTIME)
annotation class AlgebraicTypeInfo(val name: String, val color: String, val icon: String)
