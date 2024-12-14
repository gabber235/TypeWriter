package com.typewritermc.processors.entry.editors

import com.google.devtools.ksp.processing.KSPLogger
import com.google.devtools.ksp.processing.Resolver
import com.google.devtools.ksp.symbol.KSType
import com.typewritermc.core.interaction.GlobalContextKey
import com.typewritermc.processors.entry.CustomEditor
import com.typewritermc.processors.entry.DataBlueprint
import com.typewritermc.processors.entry.PrimitiveType
import com.typewritermc.processors.whenClassIs
import kotlinx.serialization.json.JsonElement

object GlobalContextKeyEditor : CustomEditor {
    override val id: String = "globalContextKey"

    override fun accept(type: KSType): Boolean {
        return type whenClassIs GlobalContextKey::class
    }

    context(KSPLogger, Resolver) override fun default(type: KSType): JsonElement {
        return shape(type).default()
    }

    context(KSPLogger, Resolver) override fun shape(type: KSType): DataBlueprint {
        return DataBlueprint.PrimitiveBlueprint(PrimitiveType.STRING)
    }
}