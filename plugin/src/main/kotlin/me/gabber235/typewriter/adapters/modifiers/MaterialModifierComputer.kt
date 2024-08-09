package me.gabber235.typewriter.adapters.modifiers

import me.gabber235.typewriter.adapters.CustomField
import me.gabber235.typewriter.adapters.FieldInfo
import me.gabber235.typewriter.adapters.FieldModifier
import me.gabber235.typewriter.utils.failure
import me.gabber235.typewriter.utils.ok

enum class MaterialProperty {
    ITEM,
    BLOCK,
    SOLID,
    TRANSPARENT,
    FLAMMABLE,
    BURNABLE,
    EDIBLE,
    FUEL,
    INTRACTABLE,
    OCCLUDING,
    RECORD,
    TOOL,
    WEAPON,
    ARMOR,
    ORE,
}

@Target(AnnotationTarget.FIELD, AnnotationTarget.VALUE_PARAMETER, AnnotationTarget.PROPERTY)
annotation class MaterialProperties(vararg val properties: MaterialProperty)


object MaterialPropertiesModifierComputer : StaticModifierComputer<MaterialProperties> {
    override val annotationClass: Class<MaterialProperties> = MaterialProperties::class.java

    override fun computeModifier(annotation: MaterialProperties, info: FieldInfo): Result<FieldModifier?> {
        // If the field is wrapped in a list or other container, we try if the inner type can be modified
        innerCompute(annotation, info)?.let { return ok(it) }

        if (info !is CustomField) {
            return failure("MaterialProperties annotation can only be used on custom fields")
        }
        if (info.editor != "material") {
            return failure("MaterialProperties annotation can only be used materials (not on ${info.editor})")
        }

        return ok(FieldModifier.DynamicModifier(
            "material_properties",
            annotation.properties.joinToString(";") { it.name.lowercase() }
        ))
    }
}