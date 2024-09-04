package com.typewritermc.processors.entry.editors

import com.google.devtools.ksp.KspExperimental
import com.google.devtools.ksp.getAnnotationsByType
import com.google.devtools.ksp.processing.KSPLogger
import com.google.devtools.ksp.symbol.KSType
import com.typewritermc.core.entries.Ref
import com.typewritermc.core.extension.annotations.Tags
import com.typewritermc.core.utils.ok
import com.typewritermc.processors.entry.*
import com.typewritermc.processors.entry.DataBlueprint.PrimitiveBlueprint
import com.typewritermc.processors.fullName
import kotlinx.serialization.json.JsonElement
import kotlinx.serialization.json.JsonNull

object RefEditor : CustomEditor {
    override val id: String = "ref"

    override fun accept(type: KSType): Boolean = type whenClassIs Ref::class

    context(KSPLogger) override fun default(type: KSType): JsonElement = JsonNull
    context(KSPLogger) override fun shape(type: KSType): DataBlueprint {
        return PrimitiveBlueprint(PrimitiveType.STRING)
    }

    context(KSPLogger)
    override fun validateDefault(type: KSType, default: JsonElement): Result<Unit> {
        if (default is JsonNull) return ok(Unit)
        return super.validateDefault(type, default)
    }

    @OptIn(KspExperimental::class)
    override fun modifiers(type: KSType): List<DataModifier> {
        val argument = type.arguments.firstOrNull()?.type?.resolve()
            ?: throw IllegalArgumentException("Ref requires a single type argument")
        val tags =
            argument.declaration.getAnnotationsByType(Tags::class).firstOrNull()?.tags ?: throw NoTagsFoundException(
                argument,
                type
            )
        if (tags.isEmpty()) throw NoTagsFoundException(argument, type)
        val tag = tags.first()

        return listOf(DataModifier.Modifier("entry", tag))
    }
}

class NoTagsFoundException(klass: KSType, origin: KSType) : Exception(
    """|No tags found for ${klass.declaration.simpleName.asString()}
    |${origin.fullName} tried to reference ${klass.fullName} but it does not have the @Tags annotation.
    |To be able to reference ${klass.fullName}, it needs to have the @Tags annotation with at least one tag.
""".trimMargin()
)