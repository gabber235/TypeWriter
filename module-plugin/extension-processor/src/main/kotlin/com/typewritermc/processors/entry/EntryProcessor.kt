package com.typewritermc.processors.entry

import com.google.devtools.ksp.KspExperimental
import com.google.devtools.ksp.getAllSuperTypes
import com.google.devtools.ksp.getAnnotationsByType
import com.google.devtools.ksp.processing.KSPLogger
import com.google.devtools.ksp.processing.Resolver
import com.google.devtools.ksp.symbol.KSAnnotated
import com.google.devtools.ksp.symbol.KSClassDeclaration
import com.typewritermc.SharedJsonManager
import com.typewritermc.core.extension.annotations.Entry
import com.typewritermc.core.extension.annotations.GenericConstraint
import com.typewritermc.core.extension.annotations.Tags
import com.typewritermc.core.extension.annotations.VariableData
import com.typewritermc.moduleplugin.TypewriterExtensionConfiguration
import com.typewritermc.processors.ExtensionPartProcessor
import com.typewritermc.processors.annotationClassValue
import com.typewritermc.processors.fullName
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.JsonArray
import kotlinx.serialization.json.JsonElement
import kotlinx.serialization.json.encodeToJsonElement

class EntryProcessor(
    private val json: SharedJsonManager,
    private val logger: KSPLogger,
    private val configuration: TypewriterExtensionConfiguration,
) : ExtensionPartProcessor {
    override fun process(resolver: Resolver): List<KSAnnotated> {
        val symbols = resolver.getSymbolsWithAnnotation(Entry::class.qualifiedName!!)
        val entries = symbols.filterIsInstance<KSClassDeclaration>()
            .map { with(resolver) { generateEntryBlueprint(it) } }
            .toList()

        logger.warn("Generated blueprints for ${entries.size} entries")

        json.updateSection("entries", JsonArray(entries))

        return symbols.toList()
    }

    context(Resolver)
    @OptIn(KspExperimental::class)
    private fun generateEntryBlueprint(clazz: KSClassDeclaration): JsonElement {
        logger.info("Generating entry blueprint for ${clazz.simpleName.asString()}")
        val annotation = clazz.getAnnotationsByType(Entry::class).first()
        val dataBlueprint = clazz.fieldBlueprints()

        val blueprint = EntryBlueprint(
            id = annotation.name,
            name = annotation.name,
            description = annotation.description,
            color = annotation.color,
            icon = annotation.icon,
            tags = clazz.tags,
            dataBlueprint = dataBlueprint,
            genericConstraints = clazz.genericConstraints(dataBlueprint),
            variableDataBlueprint = clazz.variableDataBlueprint(),
            className = clazz.qualifiedName?.asString() ?: throw IllegalClassTypeException(clazz.simpleName.asString()),
            extension = configuration.name,
        )

        return blueprintJson.encodeToJsonElement(blueprint)
    }

    @OptIn(KspExperimental::class)
    private val KSClassDeclaration.tags: List<String>
        get() {
            val clazzTags = getAnnotationsByType(Tags::class).firstOrNull()?.tags?.toList() ?: emptyList()
            val superTags: List<String> =
                getAllSuperTypes().map { it.declaration }.filterIsInstance<KSClassDeclaration>().flatMap {
                    it.getAnnotationsByType(Tags::class).firstOrNull()?.tags?.toList() ?: emptyList()
                }.toList()

            return clazzTags + superTags
        }

    context(Resolver)
    private fun KSClassDeclaration.fieldBlueprints(): DataBlueprint {
        with(logger) {
            try {
                return DataBlueprint.blueprint(this@fieldBlueprints.asStarProjectedType())
                    ?: throw CouldNotBuildBlueprintException(fullName)
            } catch (e: Exception) {
                throw FailedToGenerateBlueprintException(this@fieldBlueprints, e)
            }
        }
    }

    context(Resolver)
    @OptIn(KspExperimental::class)
    private fun KSClassDeclaration.genericConstraints(fields: DataBlueprint): List<DataBlueprint>? {
        with(logger) {
            val data = getAnnotationsByType(GenericConstraint::class)
                .map { annotation ->
                    annotation.annotationClassValue { type }
                }
                .toList()

            if (data.isEmpty()) {
                // If any field has a generic value,
                // we want to return an empty list to indicate that the entry is generic.
                // Otherwise, we want to return null to indicate that the entry is not generic.
                val hasGenericField = fields.walkAny {
                    if (it !is DataBlueprint.CustomBlueprint) return@walkAny false
                    return@walkAny it.editor == "generic"
                }
                if (hasGenericField) return emptyList()

                return null
            }

            return data.map {
                DataBlueprint.blueprint(it) ?: throw FailedToGenerateBlueprintException(
                    this@genericConstraints,
                    CouldNotBuildBlueprintException(it.fullName)
                )
            }
        }
    }


    context(Resolver)
    @OptIn(KspExperimental::class)
    private fun KSClassDeclaration.variableDataBlueprint(): DataBlueprint? {
        with(logger) {
            val data = getAnnotationsByType(VariableData::class)
                .map { it.annotationClassValue { it.type } }
                .firstOrNull() ?: return null

            val blueprint = DataBlueprint.blueprint(data) ?: throw FailedToGenerateBlueprintException(
                this@variableDataBlueprint,
                CouldNotBuildBlueprintException(data.fullName)
            )
            return blueprint
        }
    }
}

@Serializable
private data class EntryBlueprint(
    val id: String,
    val name: String,
    val description: String,
    val color: String,
    val icon: String,
    val tags: List<String>,
    val dataBlueprint: DataBlueprint,
    val genericConstraints: List<DataBlueprint>?,
    val variableDataBlueprint: DataBlueprint?,
    val className: String,
    val extension: String,
)

class IllegalClassTypeException(className: String) :
    Exception("Class $className does not have a qualified name. Entry classes must be full classes.")

class CouldNotBuildBlueprintException(className: String) :
    Exception("Could not build blueprint for class $className")

class FailedToGenerateBlueprintException(
    klass: KSClassDeclaration,
    cause: Exception,
) : Exception(
    """Failed Generating Blueprint for ${klass.fullName}
    |
    |Failed to generate blueprint for ${klass.fullName}:
    |${cause.message}
    |
    |Not all types are possible to be serialized to JSON.
    |Most platform specific types are not supported. Some examples are:
    | - org.bukkit.Location
    |
    |If you think this is a mistake, or don't know how to fix it, please open an issue on the Typewriter Discord.
|""".trimMargin()
)