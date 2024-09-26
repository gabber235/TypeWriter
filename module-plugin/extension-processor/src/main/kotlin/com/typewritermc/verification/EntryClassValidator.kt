package com.typewritermc.verification

import com.google.devtools.ksp.processing.Resolver
import com.google.devtools.ksp.processing.SymbolProcessor
import com.google.devtools.ksp.processing.SymbolProcessorEnvironment
import com.google.devtools.ksp.processing.SymbolProcessorProvider
import com.google.devtools.ksp.symbol.ClassKind
import com.google.devtools.ksp.symbol.KSAnnotated
import com.google.devtools.ksp.symbol.KSClassDeclaration
import com.google.devtools.ksp.symbol.Modifier
import com.typewritermc.core.extension.annotations.Entry
import com.typewritermc.processors.fullName

class EntryClassValidator : SymbolProcessor {
    override fun process(resolver: Resolver): List<KSAnnotated> {
        val entries = resolver.getSymbolsWithAnnotation(Entry::class.qualifiedName!!)
        val invalidEntries = entries
            .filterIsInstance<KSClassDeclaration>()
            .mapNotNull { classDeclaration ->
                val issues = mutableListOf<String>()

                if (classDeclaration.isDataClass()) {
                    issues.add("a data class")
                }
                if (classDeclaration.isInterface()) {
                    issues.add("an interface")
                }
                if (classDeclaration.isAbstract()) {
                    issues.add("abstract")
                }

                if (issues.isNotEmpty()) {
                    "${classDeclaration.fullName}: Cannot be ${issues.joinToString(", ")}"
                } else {
                    null
                }
            }
            .toList()

        if (invalidEntries.isEmpty()) return emptyList()
        throw InvalidEntryClassException(invalidEntries)
    }
}

private fun KSClassDeclaration.isDataClass() = modifiers.contains(Modifier.DATA)
private fun KSClassDeclaration.isInterface() = classKind == ClassKind.INTERFACE
private fun KSClassDeclaration.isAbstract() = modifiers.contains(Modifier.ABSTRACT)

class InvalidEntryClassException(entries: List<String>) : Exception(
    """
    |Classes annotated with @Entry must not be data classes, interfaces, or abstract.
    |The following entries have issues:
    | - ${entries.joinToString("\n - ")}
    | 
""".trimMargin()
)

class EntryClassValidatorProvider : SymbolProcessorProvider {
    override fun create(environment: SymbolProcessorEnvironment): SymbolProcessor {
        return EntryClassValidator()
    }
}