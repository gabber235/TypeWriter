package com.typewritermc.processors.entry.editors

import com.google.devtools.ksp.processing.KSPLogger
import com.google.devtools.ksp.symbol.KSType
import com.typewritermc.core.extension.annotations.ContentEditor
import com.typewritermc.core.utils.point.Position
import com.typewritermc.processors.entry.*
import com.typewritermc.processors.entry.DataBlueprint.ObjectBlueprint
import com.typewritermc.processors.entry.DataBlueprint.PrimitiveBlueprint
import com.typewritermc.processors.entry.modifiers.ContentEditorModifierComputer
import com.typewritermc.processors.whenClassIs
import kotlinx.serialization.json.JsonElement
import kotlinx.serialization.json.JsonObject
import kotlinx.serialization.json.JsonPrimitive

object PositionEditor : CustomEditor {
    override val id: String = "position"

    override fun accept(type: KSType): Boolean {
        return type whenClassIs Position::class
    }

    context(KSPLogger) override fun default(type: KSType): JsonElement {
        return JsonObject(
            mapOf(
                "world" to JsonPrimitive(""),
                "x" to JsonPrimitive(0.0),
                "y" to JsonPrimitive(0.0),
                "z" to JsonPrimitive(0.0),
                "yaw" to JsonPrimitive(0.0),
                "pitch" to JsonPrimitive(0.0),
            )
        )
    }

    context(KSPLogger) override fun shape(type: KSType): DataBlueprint {
        return ObjectBlueprint(
            mapOf(
                "world" to PrimitiveBlueprint(PrimitiveType.STRING),
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
                "com.typewritermc.engine.paper.content.modes.custom.PositionContentMode"
            )
        )
    }
}