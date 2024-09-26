package com.typewritermc.verification

import com.google.devtools.ksp.KspExperimental
import com.google.devtools.ksp.getAnnotationsByType
import com.google.devtools.ksp.processing.*
import com.google.devtools.ksp.symbol.KSAnnotated
import com.google.devtools.ksp.symbol.KSClassDeclaration
import com.typewritermc.core.extension.annotations.Entry
import com.typewritermc.core.extension.annotations.Segments
import com.typewritermc.processors.fullName
import com.typewritermc.processors.isOrExtends
import com.typewritermc.processors.whenClassIs
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.runBlocking
import java.io.File

class CinematicEntrySegmentsValidator(
    val logger: KSPLogger,
    val buildDir: File,
) : SymbolProcessor {
    @OptIn(KspExperimental::class)
    override fun process(resolver: Resolver): List<KSAnnotated> {
        val entries = resolver.getSymbolsWithAnnotation(Entry::class.qualifiedName!!)
            .filterIsInstance<KSClassDeclaration>()
            .filter { it.isOrExtends("com.typewritermc.engine.paper.entry.entries.CinematicEntry") }

        val invalidEntries = validateIcons(logger, buildDir) {
            runBlocking {
                entries
                    .map { entry ->
                        async {
                            val segments = entry.getAllProperties()
                                .filter { it.getAnnotationsByType(Segments::class).toList().isNotEmpty() }.toList()
                            if (segments.isEmpty()) {
                                return@async "${entry.fullName}: At least 1 parameter needs to have a @Segments annotation"
                            }

                            val invalidTypes = segments.mapNotNull { prop ->
                                val type = prop.type.resolve()
                                if (!type.whenClassIs(List::class)) return@mapNotNull "${prop.simpleName} needs to be a List of Segments"
                                if (type.arguments.size != 1) return@mapNotNull "${prop.simpleName} needs to be a List of Segments"
                                val segmentType = type.arguments.first().type?.resolve()
                                    ?: return@mapNotNull "${prop.simpleName} needs to be a List of Segments"
                                if (!segmentType.isOrExtends("com.typewritermc.engine.paper.entry.entries.Segment")) return@mapNotNull "${segmentType.fullName} does not implements com.typewritermc.engine.paper.entry.entries.Segment"
                                null
                            }

                            if (invalidTypes.isNotEmpty()) return@async "${entry.fullName}: ${invalidTypes.joinToString("\n - ")}"

                            val invalidIcons = segments.mapNotNull { prop ->
                                val annotation = prop.getAnnotationsByType(Segments::class).first()
                                if (!annotation.icon.isValidIcon()) {
                                    "Invalid icon for ${prop.simpleName}: ${annotation.icon}"
                                } else null
                            }
                            if (invalidIcons.isNotEmpty()) return@async "${entry.fullName}: ${invalidIcons.joinToString("\n - ")}"

                            null
                        }
                    }
                    .toList()
                    .awaitAll()
                    .filterNotNull()
                    .toList()
            }
        }

        if (invalidEntries.isEmpty()) return emptyList()
        throw InvalidCinematicEntryException(invalidEntries)
    }
}

class InvalidCinematicEntryException(names: List<String>) : Exception(
    """
    |Cinematic entries must have at least one field annotated with @Segments and the icon must be valid.
    |The following entries are invalid:
    | - ${names.joinToString("\n - ")}
    |
""".trimMargin()
)

class CinematicEntrySegmentsValidatorProvider : SymbolProcessorProvider {
    override fun create(environment: SymbolProcessorEnvironment): SymbolProcessor {
        val buildDirPath = environment.options["buildDir"] as String
        val buildDir = File(buildDirPath)
        return CinematicEntrySegmentsValidator(environment.logger, buildDir)
    }
}