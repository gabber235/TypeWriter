package me.gabber235.typewriter.adapters.modifiers

import com.google.gson.JsonObject
import me.gabber235.typewriter.adapters.*
import me.gabber235.typewriter.adapters.FieldModifier.DynamicModifier
import me.gabber235.typewriter.adapters.FieldModifier.InnerListModifier
import me.gabber235.typewriter.utils.failure
import me.gabber235.typewriter.utils.ok

@Target(AnnotationTarget.PROPERTY, AnnotationTarget.FIELD, AnnotationTarget.VALUE_PARAMETER)
annotation class Segments(val color: String = Colors.CYAN, val icon: String = "fa6-solid:star")

object SegmentModifierComputer : StaticModifierComputer<Segments> {
    override val annotationClass: Class<Segments> = Segments::class.java

    override fun computeModifier(annotation: Segments, info: FieldInfo): Result<FieldModifier?> {
        if (info !is ListField) {
            return failure("Segment annotation can only be used on lists")
        }
        val type = info.type
        if (type !is ObjectField) {
            return failure("Segment annotation can only be used on lists of objects")
        }
        val startFrame = type.fields["startFrame"]
        val endFrame = type.fields["endFrame"]
        if (startFrame !is PrimitiveField || endFrame !is PrimitiveField) {
            return failure("Segment annotation can only be used on lists of objects with startFrame and endFrame fields")
        }

        if (startFrame.type != PrimitiveFieldType.INTEGER || endFrame.type != PrimitiveFieldType.INTEGER) {
            return failure("Segment annotation can only be used on lists of objects with startFrame and endFrame fields of type int")
        }

        val color = annotation.color
        val icon = annotation.icon

        val data = JsonObject().apply {
            addProperty("color", color)
            addProperty("icon", icon)
        }

        return ok(InnerListModifier(DynamicModifier("segment", data)))
    }
}

