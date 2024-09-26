package com.typewritermc.verification

import com.google.devtools.ksp.KspExperimental
import com.google.devtools.ksp.getAnnotationsByType
import com.google.devtools.ksp.processing.*
import com.google.devtools.ksp.symbol.KSAnnotated
import com.google.devtools.ksp.symbol.KSClassDeclaration
import com.google.devtools.ksp.symbol.KSPropertyDeclaration
import com.typewritermc.core.extension.annotations.Icon
import com.typewritermc.processors.fullName
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.runBlocking
import java.io.File

class EntryParameterIconAnnotationValidator(
    val logger: KSPLogger,
    val buildDir: File,
) : SymbolProcessor {
    @OptIn(KspExperimental::class)
    override fun process(resolver: Resolver): List<KSAnnotated> {
        val parameters = resolver.getSymbolsWithAnnotation(Icon::class.qualifiedName!!).toList()

        val invalidParameters = validateIcons(logger, buildDir) {
            runBlocking {
                parameters
                    .filterIsInstance<KSPropertyDeclaration>()
                    .map { param ->
                        async {
                            val annotation = param.getAnnotationsByType(Icon::class).first()
                            val isValid = annotation.icon.isValidIcon()
                            ParameterIconInfo(param, annotation, isValid)
                        }
                    }
                    .toList()
                    .awaitAll()
                    .filter { !it.isValid }
                    .map {
                        if (it.param !is KSClassDeclaration) it.annotation.icon
                        else "${it.param.fullName}: ${it.annotation.icon}"
                    }
            }
        }

        if (invalidParameters.isEmpty()) return emptyList()
        throw InvalidParameterIconException(invalidParameters)
    }
}

data class ParameterIconInfo(
    val param: KSPropertyDeclaration,
    val annotation: Icon,
    val isValid: Boolean,
)

class InvalidParameterIconException(names: List<String>) : Exception(
    """
    |Icon must be a valid icon from https://iconify.design/.
    |The following icons are invalid:
    | - ${names.joinToString("\n - ")}
    |
""".trimMargin()
)

class EntryParameterIconValidatorProvider : SymbolProcessorProvider {
    override fun create(environment: SymbolProcessorEnvironment): SymbolProcessor {
        val buildDirPath = environment.options["buildDir"] as String
        val buildDir = File(buildDirPath)
        return EntryParameterIconAnnotationValidator(environment.logger, buildDir)
    }
}