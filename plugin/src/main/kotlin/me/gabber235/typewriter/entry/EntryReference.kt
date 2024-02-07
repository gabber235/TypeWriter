package me.gabber235.typewriter.entry

import kotlin.reflect.KClass

inline fun <reified E : Entry> emptyRef() = Ref("", E::class)

/**
 * A reference to an entry. This is used to reference an entry from another entry.
 *
 * @param E The type of the entry that this reference points to.
 */
class Ref<E : Entry>(
    val id: String,
    private val klass: KClass<E>
) {
    val isSet: Boolean
        get() = id.isNotBlank()

    fun get(): E? = Query.findById(klass, id)

    override fun toString(): String {
        return "Ref<${klass.simpleName}>($id)"
    }
}

fun <E : Entry> List<Ref<E>>.get(): List<E> = mapNotNull { it.get() }