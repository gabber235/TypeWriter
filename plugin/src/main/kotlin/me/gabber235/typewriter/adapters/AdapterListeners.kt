package me.gabber235.typewriter.adapters

import me.gabber235.typewriter.entry.*
import me.gabber235.typewriter.entry.Entry
import org.bukkit.event.Event
import org.bukkit.event.EventPriority
import java.lang.reflect.*
import java.lang.reflect.Modifier
import kotlin.reflect.KClass

data class AdapterListener(
    val method: Method,
    val entry: KClass<out Entry>,
    val generators: List<ParameterGenerator>,
    val priority: EventPriority,
    val ignoreCancelled: Boolean,
)

sealed interface ParameterGenerator {
    fun isApplicable(parameter: Parameter): Boolean
    fun generate(event: Event, adapterListener: AdapterListener): Any

    object EventParameterGenerator : ParameterGenerator {
        override fun isApplicable(parameter: Parameter): Boolean {
            // It and all superclasses must be Event
            return Event::class.java.isAssignableFrom(parameter.type)
        }

        override fun generate(event: Event, adapterListener: AdapterListener): Any = event
    }

    object QueryParameterGenerator : ParameterGenerator {
        override fun isApplicable(parameter: Parameter): Boolean {
            // It can only be Query
            return parameter.type.isAssignableFrom(Query::class.java)
        }

        override fun generate(event: Event, adapterListener: AdapterListener): Any = Query(adapterListener.entry)
    }

    companion object {
        private val generators = listOf(EventParameterGenerator, QueryParameterGenerator)

        private fun getGenerator(parameter: Parameter): ParameterGenerator? {
            return generators.firstOrNull { it.isApplicable(parameter) }
        }

        /**
         * Creates a list of ParameterGenerators for the given method.
         * @throws IllegalArgumentException if the parameter is not applicable to any generator
         */
        @Throws(IllegalArgumentException::class)
        fun getGenerators(parameters: Array<Parameter>): List<ParameterGenerator> {
            return parameters.map { parameter ->
                getGenerator(parameter)
                    ?: throw IllegalArgumentException("There is no way to create a parameter for ${parameter.name} (${parameter.type}) in ${parameter.declaringExecutable}")
            }
        }
    }
}


object AdapterListeners {

    private fun constructAdapterListener(method: Method): AdapterListener {
        val annotation = method.getAnnotation(EntryListener::class.java)
        val entry = annotation.entry
        val parameters = method.parameters
        val generators = ParameterGenerator.getGenerators(parameters)
        return AdapterListener(method, entry, generators, annotation.priority, annotation.ignoreCancelled)
    }

    fun constructAdapterListeners(classes: List<Class<*>>): List<AdapterListener> {
        // Go through all classes and find methods with the @EntryListener annotation
        // Then construct an AdapterListener for each of them
        return classes.asSequence()
            .flatMap { it.methods.asSequence() }
            .filter { Modifier.isStatic(it.modifiers) }
            .filter { it.isAnnotationPresent(EntryListener::class.java) }
            .map(::constructAdapterListener)
            .toList()
    }
}