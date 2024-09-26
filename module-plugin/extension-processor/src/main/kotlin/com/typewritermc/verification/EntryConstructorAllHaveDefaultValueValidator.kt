package com.typewritermc.verification

import com.google.devtools.ksp.processing.Resolver
import com.google.devtools.ksp.processing.SymbolProcessor
import com.google.devtools.ksp.processing.SymbolProcessorEnvironment
import com.google.devtools.ksp.processing.SymbolProcessorProvider
import com.google.devtools.ksp.symbol.KSAnnotated
import com.google.devtools.ksp.symbol.KSClassDeclaration
import com.typewritermc.core.extension.annotations.Entry
import com.typewritermc.processors.fullName

class EntryConstructorAllHaveDefaultValueValidator : SymbolProcessor {
    override fun process(resolver: Resolver): List<KSAnnotated> {
        val entries = resolver.getSymbolsWithAnnotation(Entry::class.qualifiedName!!)
        val invalidEntries = entries
            .filterIsInstance<KSClassDeclaration>()
            .mapNotNull { classDeclaration ->
                val primaryConstructor = classDeclaration.primaryConstructor
                    ?: return@mapNotNull "${classDeclaration.fullName}: No primary constructor"
                val invalidParams = primaryConstructor.parameters.filter { it.hasDefault.not() }
                if (invalidParams.isNotEmpty()) {
                    "${classDeclaration.fullName}: ${invalidParams.joinToString(", ") { it.name?.asString() ?: "unknown" }}"
                } else {
                    null
                }
            }
            .toList()

        if (invalidEntries.isEmpty()) return emptyList()
        throw InvalidConstructorDefaultValuesException(invalidEntries)
    }
}

class InvalidConstructorDefaultValuesException(entries: List<String>) : Exception(
    """
    |All primary constructor parameters must have default values.
    |The following entries have parameters without default values:
    | - ${entries.joinToString("\n - ")}
""".trimMargin()
)

class ConstructorDefaultValueValidatorProvider : SymbolProcessorProvider {
    override fun create(environment: SymbolProcessorEnvironment): SymbolProcessor {
        return EntryConstructorAllHaveDefaultValueValidator()
    }
}