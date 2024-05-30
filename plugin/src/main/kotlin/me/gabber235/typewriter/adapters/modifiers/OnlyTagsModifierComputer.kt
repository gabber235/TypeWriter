package me.gabber235.typewriter.adapters.modifiers

import me.gabber235.typewriter.adapters.CustomField
import me.gabber235.typewriter.adapters.FieldInfo
import me.gabber235.typewriter.adapters.FieldModifier
import me.gabber235.typewriter.utils.failure
import me.gabber235.typewriter.utils.ok

@Target(AnnotationTarget.PROPERTY, AnnotationTarget.FIELD, AnnotationTarget.VALUE_PARAMETER)
annotation class OnlyTags(vararg val tags: String)

object OnlyTagsModifierComputer : StaticModifierComputer<OnlyTags> {
    override val annotationClass: Class<OnlyTags> = OnlyTags::class.java

    override fun computeModifier(annotation: OnlyTags, info: FieldInfo): Result<FieldModifier?> {
        // If the field is wrapped in a list or other container, we try if the inner type can be modified
        innerCompute(annotation, info)?.let { return ok(it) }

        if (info !is CustomField) {
            return failure("OnlyTags annotation can only be used on Refs (including in lists or maps)!")
        }

        if (info.editor != "entryReference") {
            return failure("OnlyTags annotation can only be used on Refs (including in lists or maps)!")
        }

        return ok(FieldModifier.DynamicModifier("only_tags", annotation.tags.joinToString(",")))
    }
}