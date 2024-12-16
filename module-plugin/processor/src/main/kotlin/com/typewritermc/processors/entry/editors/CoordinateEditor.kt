package com.typewritermc.processors.entry.editors

import com.google.devtools.ksp.processing.KSPLogger
import com.google.devtools.ksp.processing.Resolver
import com.google.devtools.ksp.symbol.KSType
import com.typewritermc.core.utils.point.Coordinate
import com.typewritermc.processors.entry.CustomEditor
import com.typewritermc.processors.entry.DataBlueprint
import com.typewritermc.processors.entry.DataBlueprint.ObjectBlueprint
import com.typewritermc.processors.entry.DataBlueprint.PrimitiveBlueprint
import com.typewritermc.processors.entry.DataModifier
import com.typewritermc.processors.entry.PrimitiveType
import com.typewritermc.processors.whenClassIs
import kotlinx.serialization.json.JsonElement
import kotlinx.serialization.json.JsonObject
import kotlinx.serialization.json.JsonPrimitive

object CoordinateEditor : CustomEditor {
    override val id: String = "coordinate"

    override fun accept(type: KSType): Boolean {
        return type whenClassIs Coordinate::class
    }

    context(KSPLogger, Resolver) override fun default(type: KSType): JsonElement {
        return JsonObject(
            mapOf(
                "x" to JsonPrimitive(0.0),
                "y" to JsonPrimitive(0.0),
                "z" to JsonPrimitive(0.0),
                "yaw" to JsonPrimitive(0.0),
                "pitch" to JsonPrimitive(0.0),
            )
        )
    }

    context(KSPLogger, Resolver) override fun shape(type: KSType): DataBlueprint {
        return ObjectBlueprint(
            mapOf(
                "x" to PrimitiveBlueprint(PrimitiveType.DOUBLE),
                "y" to PrimitiveBlueprint(PrimitiveType.DOUBLE),
                "z" to PrimitiveBlueprint(PrimitiveType.DOUBLE),
                "yaw" to PrimitiveBlueprint(PrimitiveType.DOUBLE),
                "pitch" to PrimitiveBlueprint(PrimitiveType.DOUBLE),
            )
        )
    }

    override fun modifiers(type: KSType): List<DataModifier> {
        // TODO: Use the actual ContentEditorModifierComputer
        return listOf(
            DataModifier.Modifier(
                "contentMode",
                "com.typewritermc.engine.paper.content.modes.custom.CoordinateContentMode"
            )
        )
    }
}