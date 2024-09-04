package com.typewritermc.processors.entry.editors

import com.google.devtools.ksp.processing.KSPLogger
import com.google.devtools.ksp.symbol.KSClassDeclaration
import com.google.devtools.ksp.symbol.KSType
import com.typewritermc.processors.entry.*
import com.typewritermc.processors.fullName
import kotlinx.serialization.json.JsonElement
import kotlinx.serialization.json.JsonObject

object ItemEditor : CustomEditor {
    override val id: String = "item"

    override fun accept(type: KSType): Boolean {
        return type whenClassNameIs "com.typewritermc.engine.paper.utils.Item"
    }

    context(KSPLogger) override fun default(type: KSType): JsonElement {
        val klass = type.declaration as? KSClassDeclaration
            ?: throw IllegalStateException("Expected Item to be a class, did you goof up gabber?")

        val properties = klass.getAllProperties()
            .filter { it.hasBackingField }
            .associate { prop ->
                val name = prop.simpleName.asString()
                val propertyType = prop.type.resolve()
                val blueprint =
                    DataBlueprint.blueprint(propertyType) ?: throw CouldNotFindBlueprintException(
                        prop.fullName,
                        propertyType,
                        prop.location
                    )
                val default = blueprint.default()
                name to default

            }
        return JsonObject(properties)
    }

    context(KSPLogger) override fun shape(type: KSType): DataBlueprint {
        return DataBlueprint.ObjectBlueprint.blueprint(type) ?: throw CouldNotFindBlueprintException(
            type.fullName,
            type,
            type.declaration.location
        )
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