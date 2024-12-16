package com.typewritermc.processors.entry

import com.google.devtools.ksp.processing.KSPLogger
import com.google.devtools.ksp.processing.Resolver
import com.google.devtools.ksp.symbol.KSType
import com.typewritermc.processors.entry.editors.*
import kotlinx.serialization.json.JsonElement

interface CustomEditor {
    /// The name of the editor
    val id: String

    fun accept(type: KSType): Boolean

    context(KSPLogger, Resolver)
    fun default(type: KSType): JsonElement

    context(KSPLogger, Resolver)
    fun shape(type: KSType): DataBlueprint

    context(KSPLogger, Resolver)
    fun validateDefault(type: KSType, default: JsonElement): Result<Unit> {
        return shape(type).validateDefault(default)
    }

    fun modifiers(type: KSType): List<DataModifier> {
        return emptyList()
    }
}

context(KSPLogger, Resolver)
fun <A : Annotation> DataModifierComputer<A>.computeModifier(annotation: A, type: KSType): DataModifier {
    return this.compute(DataBlueprint.blueprint(type)!!, annotation).getOrThrow()
}

val customEditors = listOf(
    ClosedRangeEditor,
    ColorEditor,
    CoordinateEditor,
    CronEditor,
    DurationEditor,
    EntryInteractionContextKeyEditor,
    GenericEditor,
    GlobalContextKeyEditor,
    ItemEditor,
    MaterialEditor,
    OptionalEditor,
    PositionEditor,
    PotionEffectTypeEditor,
    RefEditor,
    SerializedItemEditor,
    SkinEditor,
    SoundIdEditor,
    SoundSourceEditor,
    VarEditor,
    VectorEditor,
    WorldEditor
)