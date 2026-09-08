package com.typewritermc.presentation

import com.typewritermc.types.TypeExpression
import com.typewritermc.types.TypePrototypeRegistry
import kotlinx.serialization.SerializationStrategy
import kotlinx.serialization.serializer

/**
 * A literal choice whose value type must match its selector. Catalog compilation owns serialization.
 * Use [selectOption] to capture the value serializer without exposing protocol objects to presentation authors.
 */
class PresentationOption<V : Any>
    @PublishedApi
    internal constructor(
        val id: String,
        val label: String,
        val value: V,
        private val serializer: SerializationStrategy<V>,
    ) {
        internal fun encode(
            prototypes: TypePrototypeRegistry,
            type: TypeExpression,
        ) = prototypes.dataFormat.encodeToDataValue(serializer, value, type)
    }

/** Creates a literal choice with a stable identity and a display label independent of its value. */
inline fun <reified V : Any> selectOption(
    id: String,
    label: String,
    value: V,
): PresentationOption<V> = PresentationOption(id, label, value, serializer<V>())
