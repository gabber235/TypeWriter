package com.typewritermc.processors.entry.modifiers

import com.typewritermc.core.extension.annotations.OnlyTags
import com.typewritermc.core.utils.failure
import com.typewritermc.core.utils.ok
import com.typewritermc.processors.entry.DataBlueprint
import com.typewritermc.processors.entry.DataModifier
import com.typewritermc.processors.entry.DataModifierComputer
import kotlin.reflect.KClass

object OnlyTagsModifierComputer : DataModifierComputer<OnlyTags> {
    override val annotationClass: KClass<OnlyTags> = OnlyTags::class

    override fun compute(blueprint: DataBlueprint, annotation: OnlyTags): Result<DataModifier> {
        // If the field is wrapped in a list or other container, we try if the inner type can be modified
        innerCompute(blueprint, annotation)?.let { return ok(it) }

        if (blueprint !is DataBlueprint.CustomBlueprint) {
            return failure("OnlyTags annotation can only be used on Refs (including in lists or maps)!")
        }

        if (blueprint.editor != "entryReference") {
            return failure("OnlyTags annotation can only be used on Refs (including in lists or maps)!")
        }

        return ok(DataModifier.Modifier("only_tags", annotation.tags.joinToString(",")))
    }
}