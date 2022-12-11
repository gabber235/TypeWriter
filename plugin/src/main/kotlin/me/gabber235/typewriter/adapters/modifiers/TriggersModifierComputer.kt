package me.gabber235.typewriter.adapters.modifiers

import me.gabber235.typewriter.adapters.*
import me.gabber235.typewriter.adapters.FieldModifier.InnerListModifier

@Target(AnnotationTarget.FIELD, AnnotationTarget.PROPERTY)
annotation class Triggers(val isReceiver: Boolean = false)

object TriggersModifierComputer : StaticModifierComputer<Triggers> {
	override val annotationClass: Class<Triggers> = Triggers::class.java

	override fun computeModifier(annotation: Triggers, info: FieldInfo): FieldModifier? {
		if (info !is ListField) return null
		if (info.type !is PrimitiveField) return null
		if (info.type.type != PrimitiveFieldType.STRING) return null
		return InnerListModifier(FieldModifier.DynamicModifier("trigger", annotation.isReceiver))
	}
}