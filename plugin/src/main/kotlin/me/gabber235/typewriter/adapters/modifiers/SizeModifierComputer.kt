package me.gabber235.typewriter.adapters.modifiers

import me.gabber235.typewriter.adapters.FieldInfo
import me.gabber235.typewriter.adapters.FieldModifier
import me.gabber235.typewriter.utils.ok

@Target(AnnotationTarget.PROPERTY, AnnotationTarget.FIELD, AnnotationTarget.VALUE_PARAMETER)
annotation class Min(val value: Int)

@Target(AnnotationTarget.PROPERTY, AnnotationTarget.FIELD, AnnotationTarget.VALUE_PARAMETER)
annotation class InnerMin(val annotation: Min)

@Target(AnnotationTarget.PROPERTY, AnnotationTarget.FIELD, AnnotationTarget.VALUE_PARAMETER)
annotation class Max(val value: Int)

@Target(AnnotationTarget.PROPERTY, AnnotationTarget.FIELD, AnnotationTarget.VALUE_PARAMETER)
annotation class InnerMax(val annotation: Max)

object MinModifierComputer : StaticModifierComputer<Min> {
    override val annotationClass: Class<Min> = Min::class.java

    override val innerAnnotationFinders: List<InnerAnnotationFinder<Min, Annotation>> = listOf(
        InnerAnnotationFinder(InnerMin::class.java, InnerMin::annotation)
    ) as List<InnerAnnotationFinder<Min, Annotation>>


    override fun computeModifier(annotation: Min, info: FieldInfo): Result<FieldModifier?> {
        return ok(FieldModifier.DynamicModifier("min", annotation.value))
    }
}

object MaxModifierComputer : StaticModifierComputer<Max> {
    override val annotationClass: Class<Max> = Max::class.java

    override val innerAnnotationFinders: List<InnerAnnotationFinder<Max, Annotation>> = listOf(
        InnerAnnotationFinder(InnerMax::class.java, InnerMax::annotation)
    ) as List<InnerAnnotationFinder<Max, Annotation>>

    override fun computeModifier(annotation: Max, info: FieldInfo): Result<FieldModifier?> {
        return ok(FieldModifier.DynamicModifier("max", annotation.value))
    }
}