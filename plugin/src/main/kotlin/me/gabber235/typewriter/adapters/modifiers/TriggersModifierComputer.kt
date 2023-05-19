package me.gabber235.typewriter.adapters.modifiers

import me.gabber235.typewriter.adapters.*
import me.gabber235.typewriter.adapters.FieldModifier.InnerListModifier
import me.gabber235.typewriter.adapters.FieldModifier.StaticModifier
import me.gabber235.typewriter.logger

@Target(AnnotationTarget.PROPERTY, AnnotationTarget.FIELD, AnnotationTarget.VALUE_PARAMETER)
annotation class Triggers

object TriggersModifierComputer : StaticModifierComputer<Triggers> {
    override val annotationClass: Class<Triggers> = Triggers::class.java

    override fun computeModifier(annotation: Triggers, info: FieldInfo): FieldModifier? {
        if (info !is ListField) {
            logger.warning("Triggers annotation can only be used on lists")
            return null
        }
        if (info.type !is PrimitiveField) {
            logger.warning("Triggers annotation can only be used on lists of strings")
            return null
        }
        if (info.type.type != PrimitiveFieldType.STRING) {
            logger.warning("Triggers annotation can only be used on lists of strings")
            return null
        }

        return InnerListModifier(StaticModifier("trigger"))
    }
}