package me.gabber235.typewriter.adapters.modifiers

import me.gabber235.typewriter.adapters.FieldInfo
import me.gabber235.typewriter.adapters.FieldModifier
import me.gabber235.typewriter.content.ContentMode
import me.gabber235.typewriter.utils.failure
import me.gabber235.typewriter.utils.ok
import kotlin.reflect.KClass
import kotlin.reflect.jvm.jvmName

@Target(AnnotationTarget.FIELD, AnnotationTarget.VALUE_PARAMETER, AnnotationTarget.PROPERTY)
annotation class ContentEditor(val capturer: KClass<out ContentMode>)

object ContentEditorModifierComputer : StaticModifierComputer<ContentEditor> {
    override val annotationClass: Class<ContentEditor> = ContentEditor::class.java

    override fun computeModifier(annotation: ContentEditor, info: FieldInfo): Result<FieldModifier?> {
        val contentMode = annotation.capturer
        val name = contentMode.qualifiedName
            ?: return failure("ContentEditor ${contentMode.jvmName} does not have a qualified name! It must be a non-local non-anonymous class.")

        return ok(FieldModifier.DynamicModifier("contentMode", name))
    }
}