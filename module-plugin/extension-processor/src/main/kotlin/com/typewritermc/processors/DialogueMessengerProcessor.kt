package com.typewritermc.processors

import com.google.devtools.ksp.KspExperimental
import com.google.devtools.ksp.getAnnotationsByType
import com.google.devtools.ksp.processing.KSPLogger
import com.google.devtools.ksp.processing.Resolver
import com.google.devtools.ksp.symbol.KSAnnotated
import com.google.devtools.ksp.symbol.KSClassDeclaration
import com.typewritermc.SharedJsonManager
import com.typewritermc.core.extension.annotations.Entry
import com.typewritermc.core.extension.annotations.Messenger
import com.typewritermc.processors.entry.blueprintJson
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.JsonArray
import kotlinx.serialization.json.JsonElement
import kotlinx.serialization.json.encodeToJsonElement

class DialogueMessengerProcessor(
    private val json: SharedJsonManager,
    private val logger: KSPLogger,
) : ExtensionPartProcessor {
    override fun process(resolver: Resolver): List<KSAnnotated> {
        val symbols = resolver.getSymbolsWithAnnotation(Messenger::class.qualifiedName!!)
        val messengers = symbols.filterIsInstance<KSClassDeclaration>()
            .map { generateMessengerBlueprint(it) }
            .toList()

        logger.warn("Generated blueprints for ${messengers.size} messengers")

        json.updateSection("dialogueMessengers", JsonArray(messengers))
        return symbols.toList()
    }

    @OptIn(KspExperimental::class)
    private fun generateMessengerBlueprint(clazz: KSClassDeclaration): JsonElement {
        logger.info("Generating messenger blueprint for ${clazz.simpleName.asString()}")
        val annotation = clazz.getAnnotationsByType(Messenger::class).first()
        val entry = annotation.annotationClassValue { dialogue }
        val priority = annotation.priority
        val entryAnnotation = entry.declaration.getAnnotationsByType(Entry::class).firstOrNull()
            ?: throw EntryNotFoundException("Messenger", clazz.fullName, entry.fullName)

        val blueprint = MessengerBlueprint(
            entryBlueprintId = entryAnnotation.name,
            entryClassName = entry.fullName,
            className = clazz.qualifiedName?.asString() ?: throw IllegalClassTypeException(clazz.simpleName.asString()),
            priority = priority,
        )

        return blueprintJson.encodeToJsonElement(blueprint)
    }
}

@Serializable
data class MessengerBlueprint(
    val entryBlueprintId: String,
    val entryClassName: String,
    val className: String,
    val priority: Int,
)
