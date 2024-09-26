package com.typewritermc.verification

import com.google.devtools.ksp.KspExperimental
import com.google.devtools.ksp.getAnnotationsByType
import com.google.devtools.ksp.processing.Resolver
import com.google.devtools.ksp.processing.SymbolProcessor
import com.google.devtools.ksp.processing.SymbolProcessorEnvironment
import com.google.devtools.ksp.processing.SymbolProcessorProvider
import com.google.devtools.ksp.symbol.KSAnnotated
import com.google.devtools.ksp.symbol.KSClassDeclaration
import com.google.devtools.ksp.symbol.KSValueParameter
import com.typewritermc.core.extension.annotations.Messenger
import com.typewritermc.processors.annotationClassValue
import com.typewritermc.processors.fullName
import com.typewritermc.processors.whenClassNameIs

class MessengerConstructorValidator : SymbolProcessor {
    @OptIn(KspExperimental::class)
    override fun process(resolver: Resolver): List<KSAnnotated> {
        val messengers = resolver.getSymbolsWithAnnotation(Messenger::class.qualifiedName!!)
        val invalidMessengers = messengers
            .filterIsInstance<KSClassDeclaration>()
            .mapNotNull { classDeclaration ->
                val primaryConstructor = classDeclaration.primaryConstructor
                    ?: return@mapNotNull "${classDeclaration.fullName}: No primary constructor"

                val parameters = primaryConstructor.parameters
                val issues = mutableListOf<String>()

                if (!parameters.hasParameter("org.bukkit.entity.Player")) {
                    issues.add("Missing 'player' parameter of type 'org.bukkit.entity.Player'")
                }

                val annotation = classDeclaration.getAnnotationsByType(Messenger::class).firstOrNull()!!
                val entryClass = annotation.annotationClassValue { dialogue }
                if (!parameters.hasParameter(entryClass.fullName)) {
                    issues.add("Missing 'entry' parameter of type '${entryClass.fullName}'")
                }

                if (parameters.size != 2) {
                    issues.add("Expected 2 parameters, but got ${parameters.size}")
                }

                if (issues.isNotEmpty()) {
                    "${classDeclaration.fullName}: ${issues.joinToString(", ")}"
                } else {
                    null
                }
            }
            .toList()

        if (invalidMessengers.isEmpty()) return emptyList()
        throw InvalidMessengerConstructorException(invalidMessengers)
    }
}

private fun List<KSValueParameter>.hasParameter(className: String): Boolean {
    return any { it.type.resolve().whenClassNameIs(className) }
}

class InvalidMessengerConstructorException(entries: List<String>) : Exception(
    """
    |Classes annotated with @Messenger must have a primary constructor with 'player' and 'entry' parameters.
    |The following entries have issues:
    | - ${entries.joinToString("\n - ")}
    | 
""".trimMargin()
)

class MessengerConstructorValidatorProvider : SymbolProcessorProvider {
    override fun create(environment: SymbolProcessorEnvironment): SymbolProcessor {
        return MessengerConstructorValidator()
    }
}