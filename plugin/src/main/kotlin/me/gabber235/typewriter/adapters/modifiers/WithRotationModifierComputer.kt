package me.gabber235.typewriter.adapters.modifiers

import me.gabber235.typewriter.adapters.CustomField
import me.gabber235.typewriter.adapters.FieldInfo
import me.gabber235.typewriter.adapters.FieldModifier
import me.gabber235.typewriter.utils.failure
import me.gabber235.typewriter.utils.ok

@Target(AnnotationTarget.FIELD, AnnotationTarget.PROPERTY, AnnotationTarget.VALUE_PARAMETER)
annotation class WithRotation

object WithRotationModifierComputer : StaticModifierComputer<WithRotation> {
    override val annotationClass: Class<WithRotation> = WithRotation::class.java

    override fun computeModifier(annotation: WithRotation, info: FieldInfo): Result<FieldModifier?> {
        // If the field is wrapped in a list or other container, we try if the inner type can be modified
        innerCompute(annotation, info)?.let { return ok(it) }

        if (info !is CustomField) {
            return failure("WithRotation annotation can only be used on locations (including in lists or maps)!")
        }
        if (info.editor != "location") {
            return failure("WithRotation annotation can only be used on locations (including in lists or maps)!")
        }

        return ok(FieldModifier.StaticModifier("with_rotation"))
    }
}