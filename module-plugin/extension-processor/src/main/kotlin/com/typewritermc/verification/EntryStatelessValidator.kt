package com.typewritermc.verification

import com.google.devtools.ksp.getDeclaredProperties
import com.google.devtools.ksp.processing.Resolver
import com.google.devtools.ksp.processing.SymbolProcessor
import com.google.devtools.ksp.processing.SymbolProcessorEnvironment
import com.google.devtools.ksp.processing.SymbolProcessorProvider
import com.google.devtools.ksp.symbol.KSAnnotated
import com.google.devtools.ksp.symbol.KSClassDeclaration
import com.typewritermc.core.extension.annotations.Entry
import com.typewritermc.processors.format

class EntryStatelessValidator : SymbolProcessor {
    override fun process(resolver: Resolver): List<KSAnnotated> {
        val entries = resolver.getSymbolsWithAnnotation(Entry::class.qualifiedName!!)

        val invalidEntries = entries
            .filterIsInstance<KSClassDeclaration>()
            .mapNotNull { entry ->
                val parameters = entry.primaryConstructor?.parameters?.map { it.location } ?: emptyList()

                val invalidProperties = entry.getDeclaredProperties()
                    .filter { it.hasBackingField }
                    .filter { !parameters.contains(it.location) }
                    .map { it.location.format }
                    .toList()

                if (invalidProperties.isEmpty()) return@mapNotNull null

                """
                |${entry.simpleName.asString()} invalid properties:
                |    - ${invalidProperties.joinToString("\n    - ")}
                """.trimMargin()
            }.toList()

        if (invalidEntries.isEmpty()) return emptyList()
        throw StatefulEntryException(invalidEntries)
    }
}


class StatefulEntryException(names: List<String>) : Exception(
    """
    |Entries are not allowed to have any state.
    |The following entries are stateful:
    | - ${names.joinToString("\n - ")}
    | 
""".trimMargin()
)

class EntryStatelessValidatorProvider : SymbolProcessorProvider {
    override fun create(environment: SymbolProcessorEnvironment): SymbolProcessor {
        return EntryStatelessValidator()
    }
}