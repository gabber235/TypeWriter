package me.gabber235.typewriter.adapters

import com.google.gson.JsonObject
import me.gabber235.typewriter.adapters.modifiers.*
import java.lang.reflect.Field


private val gson = com.google.gson.Gson()

sealed interface FieldModifier {

	fun appendModifier(info: FieldInfo)

	interface DataModifier : FieldModifier {
		fun modifierData(): JsonObject
		override fun appendModifier(info: FieldInfo) {
			val data = modifierData()
			if (data.size() > 0) {
				info.modifiers.add(data)
			}
		}
	}


	class StaticModifier(val name: String) : DataModifier {
		override fun modifierData(): JsonObject {
			return JsonObject().apply {
				addProperty("name", name)
			}
		}
	}

	class DynamicModifier(val name: String, val data: Any) : DataModifier {
		override fun modifierData(): JsonObject {
			return JsonObject().apply {
				addProperty("name", name)
				add("data", gson.toJsonTree(data))
			}
		}
	}

	class InnerListModifier(val modifier: FieldModifier) : FieldModifier {
		override fun appendModifier(info: FieldInfo) {
			if (info !is ListField) return
			modifier.appendModifier(info.type)
		}
	}

	class InnerMapModifier(private val key: FieldModifier?, val value: FieldModifier?) : FieldModifier {
		override fun appendModifier(info: FieldInfo) {
			if (info !is MapField) return
			key?.appendModifier(info.key)
			value?.appendModifier(info.value)
		}
	}

	class MultiModifier(val modifiers: List<FieldModifier>) : FieldModifier {
		constructor(vararg modifiers: FieldModifier) : this(modifiers.toList())

		override fun appendModifier(info: FieldInfo) {
			modifiers.forEach { it.appendModifier(info) }
		}
	}
}

interface ModifierComputer {
	fun compute(field: Field, info: FieldInfo): FieldModifier?
}

private val computers: List<ModifierComputer> by lazy {
	listOf(
		TriggersModifierComputer,
		StaticSelectorModifierComputer,
		SnakeCaseModifierComputer,
		MultiLineModifierComputer,
	)
}

fun computeFieldModifiers(field: Field, info: FieldInfo) {
	computers.mapNotNull { it.compute(field, info) }.forEach { it.appendModifier(info) }
}