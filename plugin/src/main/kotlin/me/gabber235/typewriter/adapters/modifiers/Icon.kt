package me.gabber235.typewriter.adapters.modifiers

import me.gabber235.typewriter.adapters.FieldInfo
import me.gabber235.typewriter.adapters.FieldModifier
import me.gabber235.typewriter.utils.Icons

@Target(AnnotationTarget.PROPERTY, AnnotationTarget.FIELD, AnnotationTarget.VALUE_PARAMETER)
annotation class Icon(val icon: Icons)

object IconModifierComputer : StaticModifierComputer<Icon> {
    override val annotationClass: Class<Icon> = Icon::class.java

    override fun computeModifier(annotation: Icon, info: FieldInfo): FieldModifier {
        return FieldModifier.DynamicModifier("icon", annotation.icon.id)
    }
}