package com.typewritermc.verification

import com.google.devtools.ksp.processing.Resolver
import com.google.devtools.ksp.processing.SymbolProcessor
import com.google.devtools.ksp.processing.SymbolProcessorEnvironment
import com.google.devtools.ksp.processing.SymbolProcessorProvider
import com.google.devtools.ksp.symbol.ClassKind
import com.google.devtools.ksp.symbol.KSAnnotated
import com.google.devtools.ksp.symbol.KSClassDeclaration
import com.typewritermc.core.extension.annotations.Initializer
import com.typewritermc.processors.fullName

class InitializerIsObjectValidator : SymbolProcessor {
    override fun process(resolver: Resolver): List<KSAnnotated> {
        val initializers = resolver.getSymbolsWithAnnotation(Initializer::class.qualifiedName!!)
        val invalidInitializers = initializers
            .filterIsInstance<KSClassDeclaration>()
            .mapNotNull { classDeclaration ->
                if (classDeclaration.classKind != ClassKind.OBJECT) {
                    classDeclaration.fullName
                } else {
                    null
                }
            }
            .toList()

        if (invalidInitializers.isEmpty()) return emptyList()
        throw InvalidInitializerObjectException(invalidInitializers)
    }
}

class InvalidInitializerObjectException(entries: List<String>) : Exception(
    """
    |Classes annotated with @Initializer must be objects.
    |The following entries have issues:
    | - ${entries.joinToString("\n - ")}
    | 
""".trimMargin()
)

class InitializerObjectValidatorProvider : SymbolProcessorProvider {
    override fun create(environment: SymbolProcessorEnvironment): SymbolProcessor {
        return InitializerIsObjectValidator()
    }
}