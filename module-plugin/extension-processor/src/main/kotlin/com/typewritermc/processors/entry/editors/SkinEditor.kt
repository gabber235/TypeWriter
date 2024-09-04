package com.typewritermc.processors.entry.editors

import com.google.devtools.ksp.processing.KSPLogger
import com.google.devtools.ksp.symbol.KSType
import com.typewritermc.processors.entry.CustomEditor
import com.typewritermc.processors.entry.DataBlueprint
import com.typewritermc.processors.entry.DataBlueprint.ObjectBlueprint
import com.typewritermc.processors.entry.DataBlueprint.PrimitiveBlueprint
import com.typewritermc.processors.entry.PrimitiveType
import com.typewritermc.processors.entry.whenClassNameIs
import kotlinx.serialization.json.JsonElement
import kotlinx.serialization.json.JsonObject
import kotlinx.serialization.json.JsonPrimitive

object SkinEditor : CustomEditor {
    override val id: String = "skin"

    override fun accept(type: KSType): Boolean {
        return type whenClassNameIs "com.typewritermc.engine.paper.entry.entity.SkinProperty"
    }

    context(KSPLogger) override fun default(type: KSType): JsonElement {
        return JsonObject(
            mapOf(
                "texture" to JsonPrimitive(""),
                "signature" to JsonPrimitive(""),
            )
        )
    }

    context(KSPLogger) override fun shape(type: KSType): DataBlueprint {
        return ObjectBlueprint(
            mapOf(
                "texture" to PrimitiveBlueprint(PrimitiveType.STRING),
                "signature" to PrimitiveBlueprint(PrimitiveType.STRING),
            )
        )
    }
}