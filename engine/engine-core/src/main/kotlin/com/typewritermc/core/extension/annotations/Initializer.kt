package com.typewritermc.core.extension.annotations

@Target(AnnotationTarget.CLASS)
@Deprecated("Use the @Singleton annotation instead", replaceWith = ReplaceWith("Singleton"), level = DeprecationLevel.ERROR)
annotation class Initializer()