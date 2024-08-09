package me.gabber235.typewriter.adapters.modifiers

import me.gabber235.typewriter.adapters.*
import me.gabber235.typewriter.utils.ok
import java.lang.reflect.Field
import kotlin.reflect.full.findAnnotations
import kotlin.reflect.full.findParameterByName
import kotlin.reflect.full.memberProperties
import kotlin.reflect.full.primaryConstructor

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

    fun computeModifier(annotation: A, info: FieldInfo): Result<FieldModifier?>

    override fun compute(field: Field, info: FieldInfo): Result<FieldModifier?> {
        findAnnotation(field, annotationClass)?.let { annotation ->
            return computeModifier(annotation, info)
        }

        if (info !is ListField && info !is MapField && info !is ObjectField) {
            return ok(null)
        }

        innerAnnotationFinders.forEach { finder ->
            val annotation = findAnnotation(field, finder.parentAnnotationClass) ?: return@forEach
            val innerAnnotation = finder.transformer(annotation)

            if (info is ListField && finder.types.contains(InnerModifierType.LIST)) {
                computeModifier(innerAnnotation, info.type).onSuccess { modifier ->
                    return ok(modifier?.let { FieldModifier.InnerListModifier(it) })
                }
                    .onFailure { return ok(null) }
            }
            if (info is MapField && finder.types.contains(InnerModifierType.MAP)) {
                val keyModifier = computeModifier(innerAnnotation, info.key).onFailure { return ok(null) }.getOrNull()
                val valueModifier =
                    computeModifier(innerAnnotation, info.value).onFailure { return ok(null) }.getOrNull()

                return ok(FieldModifier.InnerMapModifier(keyModifier, valueModifier))
            }
            if (info is ObjectField && finder.types.contains(InnerModifierType.OBJECT)) {
                computeModifier(innerAnnotation, info).onSuccess { modifier ->
                    return ok(modifier?.let { FieldModifier.InnerCustomModifier(it) })
                }
                    .onFailure { return ok(null) }
            }
        }
        return ok(null)
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
        val modifier = computeModifier(annotation, info.type).getOrNull() ?: return null
        return FieldModifier.InnerListModifier(modifier)
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
        val keyModifier = computeModifier(annotation, info.key).getOrNull()
        val valueModifier = computeModifier(annotation, info.value).getOrNull()

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
        val modifier = computeModifier(annotation, customInfo).getOrNull() ?: return null
        return FieldModifier.InnerCustomModifier(modifier)
    }

    /**
     * Checks if the given field is a list, map or custom field and tries to compute the modifier for the field's type.
     * This can be called inside the [compute] method.
     *
     * @param annotation the annotation that was found on the field
     * @param info The info of the field
     */
    fun innerCompute(annotation: A, info: FieldInfo): FieldModifier? {
        return when (info) {
            is ListField -> innerComputeForList(annotation, info)
            is MapField -> innerComputeForMap(annotation, info)
            is CustomField -> innerComputeForCustom(annotation, info)
            else -> null
        }
    }

}