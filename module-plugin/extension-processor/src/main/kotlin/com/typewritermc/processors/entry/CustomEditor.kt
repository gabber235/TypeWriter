package com.typewritermc.processors.entry

import com.google.devtools.ksp.processing.KSPLogger
import com.google.devtools.ksp.symbol.KSType
import com.typewritermc.processors.entry.editors.*
import kotlinx.serialization.json.JsonElement
import kotlin.reflect.KClass

interface CustomEditor {
    /// The name of the editor
    val id: String

    fun accept(type: KSType): Boolean

    context(KSPLogger)
    fun default(type: KSType): JsonElement

    context(KSPLogger)
    fun shape(type: KSType): DataBlueprint

    context(KSPLogger)
    fun validateDefault(type: KSType, default: JsonElement): Result<Unit> {
        return shape(type).validateDefault(default)
    }

    fun modifiers(type: KSType): List<DataModifier> {
        return emptyList()
    }
}

infix fun KSType.whenClassNameIs(className: String): Boolean {
    return this.declaration.qualifiedName?.asString() == className
}

infix fun KSType.whenClassIs(kclass: KClass<*>): Boolean {
    return this.declaration.qualifiedName?.asString() == kclass.qualifiedName
}

context(KSPLogger)
fun <A : Annotation> DataModifierComputer<A>.computeModifier(annotation: A, type: KSType): DataModifier {
    return this.compute(DataBlueprint.blueprint(type)!!, annotation).getOrThrow()
}

val customEditors = listOf(
    ClosedRangeEditor,
    ColorEditor,
    CoordinateEditor,
    CronEditor,
    DurationEditor,
    ItemEditor,
    MaterialEditor,
    OptionalEditor,
    PositionEditor,
    PotionEffectTypeEditor,
    RefEditor,
    SkinEditor,
    SoundIdEditor,
    SoundSourceEditor,
    VectorEditor,
    WorldEditor
)