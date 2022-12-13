package me.gabber235.typewriter.adapters.modifiers

import me.gabber235.typewriter.Typewriter.Companion.plugin
import me.gabber235.typewriter.adapters.*
import me.gabber235.typewriter.adapters.FieldModifier.InnerListModifier
import me.gabber235.typewriter.adapters.FieldModifier.StaticModifier


@Target(AnnotationTarget.FIELD, AnnotationTarget.PROPERTY)
annotation class SnakeCase()

object SnakeCaseModifierComputer : StaticModifierComputer<SnakeCase> {
	override val annotationClass: Class<SnakeCase> = SnakeCase::class.java

	override fun computeModifier(annotation: SnakeCase, info: FieldInfo): FieldModifier? {
		// If the field is a list, we try if its child fits.
		if (info is ListField) {
			val modifier = computeModifier(annotation, info.type)
			return modifier?.let { InnerListModifier(it) }
		}
		if (info !is PrimitiveField) {
			plugin.logger.warning("SnakeCase annotation can only be used on strings or lists of strings")
			return null
		}
		if (info.type != PrimitiveFieldType.STRING) {
			plugin.logger.warning("SnakeCase annotation can only be used on strings or lists of strings")
			return null
		}

		return StaticModifier("snake_case")
	}
}