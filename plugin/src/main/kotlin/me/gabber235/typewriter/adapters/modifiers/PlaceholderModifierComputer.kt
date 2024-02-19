package me.gabber235.typewriter.adapters.modifiers

import me.gabber235.typewriter.adapters.FieldInfo
import me.gabber235.typewriter.adapters.FieldModifier
import me.gabber235.typewriter.adapters.PrimitiveField
import me.gabber235.typewriter.adapters.PrimitiveFieldType
import me.gabber235.typewriter.utils.failure
import me.gabber235.typewriter.utils.ok


@Target(AnnotationTarget.FIELD, AnnotationTarget.PROPERTY, AnnotationTarget.VALUE_PARAMETER)
annotation class Placeholder

object PlaceholderModifierComputer : StaticModifierComputer<Placeholder> {
    override val annotationClass: Class<Placeholder> = Placeholder::class.java

    override fun computeModifier(annotation: Placeholder, info: FieldInfo): Result<FieldModifier?> {
        // If the field is wrapped in a list or other container we try if the inner type can be modified
        innerCompute(annotation, info)?.let { return ok(it) }

        if (info !is PrimitiveField) {
            return failure("Placeholder annotation can only be used on strings (including in lists or maps)!")
        }

        if (info.type != PrimitiveFieldType.STRING) {
            return failure("Placeholder annotation can only be used on strings (including in lists or maps)!")
        }

        return ok(FieldModifier.StaticModifier("placeholder"))
    }
}