package me.gabber235.typewriter.adapters.modifiers

import me.gabber235.typewriter.Typewriter.Companion.plugin
import me.gabber235.typewriter.adapters.*
import me.gabber235.typewriter.entry.PageType

@Target(AnnotationTarget.PROPERTY, AnnotationTarget.FIELD, AnnotationTarget.VALUE_PARAMETER)
annotation class Page(val type: PageType)

object PageModifierComputer : StaticModifierComputer<Page> {
	override val annotationClass: Class<Page> = Page::class.java

	override fun computeModifier(annotation: Page, info: FieldInfo): FieldModifier? {
		innerCompute(annotation, info)?.let { return it }

		if (info !is PrimitiveField) {
			plugin.logger.warning("Page annotation can only be used on primitive fields")
			return null
		}

		if (info.type != PrimitiveFieldType.STRING) {
			plugin.logger.warning("Page annotation can only be used on string fields")
			return null
		}

		return FieldModifier.DynamicModifier("page", annotation.type.id)
	}
}