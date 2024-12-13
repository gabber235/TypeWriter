package com.typewritermc.verification

import com.google.devtools.ksp.processing.Resolver
import com.google.devtools.ksp.processing.SymbolProcessor
import com.google.devtools.ksp.processing.SymbolProcessorEnvironment
import com.google.devtools.ksp.processing.SymbolProcessorProvider
import com.google.devtools.ksp.symbol.KSAnnotated
import com.google.devtools.ksp.symbol.KSClassDeclaration
import com.typewritermc.core.extension.annotations.*
import com.typewritermc.processors.fullName
import com.typewritermc.processors.isImplementingInterface
import kotlin.reflect.KClass

class InterfaceValidator<T : Annotation>(
    private val annotationClass: KClass<T>,
    private val interfaceName: String
) : SymbolProcessor {
    override fun process(resolver: Resolver): List<KSAnnotated> {
        val symbols = resolver.getSymbolsWithAnnotation(annotationClass.qualifiedName!!)
        val invalidSymbols = symbols
            .filterIsInstance<KSClassDeclaration>()
            .mapNotNull { classDeclaration ->
                if (!classDeclaration.isImplementingInterface(interfaceName)) {
                    "${classDeclaration.fullName}: Does not implement $interfaceName"
                } else {
                    null
                }
            }
            .toList()

        if (invalidSymbols.isEmpty()) return emptyList()
        throw InterfaceValidationException(annotationClass.simpleName!!, interfaceName, invalidSymbols)
    }
}


class InterfaceValidationException(annotationName: String, interfaceName: String, entries: List<String>) : Exception(
    """
    |Classes annotated with @$annotationName must implement $interfaceName.
    |The following entries have issues:
    | - ${entries.joinToString("\n - ")}
    |
""".trimMargin()
)

open class InterfaceValidatorProvider<T : Annotation>(
    private val annotationClass: KClass<T>,
    private val interfaceName: String
) : SymbolProcessorProvider {
    override fun create(environment: SymbolProcessorEnvironment): SymbolProcessor {
        return InterfaceValidator(annotationClass, interfaceName)
    }
}

class EntryOnlyOnEntryValidatorProvider : InterfaceValidatorProvider<Entry>(
    Entry::class,
    com.typewritermc.core.entries.Entry::class.qualifiedName!!
)

class TagsOnlyOnEntryValidatorProvider : InterfaceValidatorProvider<Tags>(
    Tags::class,
    com.typewritermc.core.entries.Entry::class.qualifiedName!!
)

class VariableDataOnlyOnEntryValidatorProvider : InterfaceValidatorProvider<VariableData>(
    VariableData::class,
    com.typewritermc.core.entries.Entry::class.qualifiedName!!
)

class GenericConstraintOnlyOnEntryValidatorProvider : InterfaceValidatorProvider<GenericConstraint>(
    GenericConstraint::class,
    com.typewritermc.core.entries.Entry::class.qualifiedName!!
)

class ContextKeysOnlyOnEntryValidatorProvider : InterfaceValidatorProvider<ContextKeys>(
    ContextKeys::class,
    com.typewritermc.core.entries.Entry::class.qualifiedName!!
)

class KeyTypeOnlyOnEntryContextKeysValidatorProvider : InterfaceValidatorProvider<KeyType>(
    KeyType::class,
    com.typewritermc.core.interaction.EntryContextKey::class.qualifiedName!!
)

class MessengerOnlyOnMessengerValidatorProvider : InterfaceValidatorProvider<Messenger>(
    Messenger::class,
    "com.typewritermc.engine.paper.entry.dialogue.DialogueMessenger"
)
