package com.typewritermc.processors.entry.editors

import com.google.devtools.ksp.processing.KSPLogger
import com.google.devtools.ksp.symbol.KSType
import com.typewritermc.processors.entry.CustomEditor
import com.typewritermc.processors.entry.DataBlueprint
import com.typewritermc.processors.entry.DataBlueprint.ObjectBlueprint
import com.typewritermc.processors.entry.DataBlueprint.PrimitiveBlueprint
import com.typewritermc.processors.entry.PrimitiveType
import com.typewritermc.processors.entry.whenClassIs
import kotlinx.serialization.json.JsonElement
import kotlinx.serialization.json.JsonObject
import kotlinx.serialization.json.JsonPrimitive

object FloatRangeEditor : CustomEditor {
    override val id: String = "floatRange"

    override fun accept(type: KSType): Boolean {
        if (!type.whenClassIs(ClosedFloatingPointRange::class)) return false

        val typeArguments = type.arguments
        if (typeArguments.size != 1) return false

        val typeArgument = typeArguments.first()
        return typeArgument.type?.resolve()?.whenClassIs(Float::class) ?: false
    }

    context(KSPLogger)
    override fun default(type: KSType): JsonElement {
        return JsonObject(
            mapOf(
                "start" to JsonPrimitive(0),
                "end" to JsonPrimitive(0),
            )
        )
    }

    context(KSPLogger)
    override fun shape(type: KSType): DataBlueprint {
        return ObjectBlueprint(
            mapOf(
                "start" to PrimitiveBlueprint(PrimitiveType.INTEGER),
                "end" to PrimitiveBlueprint(PrimitiveType.INTEGER)
            )
        )
    }
}