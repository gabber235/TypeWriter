package com.typewritermc.verification

import com.google.devtools.ksp.KspExperimental
import com.google.devtools.ksp.getAnnotationsByType
import com.google.devtools.ksp.processing.Resolver
import com.google.devtools.ksp.processing.SymbolProcessor
import com.google.devtools.ksp.processing.SymbolProcessorEnvironment
import com.google.devtools.ksp.processing.SymbolProcessorProvider
import com.google.devtools.ksp.symbol.KSAnnotated
import com.google.devtools.ksp.symbol.KSClassDeclaration
import com.typewritermc.core.extension.annotations.Entry
import com.typewritermc.processors.fullName

private val regex = Regex("^[a-z0-9_]+$")

class EntryNameValidator : SymbolProcessor {
    @OptIn(KspExperimental::class)
    override fun process(resolver: Resolver): List<KSAnnotated> {
        val entries = resolver.getSymbolsWithAnnotation(Entry::class.qualifiedName!!)
        val invalidEntries = entries
            .map { it to it.getAnnotationsByType(Entry::class).first() }
            .filter { (_, annotation) ->
                !annotation.name.matches(regex)
            }
            .map { (entry, annotation) ->
                if (entry !is KSClassDeclaration) return@map annotation.name
                "${entry.fullName}: ${annotation.name}"
            }
            .toList()

        if (invalidEntries.isEmpty()) return emptyList()
        throw InvalidEntryNamesException(invalidEntries)
    }
}

class InvalidEntryNamesException(names: List<String>) : Exception(
    """
    |Entry names can only contain lowercase letters, numbers and underscores.
    |The following names are invalid: 
    | - ${names.joinToString("\n - ")}
    | 
""".trimMargin()
)

class EntryNameValidatorProvider : SymbolProcessorProvider {
    override fun create(environment: SymbolProcessorEnvironment): SymbolProcessor {
        return EntryNameValidator()
    }
}