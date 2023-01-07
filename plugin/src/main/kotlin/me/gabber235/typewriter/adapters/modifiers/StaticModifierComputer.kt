package me.gabber235.typewriter.adapters.modifiers

import me.gabber235.typewriter.adapters.*
import java.lang.reflect.Field
import kotlin.reflect.full.*


interface StaticModifierComputer<A : Annotation> : ModifierComputer {
	val annotationClass: Class<A>
	fun computeModifier(annotation: A, info: FieldInfo): FieldModifier?

	override fun compute(field: Field, info: FieldInfo): FieldModifier? {
		val annotation = field.getAnnotation(annotationClass)
		if (annotation != null) {
			return computeModifier(annotation, info)
		}

		// If the annotation is not present on the field, check if it is present on primary constructor parameter
		field.declaringClass.kotlin.primaryConstructor?.findParameterByName(field.name)
			?.findAnnotations(annotationClass.kotlin)?.firstOrNull()?.let {
				return computeModifier(it, info)
			}

		// We try to find the interface property and see if it has the annotation
		return field.declaringClass.interfaces.asSequence().mapNotNull {
			it.kotlin.memberProperties.firstOrNull { prop -> prop.name == field.name }
		}.firstOrNull()?.findAnnotations(annotationClass.kotlin)?.firstOrNull()?.let {
			computeModifier(it, info)
		}
	}

	/**
	 * Checks if the given field is a list and tries to compute the modifier for the list's type.
	 * This can be called inside the [compute] method.
	 *
	 * @param annotation the annotation that was found on the field
	 * @param info The info of the field
	 */
	fun innerComputeForList(annotation: A, info: FieldInfo): FieldModifier? {
		if (info !is ListField) return null
		return computeModifier(annotation, info.type)
	}

	/**
	 * Checks if the given field is a map and tries to compute the modifier for the map's key and value.
	 * This can be called inside the [compute] method.
	 *
	 * @param annotation the annotation that was found on the field
	 * @param info The info of the field
	 */
	fun innerComputeForMap(annotation: A, info: FieldInfo): FieldModifier? {
		if (info !is MapField) return null
		return FieldModifier.InnerMapModifier(
			computeModifier(annotation, info.key),
			computeModifier(annotation, info.value)
		)
	}

	/**
	 * Checks if the given field is a custom field and tries to compute the modifier for the custom field's type.
	 * This can be called inside the [compute] method.
	 *
	 * @param annotation the annotation that was found on the field
	 * @param info The info of the field
	 */
	fun innerComputeForCustom(annotation: A, info: FieldInfo): FieldModifier? {
		if (info !is CustomField) return null
		val customInfo = info.fieldInfo ?: return null
		return computeModifier(annotation, customInfo)
	}

	/**
	 * Checks if the given field is a list, map or custom field and tries to compute the modifier for the field's type.
	 * This can be called inside the [compute] method.
	 *
	 * @param annotation the annotation that was found on the field
	 * @param info The info of the field
	 */
	fun innerCompute(annotation: A, info: FieldInfo): FieldModifier? {
		return innerComputeForList(annotation, info)
			?: innerComputeForMap(annotation, info)
			?: innerComputeForCustom(annotation, info)
	}

}