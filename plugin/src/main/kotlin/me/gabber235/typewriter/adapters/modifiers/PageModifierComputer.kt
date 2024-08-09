package me.gabber235.typewriter.adapters.modifiers

import me.gabber235.typewriter.adapters.FieldInfo
import me.gabber235.typewriter.adapters.FieldModifier
import me.gabber235.typewriter.adapters.PrimitiveField
import me.gabber235.typewriter.adapters.PrimitiveFieldType
import me.gabber235.typewriter.entry.PageType
import me.gabber235.typewriter.utils.failure
import me.gabber235.typewriter.utils.ok

@Target(AnnotationTarget.PROPERTY, AnnotationTarget.FIELD, AnnotationTarget.VALUE_PARAMETER)
annotation class Page(val type: PageType)

object PageModifierComputer : StaticModifierComputer<Page> {
    override val annotationClass: Class<Page> = Page::class.java

    override fun computeModifier(annotation: Page, info: FieldInfo): Result<FieldModifier?> {
        innerCompute(annotation, info)?.let { return ok(it) }

        if (info !is PrimitiveField) {
            return failure("Page annotation can only be used on primitive fields")
        }

        if (info.type != PrimitiveFieldType.STRING) {
            return failure("Page annotation can only be used on string fields")
        }

        return ok(FieldModifier.DynamicModifier("page", annotation.type.id))
    }
}