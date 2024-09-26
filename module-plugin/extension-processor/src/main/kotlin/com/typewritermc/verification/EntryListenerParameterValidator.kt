package com.typewritermc.verification

import com.google.devtools.ksp.getAllSuperTypes
import com.google.devtools.ksp.processing.*
import com.google.devtools.ksp.symbol.KSAnnotated
import com.google.devtools.ksp.symbol.KSClassDeclaration
import com.google.devtools.ksp.symbol.KSFunctionDeclaration
import com.google.devtools.ksp.symbol.KSValueParameter
import com.typewritermc.core.entries.Query
import com.typewritermc.core.extension.annotations.EntryListener
import com.typewritermc.processors.format
import com.typewritermc.processors.fullName
import com.typewritermc.processors.whenClassNameIs

private val allowedTypes = listOf(
    "org.bukkit.event.Event",
    Query::class.qualifiedName!!,
)

class EntryListenerParameterValidator : SymbolProcessor {
    override fun process(resolver: Resolver): List<KSAnnotated> {
        val symbols = resolver.getSymbolsWithAnnotation(EntryListener::class.qualifiedName!!)
        val invalidListeners = symbols
            .filterIsInstance<KSFunctionDeclaration>()
            .mapNotNull { function ->
                val parameters = function.parameters
                    .map { Parameter(it) }

                if (parameters.none { parameter ->
                        parameter.isOrExtends("org.bukkit.event.Event")
                    }) return@mapNotNull "${function.location.format}: A valid Bukkit Event parameter is missing"

                val invalidParameters =
                    parameters.filter { parameter ->
                        allowedTypes.none { parameter.isOrExtends(it) }
                    }
                if (invalidParameters.isNotEmpty()) {
                    return@mapNotNull "${function.location.format}: Invalid parameter type: ${invalidParameters.joinToString()}"
                }

                null
            }
            .toList()

        if (invalidListeners.isEmpty()) return emptyList()
        throw InvalidEntryListenerParametersException(invalidListeners)
    }
}

private class Parameter(val parameter: KSValueParameter) {
    val type by lazy(LazyThreadSafetyMode.NONE) { parameter.type.resolve() }
    val declaration by lazy(LazyThreadSafetyMode.NONE) { type.declaration as? KSClassDeclaration }
    val superTypes by lazy(LazyThreadSafetyMode.NONE) {
        declaration?.getAllSuperTypes()?.toList() ?: emptyList()
    }

    fun isOrExtends(className: String): Boolean {
        if (type whenClassNameIs className) return true
        return superTypes.any { it whenClassNameIs className }
    }

    override fun toString(): String {
        return "(${parameter.name}: ${declaration?.fullName})"
    }
}

class InvalidEntryListenerParametersException(functions: List<String>) : Exception(
    """
    |Entry listeners must have at least one parameter extending org.bukkit.event.Event.
    |Other valid parameters are ${allowedTypes.joinToString(", ")}.
    |
    |The following functions have invalid parameters:
    | - ${functions.joinToString("\n - ")}
    | 
""".trimMargin()
)

class EntryListenerParameterValidatorProvider : SymbolProcessorProvider {
    override fun create(environment: SymbolProcessorEnvironment): SymbolProcessor {
        return EntryListenerParameterValidator()
    }
}