package com.typewritermc.verification

import com.google.devtools.ksp.processing.Resolver
import com.google.devtools.ksp.processing.SymbolProcessor
import com.google.devtools.ksp.processing.SymbolProcessorEnvironment
import com.google.devtools.ksp.processing.SymbolProcessorProvider
import com.google.devtools.ksp.symbol.KSAnnotated
import com.google.devtools.ksp.symbol.KSClassDeclaration
import com.google.devtools.ksp.symbol.KSTypeReference
import com.typewritermc.core.extension.annotations.Entry
import com.typewritermc.processors.fullName
import com.google.devtools.ksp.symbol.ClassKind
import com.typewritermc.core.entries.Ref
import com.typewritermc.processors.whenClassIs
import com.typewritermc.processors.whenClassNameIs

class EntryConstructorMapKeyValidator : SymbolProcessor {
    override fun process(resolver: Resolver): List<KSAnnotated> {
        val entries = resolver.getSymbolsWithAnnotation(Entry::class.qualifiedName!!)
        val invalidEntries = entries
            .filterIsInstance<KSClassDeclaration>()
            .mapNotNull { classDeclaration ->
                val primaryConstructor = classDeclaration.primaryConstructor ?: return@mapNotNull null
                val invalidParams = primaryConstructor.parameters.filter { param ->
                    param.type.isMapType() && !param.type.isValidMapKeyType()
                }
                if (invalidParams.isNotEmpty()) {
                    classDeclaration.fullName
                } else {
                    null
                }
            }
            .toList()

        if (invalidEntries.isEmpty()) return emptyList()
        throw InvalidEntryConstructorMapKeyException(invalidEntries)
    }

    private fun KSTypeReference.isMapType(): Boolean {
        return this.resolve().whenClassNameIs("kotlin.collections.Map")
    }

    private fun KSTypeReference.isValidMapKeyType(): Boolean {
        val keyType = this.element?.typeArguments?.firstOrNull()?.type?.resolve() ?: return false

        if (keyType whenClassNameIs "kotlin.String") return true
        val declaration = keyType.declaration
        if (declaration is KSClassDeclaration && declaration.classKind == ClassKind.ENUM_CLASS) return true
        if (keyType whenClassIs Ref::class) return true

        return false
    }
}

class InvalidEntryConstructorMapKeyException(entries: List<String>) : Exception(
    """
    |Classes annotated with @Entry must have primary constructor parameters of type Map with keys that are either String, Enum, or Entry reference.
    |The following entries have issues:
    | - ${entries.joinToString("\n - ")}
    |
""".trimMargin()
)

class EntryConstructorMapKeyValidatorProvider : SymbolProcessorProvider {
    override fun create(environment: SymbolProcessorEnvironment): SymbolProcessor {
        return EntryConstructorMapKeyValidator()
    }
}