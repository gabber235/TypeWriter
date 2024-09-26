package com.typewritermc.verification

import com.google.devtools.ksp.processing.Resolver
import com.google.devtools.ksp.processing.SymbolProcessor
import com.google.devtools.ksp.processing.SymbolProcessorEnvironment
import com.google.devtools.ksp.processing.SymbolProcessorProvider
import com.google.devtools.ksp.symbol.KSAnnotated
import com.google.devtools.ksp.symbol.KSClassDeclaration
import com.typewritermc.core.extension.Initializable
import com.typewritermc.core.extension.annotations.Initializer
import com.typewritermc.processors.fullName
import com.typewritermc.processors.whenClassIs

class InitializerOnlyOnInitializableValidator : SymbolProcessor {
    override fun process(resolver: Resolver): List<KSAnnotated> {
        val initializers = resolver.getSymbolsWithAnnotation(Initializer::class.qualifiedName!!)
        val invalidInitializers = initializers
            .filterIsInstance<KSClassDeclaration>()
            .mapNotNull { classDeclaration ->
                if (!classDeclaration.isImplementingInitializable()) {
                    "${classDeclaration.fullName}: Does not implement Initializable"
                } else {
                    null
                }
            }
            .toList()

        if (invalidInitializers.isEmpty()) return emptyList()
        throw InvalidInitializerException(invalidInitializers)
    }
}

private fun KSClassDeclaration.isImplementingInitializable(): Boolean {
    return superTypes.any { it.resolve().whenClassIs(Initializable::class) }
}

class InvalidInitializerException(entries: List<String>) : Exception(
    """
    |Classes annotated with @Initializer must implement Initializable.
    |The following entries have issues:
    |
    | - ${entries.joinToString("\n - ")}
""".trimMargin()
)

class InitializerValidatorProvider : SymbolProcessorProvider {
    override fun create(environment: SymbolProcessorEnvironment): SymbolProcessor {
        return InitializerOnlyOnInitializableValidator()
    }
}