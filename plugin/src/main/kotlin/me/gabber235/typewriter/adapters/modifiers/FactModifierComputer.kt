package me.gabber235.typewriter.adapters.modifiers

import me.gabber235.typewriter.adapters.*
import me.gabber235.typewriter.adapters.FieldModifier.StaticModifier

@Target(AnnotationTarget.FIELD, AnnotationTarget.PROPERTY)
annotation class Fact


object FactModifierComputer : StaticModifierComputer<Fact> {
	override val annotationClass: Class<Fact> = Fact::class.java

	override fun computeModifier(annotation: Fact, info: FieldInfo): FieldModifier? {
		if (info !is PrimitiveField) return null
		if (info.type != PrimitiveFieldType.STRING) return null

		return StaticModifier("fact")
	}
}