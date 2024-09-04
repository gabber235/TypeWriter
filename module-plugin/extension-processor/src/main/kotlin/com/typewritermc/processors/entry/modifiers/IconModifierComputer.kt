package com.typewritermc.processors.entry.modifiers

import com.typewritermc.core.extension.annotations.Icon
import com.typewritermc.core.utils.ok
import com.typewritermc.processors.entry.*
import kotlin.reflect.KClass

object IconModifierComputer : DataModifierComputer<Icon> {
    override val annotationClass: KClass<Icon> = Icon::class

    override fun compute(blueprint: DataBlueprint, annotation: Icon): Result<DataModifier> {
        innerCompute(blueprint, annotation)?.let { return ok(it) }
        return ok(DataModifier.Modifier("icon", annotation.icon))
    }
}