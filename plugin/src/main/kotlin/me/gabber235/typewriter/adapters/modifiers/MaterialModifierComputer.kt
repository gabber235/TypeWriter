package me.gabber235.typewriter.adapters.modifiers

import me.gabber235.typewriter.Typewriter
import me.gabber235.typewriter.adapters.*

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

	override fun computeModifier(annotation: MaterialProperties, info: FieldInfo): FieldModifier? {
		// If the field is wrapped in a list or other container we try if the inner type can be modified
		innerCompute(annotation, info)?.let { return it }

		if (info !is CustomField) {
			Typewriter.plugin.logger.warning("MaterialProperties annotation can only be used on custom fields")
			return null
		}
		if (info.editor != "material") {
			Typewriter.plugin.logger.warning("MaterialProperties annotation can only be used materials (not on ${info.editor})")
			return null
		}

		return FieldModifier.DynamicModifier(
			"material_properties",
			annotation.properties.joinToString(";") { it.name.lowercase() }
		)
	}
}