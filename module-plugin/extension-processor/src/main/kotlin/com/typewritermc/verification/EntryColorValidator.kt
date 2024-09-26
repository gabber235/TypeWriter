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

private val regex = Regex("^#[0-9a-fA-F]{6}$")

class EntryColorValidator : SymbolProcessor {
    @OptIn(KspExperimental::class)
    override fun process(resolver: Resolver): List<KSAnnotated> {
        val entries = resolver.getSymbolsWithAnnotation(Entry::class.qualifiedName!!)
        val invalidEntries = entries
            .map { it to it.getAnnotationsByType(Entry::class).first() }
            .filter { (_, annotation) ->
                !annotation.color.matches(regex)
            }
            .map { (entry, annotation) ->
                if (entry !is KSClassDeclaration) return@map annotation.color
                "${entry.fullName}: ${annotation.color}"
            }
            .toList()

        if (invalidEntries.isEmpty()) return emptyList()
        throw InvalidEntryColorsException(invalidEntries)
    }
}

class InvalidEntryColorsException(colors: List<String>) : Exception(
    """
    |Entry colors must be valid hex colors (e.g., #FFFFFF).
    |The following colors are invalid:
    | - ${colors.joinToString("\n - ")}
    | 
""".trimMargin()
)

class EntryColorValidatorProvider : SymbolProcessorProvider {
    override fun create(environment: SymbolProcessorEnvironment): SymbolProcessor {
        return EntryColorValidator()
    }
}