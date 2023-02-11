package me.gabber235.typewriter.adapters.modifiers

import me.gabber235.typewriter.adapters.FieldInfo
import me.gabber235.typewriter.adapters.FieldModifier
import me.gabber235.typewriter.adapters.FieldModifier.DynamicModifier

@Target(AnnotationTarget.FIELD, AnnotationTarget.PROPERTY, AnnotationTarget.VALUE_PARAMETER)
annotation class Help(val text: String)

object HelpModifierComputer : StaticModifierComputer<Help> {
	override val annotationClass: Class<Help> = Help::class.java

	override fun computeModifier(annotation: Help, info: FieldInfo): FieldModifier? {
		// If the field is wrapped in a list or other container we try if the inner type can be modified
		innerCompute(annotation, info)?.let { return it }

		return DynamicModifier("help", annotation.text)
	}
}
