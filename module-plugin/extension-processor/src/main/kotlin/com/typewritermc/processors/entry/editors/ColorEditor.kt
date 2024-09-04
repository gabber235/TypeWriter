package com.typewritermc.processors.entry.editors

import com.google.devtools.ksp.processing.KSPLogger
import com.google.devtools.ksp.symbol.KSType
import com.typewritermc.processors.entry.CustomEditor
import com.typewritermc.processors.entry.DataBlueprint
import com.typewritermc.processors.entry.DataBlueprint.PrimitiveBlueprint
import com.typewritermc.processors.entry.PrimitiveType
import com.typewritermc.processors.entry.whenClassNameIs
import kotlinx.serialization.json.JsonElement
import kotlinx.serialization.json.JsonPrimitive

object ColorEditor : CustomEditor {
    override val id: String = "color"

    override fun accept(type: KSType): Boolean = type whenClassNameIs "com.typewritermc.engine.paper.utils.Color"

    context(KSPLogger) override fun default(type: KSType): JsonElement = JsonPrimitive(0xFFFFFFFF)
    context(KSPLogger) override fun shape(type: KSType): DataBlueprint = PrimitiveBlueprint(PrimitiveType.INTEGER)
}