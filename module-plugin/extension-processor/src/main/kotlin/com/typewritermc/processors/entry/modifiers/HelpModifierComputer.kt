package com.typewritermc.processors.entry.modifiers

import com.typewritermc.core.extension.annotations.Help
import com.typewritermc.core.utils.ok
import com.typewritermc.processors.entry.DataBlueprint
import com.typewritermc.processors.entry.DataModifier
import com.typewritermc.processors.entry.DataModifierComputer
import kotlin.reflect.KClass

object HelpModifierComputer : DataModifierComputer<Help> {
    override val annotationClass: KClass<Help> = Help::class

    override fun compute(blueprint: DataBlueprint, annotation: Help): Result<DataModifier> {
        innerCompute(blueprint, annotation)?.let { return ok(it) }

        return ok(DataModifier.Modifier("help", annotation.text))
    }
}