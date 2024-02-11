package me.gabber235.typewriter.adapters.modifiers

import com.google.gson.JsonObject
import me.gabber235.typewriter.adapters.*
import me.gabber235.typewriter.adapters.FieldModifier.DynamicModifier
import me.gabber235.typewriter.adapters.FieldModifier.InnerListModifier
import me.gabber235.typewriter.logger

@Target(AnnotationTarget.PROPERTY, AnnotationTarget.FIELD, AnnotationTarget.VALUE_PARAMETER)
annotation class Segments(val color: String = Colors.CYAN, val icon: String = "fa6-solid:star")

object SegmentModifierComputer : StaticModifierComputer<Segments> {
    override val annotationClass: Class<Segments> = Segments::class.java

    override fun computeModifier(annotation: Segments, info: FieldInfo): FieldModifier? {
        if (info !is ListField) {
            logger.warning("Segment annotation can only be used on lists")
            return null
        }
        val type = info.type
        if (type !is ObjectField) {
            logger.warning("Segment annotation can only be used on lists of objects")
            return null
        }
        val startFrame = type.fields["startFrame"]
        val endFrame = type.fields["endFrame"]
        if (startFrame !is PrimitiveField || endFrame !is PrimitiveField) {
            logger.warning("Segment annotation can only be used on lists of objects with startFrame and endFrame fields")
            return null
        }

        if (startFrame.type != PrimitiveFieldType.INTEGER || endFrame.type != PrimitiveFieldType.INTEGER) {
            logger.warning("Segment annotation can only be used on lists of objects with startFrame and endFrame fields of type int")
            return null
        }

        val color = annotation.color
        val icon = annotation.icon

        val data = JsonObject().apply {
            addProperty("color", color)
            addProperty("icon", icon)
        }

        return InnerListModifier(DynamicModifier("segment", data))
    }
}

