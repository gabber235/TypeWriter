package com.typewritermc.processors.entry.modifiers

import com.typewritermc.core.extension.annotations.Placeholder
import com.typewritermc.core.utils.failure
import com.typewritermc.core.utils.ok
import com.typewritermc.processors.entry.DataBlueprint
import com.typewritermc.processors.entry.DataModifier
import com.typewritermc.processors.entry.DataModifierComputer
import com.typewritermc.processors.entry.PrimitiveType
import kotlin.reflect.KClass

object PlaceholderModifierComputer : DataModifierComputer<Placeholder> {
    override val annotationClass: KClass<Placeholder> = Placeholder::class

    override fun compute(blueprint: DataBlueprint, annotation: Placeholder): Result<DataModifier> {
        // If the field is wrapped in a list or other container we try if the inner type can be modified
        innerCompute(blueprint, annotation)?.let { return ok(it) }

        if (blueprint !is DataBlueprint.PrimitiveBlueprint) {
            return failure("Placeholder annotation can only be used on strings (including in lists or maps)!")
        }

        if (blueprint.type != PrimitiveType.STRING) {
            return failure("Placeholder annotation can only be used on strings (including in lists or maps)!")
        }

        return ok(DataModifier.Modifier("placeholder"))
    }
}