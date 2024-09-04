package com.typewritermc.processors.entry.modifiers

import com.typewritermc.core.extension.annotations.SnakeCase
import com.typewritermc.core.utils.failure
import com.typewritermc.core.utils.ok
import com.typewritermc.processors.entry.*
import kotlin.reflect.KClass

object SnakeCaseModifierComputer : DataModifierComputer<SnakeCase> {
    override val annotationClass: KClass<SnakeCase> = SnakeCase::class

    override fun compute(blueprint: DataBlueprint, annotation: SnakeCase): Result<DataModifier> {
        // If the field is wrapped in a list or other container we try if the inner type can be modified
        innerCompute(blueprint, annotation)?.let { return ok(it) }

        if (blueprint !is DataBlueprint.PrimitiveBlueprint) {
            return failure("SnakeCase annotation can only be used on strings (including in lists or maps)!")
        }
        if (blueprint.type != PrimitiveType.STRING) {
            return failure("SnakeCase annotation can only be used on strings (including in lists or maps)!")
        }

        return ok(DataModifier.Modifier("snake_case"))
    }
}