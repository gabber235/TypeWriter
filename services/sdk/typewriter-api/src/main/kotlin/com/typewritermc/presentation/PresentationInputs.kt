package com.typewritermc.presentation

import kotlin.reflect.KClass
import kotlin.reflect.KProperty1

/**
 * Identifies a value supplied to one presentation declaration.
 *
 * Inputs are local to their declaration. The panel binds and edits them, while the caller owns persistence and
 * decides whether an editable value is eventually committed.
 */
class PresentationInputRef<T : Any> internal constructor(
    val name: String,
    val type: KClass<T>,
    val editable: Boolean,
    internal val index: Long,
    private val context: PresentationBuildContext,
) {
    /** Returns a binding to the complete input value. */
    fun value(): PresentationValue<T> = PresentationValue(this, emptyList(), type, context)

    /** Returns a binding to a serialized property nested within this input. */
    inline fun <reified V : Any> field(property: KProperty1<T, V>): PresentationValue<V> = value().field(property)

    /** Maps [value] into this input when a presentation is included by another presentation. */
    infix fun receives(value: PresentationValue<T>): PresentationArgumentRef = PresentationArgumentRef(this, value)
}

/**
 * A typed binding into a declaration input.
 *
 * Property selection retains the originating input identity and records serialized field names for protocol
 * compilation. It describes data flow only, not storage or mutation.
 */
class PresentationValue<T : Any>
    @PublishedApi
    internal constructor(
        @PublishedApi internal val input: PresentationInputRef<*>,
        @PublishedApi internal val fields: List<String>,
        val type: KClass<T>,
        @PublishedApi internal val context: PresentationBuildContext,
    ) {
        /** Selects a serialized property while preserving this binding's input identity. */
        inline fun <reified V : Any> field(property: KProperty1<T, V>): PresentationValue<V> {
            val field = context.field(type, property.name)
            return PresentationValue(input, fields + field.serializedName, V::class, context)
        }

        internal fun reference(): FieldReference = FieldReference(fields.lastOrNull().orEmpty(), input, fields.dropLast(1))
    }

/** Connects an including presentation's input to an included presentation's input. */
class PresentationArgumentRef internal constructor(
    internal val input: PresentationInputRef<*>,
    internal val value: PresentationValue<*>,
)

/** Builds a composed presentation with no implicit primary input. */
@JvmName("composedPresentation")
context(context: PresentationBuildContext)
fun presentation(
    name: String,
    block: PresentationBuilder<Unit>.() -> Unit,
): PresentationSpec<Unit> = PresentationBuilder(Unit::class, context).apply(block).build(name)
