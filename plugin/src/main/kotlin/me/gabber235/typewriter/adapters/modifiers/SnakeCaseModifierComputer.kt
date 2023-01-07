package me.gabber235.typewriter.adapters.modifiers

import me.gabber235.typewriter.Typewriter.Companion.plugin
import me.gabber235.typewriter.adapters.*
import me.gabber235.typewriter.adapters.FieldModifier.StaticModifier


@Target(AnnotationTarget.FIELD, AnnotationTarget.PROPERTY, AnnotationTarget.VALUE_PARAMETER)
annotation class SnakeCase

object SnakeCaseModifierComputer : StaticModifierComputer<SnakeCase> {
	override val annotationClass: Class<SnakeCase> = SnakeCase::class.java

	override fun computeModifier(annotation: SnakeCase, info: FieldInfo): FieldModifier? {
		// If the field is wrapped in a list or other container we try if the inner type can be modified
		innerCompute(annotation, info)?.let { return it }

		if (info !is PrimitiveField) {
			plugin.logger.warning("SnakeCase annotation can only be used on strings (including in lists or maps)!")
			return null
		}
		if (info.type != PrimitiveFieldType.STRING) {
			plugin.logger.warning("SnakeCase annotation can only be used on strings (including in lists or maps)!")
			return null
		}

		return StaticModifier("snake_case")
	}
}