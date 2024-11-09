package com.typewritermc.processors.entry.editors

import com.google.devtools.ksp.processing.KSPLogger
import com.google.devtools.ksp.symbol.KSType
import com.typewritermc.processors.entry.CustomEditor
import com.typewritermc.processors.entry.DataBlueprint
import com.typewritermc.processors.entry.DataModifier
import com.typewritermc.processors.fullName
import com.typewritermc.processors.whenClassNameIs
import kotlinx.serialization.json.JsonElement

object ItemEditor : CustomEditor {
    override val id: String = "item"

    override fun accept(type: KSType): Boolean {
        return type whenClassNameIs "com.typewritermc.engine.paper.utils.item.Item"
    }

    context(KSPLogger) override fun default(type: KSType): JsonElement = shape(type).default()

    context(KSPLogger) override fun shape(type: KSType): DataBlueprint {
        return DataBlueprint.AlgebraicBlueprint.blueprint(type)
            ?: throw IllegalStateException("Could not find blueprint for type ${type.fullName}")
    }

    override fun modifiers(type: KSType): List<DataModifier> {
        // TODO: Use the actual ContentEditorModifierComputer
        return listOf(
            DataModifier.Modifier(
                "contentMode",
                "com.typewritermc.engine.paper.content.modes.custom.HoldingItemContentMode"
            )
        )
    }
}