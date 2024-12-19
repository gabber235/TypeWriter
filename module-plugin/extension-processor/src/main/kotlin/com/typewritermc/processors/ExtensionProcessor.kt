package com.typewritermc.processors

import com.google.devtools.ksp.containingFile
import com.google.devtools.ksp.processing.*
import com.google.devtools.ksp.symbol.KSAnnotated
import com.google.devtools.ksp.symbol.KSFile
import com.typewritermc.moduleplugin.TypewriterModuleConfiguration
import com.typewritermc.processors.entry.EntryProcessor
import kotlinx.serialization.json.*
import java.util.*

class ExtensionProcessor(
    private val sharedJsonManager: SharedJsonManager,
    private val codeGenerator: CodeGenerator,
    private val logger: KSPLogger,
    private val pluginVersion: String,
    private val version: String,
    private val configuration: TypewriterModuleConfiguration,
    private vararg val processors: PartProcessor,
) : SymbolProcessor {
    private var dependencies = Dependencies(false)
    override fun process(resolver: Resolver): List<KSAnnotated> {
        val files = mutableSetOf<KSFile>()
        for (processor in processors) {
            val result = processor.process(resolver)
            files.addAll(result.mapNotNull { it.containingFile })
        }
        logger.warn("Inspected ${files.size} files")
        dependencies = Dependencies(true, *files.toTypedArray())

        val extension = configuration.extension!!
        val extensionInfo = Json.encodeToJsonElement(extension).jsonObject
        sharedJsonManager.updateSection(
            "extension", JsonObject(
                extensionInfo + mapOf(
                    "engineVersion" to JsonPrimitive(extension.engineVersion),
                    "pluginVersion" to JsonPrimitive(pluginVersion),
                    "version" to JsonPrimitive(version),
                    "namespace" to JsonPrimitive(configuration.namespace),
                    "dependencies" to JsonArray(
                        extension.dependencies?.dependencies?.map { Json.encodeToJsonElement(it) } ?: emptyList()
                    )
                )
            )
        )

        return emptyList()
    }

    override fun finish() {
        super.finish()

        logger.info("Writing extension.json")
        codeGenerator.createNewFile(
            dependencies = dependencies,
            packageName = "",
            fileName = "extension",
            extensionName = "json"
        ).use { outputStream ->
            outputStream.write(sharedJsonManager.toString().toByteArray())
        }
    }
}

object EmptyProcessor : SymbolProcessor {
    override fun process(resolver: Resolver): List<KSAnnotated> = emptyList()
    override fun finish() {}
}


class ExtensionProcessorProvider : SymbolProcessorProvider {
    override fun create(environment: SymbolProcessorEnvironment): SymbolProcessor {
        val sharedJsonManager = SharedJsonManager()
        val logger = environment.logger

        val pluginVersion = environment.options["pluginVersion"] ?: "0.0.0"
        val version = environment.options["version"] ?: "0.0.0"

        val configuration = environment.options["configuration"]?.let {
            Json.decodeFromString(
                TypewriterModuleConfiguration.serializer(), String(Base64.getDecoder().decode(it))
            )
        } ?: throw IllegalArgumentException("No extension configuration found")

        val extension = configuration.extension ?: return EmptyProcessor

        return ExtensionProcessor(
            sharedJsonManager = sharedJsonManager,
            codeGenerator = environment.codeGenerator,
            logger = logger,
            pluginVersion = pluginVersion,
            version = version,
            configuration = configuration,
            EntryProcessor(sharedJsonManager, logger, extension),
            EntryListenerProcessor(sharedJsonManager, logger),
            TypewriterCommandProcessor(sharedJsonManager, logger),
            DependencyInjectionProcessor(sharedJsonManager, logger),
            GlobalContextKeysProcessor(sharedJsonManager, logger),
        )
    }
}