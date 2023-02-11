package me.gabber235.typewriter.adapters.modifiers

import me.gabber235.typewriter.Typewriter.Companion.plugin
import me.gabber235.typewriter.adapters.*

@Target(AnnotationTarget.PROPERTY, AnnotationTarget.FIELD, AnnotationTarget.VALUE_PARAMETER)
annotation class Sound

object SoundModifierComputer : StaticModifierComputer<Sound> {
	override val annotationClass: Class<Sound> = Sound::class.java

	override fun computeModifier(annotation: Sound, info: FieldInfo): FieldModifier? {
		// If the field is wrapped in a list or other container we try if the inner type can be modified
		innerCompute(annotation, info)?.let { return it }

		if (info !is PrimitiveField) {
			plugin.logger.warning("Sound annotation can only be used on strings")
			return null
		}
		if (info.type != PrimitiveFieldType.STRING) {
			plugin.logger.warning("Sound annotation can only be used on strings")
			return null
		}

		return FieldModifier.StaticModifier("sound")
	}
}