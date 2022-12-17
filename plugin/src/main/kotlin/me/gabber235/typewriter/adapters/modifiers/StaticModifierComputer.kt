package me.gabber235.typewriter.adapters.modifiers

import me.gabber235.typewriter.adapters.*
import java.lang.reflect.Field
import kotlin.reflect.full.findAnnotations
import kotlin.reflect.full.memberProperties


interface StaticModifierComputer<A : Annotation> : ModifierComputer {
	val annotationClass: Class<A>
	fun computeModifier(annotation: A, info: FieldInfo): FieldModifier?

	override fun compute(field: Field, info: FieldInfo): FieldModifier? {
		val annotation = field.getAnnotation(annotationClass)
		if (annotation != null) {
			return computeModifier(annotation, info)
		}

		// We try to find the interface property and see if it has the annotation
		val innerField = field.declaringClass.interfaces.asSequence().mapNotNull {
			it.kotlin.memberProperties.firstOrNull { prop -> prop.name == field.name }
		}.firstOrNull()

		return innerField?.findAnnotations(annotationClass.kotlin)?.firstOrNull()?.let {
			computeModifier(it, info)
		}
	}
}