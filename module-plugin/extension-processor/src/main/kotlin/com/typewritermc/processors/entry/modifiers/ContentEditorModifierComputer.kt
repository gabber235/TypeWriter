package com.typewritermc.processors.entry.modifiers

import com.typewritermc.core.extension.annotations.ContentEditor
import com.typewritermc.core.utils.failure
import com.typewritermc.core.utils.ok
import com.typewritermc.processors.annotationClassValue
import com.typewritermc.processors.entry.DataBlueprint
import com.typewritermc.processors.entry.DataModifier
import com.typewritermc.processors.entry.DataModifierComputer
import com.typewritermc.processors.fullName
import kotlin.reflect.KClass

object ContentEditorModifierComputer : DataModifierComputer<ContentEditor> {
    override val annotationClass: KClass<ContentEditor> = ContentEditor::class

    override fun compute(blueprint: DataBlueprint, annotation: ContentEditor): Result<DataModifier> {
        innerCompute(blueprint, annotation)?.let { return ok(it) }

        val contentMode = annotation.annotationClassValue { capturer }
        val className = contentMode.declaration.qualifiedName?.asString()
            ?: return failure("ContentEditor ${contentMode.fullName} does not have a qualified name! It must be a non-local non-anonymous class.")

        return ok(DataModifier.Modifier("contentMode", className))
    }
}