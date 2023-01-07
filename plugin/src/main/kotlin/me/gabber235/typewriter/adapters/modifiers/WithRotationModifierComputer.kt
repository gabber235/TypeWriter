package me.gabber235.typewriter.adapters.modifiers

import me.gabber235.typewriter.Typewriter
import me.gabber235.typewriter.adapters.*

@Target(AnnotationTarget.FIELD, AnnotationTarget.PROPERTY, AnnotationTarget.VALUE_PARAMETER)
annotation class WithRotation

object WithRotationModifierComputer : StaticModifierComputer<WithRotation> {
	override val annotationClass: Class<WithRotation> = WithRotation::class.java

	override fun computeModifier(annotation: WithRotation, info: FieldInfo): FieldModifier? {
		// If the field is wrapped in a list or other container we try if the inner type can be modified
		innerCompute(annotation, info)?.let { return it }

		if (info !is CustomField) {
			Typewriter.plugin.logger.warning("WithRotation annotation can only be used on locations (including in lists or maps)!")
			return null
		}
		if (info.editor != "location") {
			Typewriter.plugin.logger.warning("WithRotation annotation can only be used on locations (including in lists or maps)!")
			return null
		}

		return FieldModifier.StaticModifier("with_rotation")
	}
}