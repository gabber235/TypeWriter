package com.typewritermc.processors

import com.google.devtools.ksp.processing.*
import com.google.devtools.ksp.symbol.KSAnnotated
import com.google.devtools.ksp.symbol.KSFile
import com.google.devtools.ksp.symbol.KSFunctionDeclaration
import com.typewritermc.core.extension.annotations.TypewriterCommand
import com.typewritermc.processors.entry.blueprintJson
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.JsonArray
import kotlinx.serialization.json.JsonElement
import kotlinx.serialization.json.encodeToJsonElement

class TypewriterCommandProcessor(
    private val json: SharedJsonManager,
    private val logger: KSPLogger,
) : PartProcessor {
    override fun process(resolver: Resolver): List<KSAnnotated> {
        val symbols = resolver.getSymbolsWithAnnotation(TypewriterCommand::class.qualifiedName!!)
        val commands = symbols.filterIsInstance<KSFunctionDeclaration>()
            .map { generateCommandBlueprint(it) }
            .toList()

        logger.warn("Generated blueprints for ${commands.size} commands")

        json.updateSection("typewriterCommands", JsonArray(commands))
        return symbols.toList()
    }

    private fun generateCommandBlueprint(function: KSFunctionDeclaration): JsonElement {
        logger.info("Generating command blueprint for ${function.simpleName.asString()}")

        val parent = function.parent
        if (parent !is KSFile) throw MustBeTopLevelFunctionException("Typewriter commands", function.fullName)

        val blueprint = TypewriterCommandBlueprint(
            className = parent.className,
            methodName = function.simpleName.asString(),
        )

        return blueprintJson.encodeToJsonElement(blueprint)
    }
}

@Serializable
data class TypewriterCommandBlueprint(
    val className: String,
    val methodName: String,
)