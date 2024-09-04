package com.typewritermc.processors

import com.google.devtools.ksp.processing.KSPLogger
import com.google.devtools.ksp.processing.Resolver
import com.google.devtools.ksp.symbol.KSAnnotated
import com.google.devtools.ksp.symbol.KSClassDeclaration
import com.typewritermc.SharedJsonManager
import com.typewritermc.core.extension.annotations.Initializer
import com.typewritermc.processors.entry.blueprintJson
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.JsonArray
import kotlinx.serialization.json.JsonElement
import kotlinx.serialization.json.encodeToJsonElement

class InitializerProcessor(
    private val json: SharedJsonManager,
    private val logger: KSPLogger,
) : ExtensionPartProcessor {
    override fun process(resolver: Resolver): List<KSAnnotated> {
        val symbols = resolver.getSymbolsWithAnnotation(Initializer::class.qualifiedName!!)
        val initializers = symbols.filterIsInstance<KSClassDeclaration>()
            .map { generateInitializerBlueprint(it) }
            .toList()

        logger.warn("Generated blueprints for ${initializers.size} initializers")

        json.updateSection("initializers", JsonArray(initializers))
        return symbols.toList()
    }

    private fun generateInitializerBlueprint(clazz: KSClassDeclaration): JsonElement {
        logger.info("Generating initializer blueprint for ${clazz.simpleName.asString()}")

        val blueprint = InitializerBlueprint(
            className = clazz.qualifiedName?.asString() ?: throw IllegalClassTypeException(clazz.simpleName.asString())
        )

        return blueprintJson.encodeToJsonElement(blueprint)
    }
}

@Serializable
data class InitializerBlueprint(
    val className: String,
)