package me.gabber235.typewriter.adapters.modifiers

import me.gabber235.typewriter.adapters.*
import java.lang.reflect.Field
import kotlin.reflect.full.*

enum class InnerModifierType {
	LIST,
	MAP,
	OBJECT,
}

data class InnerAnnotationFinder<BaseAnnotation : Annotation, ParentAnnotation : Annotation>(
	val parentAnnotationClass: Class<ParentAnnotation>,
	val transformer: (ParentAnnotation) -> BaseAnnotation,
	val types: List<InnerModifierType> = listOf(
		InnerModifierType.LIST,
		InnerModifierType.MAP,
		InnerModifierType.OBJECT
	),
)

interface StaticModifierComputer<A : Annotation> : ModifierComputer {
	val annotationClass: Class<A>
	val innerAnnotationFinders: List<InnerAnnotationFinder<A, Annotation>> get() = emptyList()

	fun computeModifier(annotation: A, info: FieldInfo): FieldModifier?

	override fun compute(field: Field, info: FieldInfo): FieldModifier? {
		findAnnotation(field, annotationClass)?.let { annotation ->
			return computeModifier(annotation, info)
		}

		if (info !is ListField && info !is MapField && info !is ObjectField) {
			return null
		}

		innerAnnotationFinders.forEach { finder ->
			val annotation = findAnnotation(field, finder.parentAnnotationClass) ?: return@forEach
			val innerAnnotation = finder.transformer(annotation)

			if (info is ListField && finder.types.contains(InnerModifierType.LIST)) {
				computeModifier(innerAnnotation, info.type)?.let { return FieldModifier.InnerListModifier(it) }
			}
			if (info is MapField && finder.types.contains(InnerModifierType.MAP)) {
				val keyModifier = computeModifier(innerAnnotation, info.key)
				val valueModifier = computeModifier(innerAnnotation, info.value)

				return FieldModifier.InnerMapModifier(keyModifier, valueModifier)
			}
			if (info is ObjectField && finder.types.contains(InnerModifierType.OBJECT)) {
				computeModifier(innerAnnotation, info)?.let { return FieldModifier.InnerCustomModifier(it) }
			}
		}
		return null
	}

	fun <T : Annotation> findAnnotation(field: Field, annotationClass: Class<T>): T? {
		val annotation = field.getAnnotation(annotationClass)
		if (annotation != null) {
			return annotation
		}

		// If the annotation is not present on the field, check if it is present on primary constructor parameter
		field.declaringClass.kotlin.primaryConstructor?.findParameterByName(field.name)
			?.findAnnotations(annotationClass.kotlin)?.firstOrNull()?.let {
				return it
			}

		// We try to find the interface property and see if it has the annotation
		return field.declaringClass.interfaces.asSequence().mapNotNull {
			it.kotlin.memberProperties.firstOrNull { prop -> prop.name == field.name }
		}.firstOrNull()?.findAnnotations(annotationClass.kotlin)?.firstOrNull()
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
		return computeModifier(annotation, info.type)?.let { FieldModifier.InnerListModifier(it) }
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
		val keyModifier = computeModifier(annotation, info.key)
		val valueModifier = computeModifier(annotation, info.value)

		if (keyModifier == null && valueModifier == null) return null
		return FieldModifier.InnerMapModifier(keyModifier, valueModifier)
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
		return computeModifier(annotation, customInfo)?.let { FieldModifier.InnerCustomModifier(it) }
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