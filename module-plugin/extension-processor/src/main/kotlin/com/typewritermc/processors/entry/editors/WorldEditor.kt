package com.typewritermc.processors.entry.editors

import com.google.devtools.ksp.processing.KSPLogger
import com.google.devtools.ksp.symbol.KSType
import com.typewritermc.core.utils.point.World
import com.typewritermc.processors.entry.CustomEditor
import com.typewritermc.processors.entry.DataBlueprint
import com.typewritermc.processors.entry.DataBlueprint.*
import com.typewritermc.processors.entry.PrimitiveType
import com.typewritermc.processors.entry.whenClassIs
import kotlinx.serialization.json.JsonElement
import kotlinx.serialization.json.JsonPrimitive

object WorldEditor : CustomEditor {
    override val id: String = "world"

    override fun accept(type: KSType): Boolean {
        return type whenClassIs World::class
    }

    context(KSPLogger) override fun default(type: KSType): JsonElement = JsonPrimitive("")
    context(KSPLogger) override fun shape(type: KSType): DataBlueprint = PrimitiveBlueprint(PrimitiveType.STRING)
}