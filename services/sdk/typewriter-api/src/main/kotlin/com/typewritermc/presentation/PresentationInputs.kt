package com.typewritermc.presentation

import kotlin.reflect.KClass
import kotlin.reflect.KProperty1

/** A declaration local input. Callers supply values; declarations never own persistence. */
class PresentationInputRef<T : Any> internal constructor(
    val name: String,
    val type: KClass<T>,
    val editable: Boolean,
    internal val index: Long,
    private val context: PresentationBuildContext,
) {
    fun value(): PresentationValue<T> = PresentationValue(this, emptyList(), type, context)

    inline fun <reified V : Any> field(property: KProperty1<T, V>): PresentationValue<V> = value().field(property)

    infix fun receives(value: PresentationValue<T>): PresentationArgumentRef = PresentationArgumentRef(this, value)
}

/** A typed path retains its input identity through nested property selection. */
class PresentationValue<T : Any>
    @PublishedApi
    internal constructor(
        @PublishedApi internal val input: PresentationInputRef<*>,
        @PublishedApi internal val fields: List<String>,
        val type: KClass<T>,
        @PublishedApi internal val context: PresentationBuildContext,
    ) {
        inline fun <reified V : Any> field(property: KProperty1<T, V>): PresentationValue<V> {
            val field = context.field(type, property.name)
            return PresentationValue(input, fields + field.serializedName, V::class, context)
        }

        internal fun reference(): FieldReference = FieldReference(fields.lastOrNull().orEmpty(), input, fields.dropLast(1))
    }

class PresentationArgumentRef internal constructor(
    internal val input: PresentationInputRef<*>,
    internal val value: PresentationValue<*>,
)

@JvmName("composedPresentation")
context(context: PresentationBuildContext)
fun presentation(
    name: String,
    block: PresentationBuilder<Unit>.() -> Unit,
): PresentationSpec<Unit> = PresentationBuilder(Unit::class, context).apply(block).build(name)
