package me.gabber235.typewriter.adapters.modifiers

import me.gabber235.typewriter.Typewriter.Companion.plugin
import me.gabber235.typewriter.adapters.*
import me.gabber235.typewriter.adapters.FieldModifier.DynamicModifier
import me.gabber235.typewriter.entry.StaticEntry
import kotlin.reflect.KClass

@Target(AnnotationTarget.FIELD, AnnotationTarget.PROPERTY)
annotation class StaticEntryIdentifier(val entry: KClass<out StaticEntry>)


object StaticSelectorModifierComputer : StaticModifierComputer<StaticEntryIdentifier> {
	override val annotationClass: Class<StaticEntryIdentifier> = StaticEntryIdentifier::class.java

	override fun computeModifier(annotation: StaticEntryIdentifier, info: FieldInfo): FieldModifier? {
		if (info !is PrimitiveField) {
			plugin.logger.warning("StaticEntryIdentifier can only be used on a string field")
			return null
		}
		if (info.type != PrimitiveFieldType.STRING) {
			plugin.logger.warning("StaticEntryIdentifier can only be used on a string field")
			return null
		}

		val entry = annotation.entry
		// Get the tag from the entry with annotation @Tags
		val tag = entry.annotations.find { it is Tags }?.let { (it as Tags).tags.firstOrNull() }
		if (tag == null) {
			plugin.logger.warning("WARNING: Entry ${entry.simpleName} does not have a tag. It is needed for the StaticEntryIdentifier")
			return null
		}

		return DynamicModifier("static", tag)
	}
}