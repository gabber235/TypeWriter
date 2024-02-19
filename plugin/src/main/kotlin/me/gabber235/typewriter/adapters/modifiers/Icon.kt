package me.gabber235.typewriter.adapters.modifiers

import me.gabber235.typewriter.adapters.FieldInfo
import me.gabber235.typewriter.adapters.FieldModifier
import me.gabber235.typewriter.utils.ok

@Target(AnnotationTarget.PROPERTY, AnnotationTarget.FIELD, AnnotationTarget.VALUE_PARAMETER)
annotation class Icon(val icon: String)

object IconModifierComputer : StaticModifierComputer<Icon> {
    override val annotationClass: Class<Icon> = Icon::class.java

    override fun computeModifier(annotation: Icon, info: FieldInfo): Result<FieldModifier?> {
        return ok(FieldModifier.DynamicModifier("icon", annotation.icon))
    }
}