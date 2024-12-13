package com.typewritermc.verification

import com.google.devtools.ksp.KspExperimental
import com.google.devtools.ksp.getAnnotationsByType
import com.google.devtools.ksp.processing.Resolver
import com.google.devtools.ksp.processing.SymbolProcessor
import com.google.devtools.ksp.processing.SymbolProcessorEnvironment
import com.google.devtools.ksp.processing.SymbolProcessorProvider
import com.google.devtools.ksp.symbol.KSAnnotated
import com.google.devtools.ksp.symbol.KSClassDeclaration
import com.typewritermc.core.extension.annotations.Messenger
import com.typewritermc.core.interaction.InteractionContext
import com.typewritermc.processors.annotationClassValue
import com.typewritermc.processors.fullName
import com.typewritermc.processors.hasParameter

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

                if (!parameters.hasParameter(0, "org.bukkit.entity.Player")) {
                    issues.add("First parameter must be of type 'org.bukkit.entity.Player'")
                }

                if (!parameters.hasParameter(1, InteractionContext::class.qualifiedName!!)) {
                    issues.add("Second parameter must be of type '${InteractionContext::class.qualifiedName}'")
                }

                val annotation = classDeclaration.getAnnotationsByType(Messenger::class).firstOrNull()!!
                val entryClass = with(resolver) { annotation.annotationClassValue { dialogue } }
                if (!parameters.hasParameter(2, entryClass.fullName)) {
                    issues.add("Third parameter must be of type '${entryClass.fullName}'")
                }

                if (parameters.size != 3) {
                    issues.add("Expected 3 parameters, but got ${parameters.size}")
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