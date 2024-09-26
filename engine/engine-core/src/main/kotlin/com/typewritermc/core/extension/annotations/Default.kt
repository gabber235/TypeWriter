package com.typewritermc.core.extension.annotations

import org.intellij.lang.annotations.Language

@Target(AnnotationTarget.FIELD, AnnotationTarget.PROPERTY)
@Retention(AnnotationRetention.RUNTIME)
/**
 * This allows you to specify a default value for the field.
 * The default value must be a valid JSON string.
 */
annotation class Default(
    @Language("JSON")
    val json: String
)
