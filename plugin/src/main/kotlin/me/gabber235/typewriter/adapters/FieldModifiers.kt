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

    class InnerCustomModifier(val modifier: FieldModifier) : FieldModifier {
        override fun appendModifier(info: FieldInfo) {
            if (info !is CustomField) return
            val customInfo = info.fieldInfo ?: return
            modifier.appendModifier(customInfo)
        }
    }
}

interface ModifierComputer {
    fun compute(field: Field, info: FieldInfo): FieldModifier?

    /**
     * Checks if the given field is a list and tries to compute the modifier for the list's type.
     * This can be called inside the [compute] method.
     *
     * @param field The field to check
     * @param info The info of the field
     */
    fun innerComputeForList(field: Field, info: FieldInfo): FieldModifier? {
        if (info !is ListField) return null
        return compute(field, info.type)?.let { FieldModifier.InnerListModifier(it) }
    }

    /**
     * Checks if the given field is a map and tries to compute the modifier for the map's key and value.
     * This can be called inside the [compute] method.
     *
     * @param field The field to check
     * @param info The info of the field
     */
    fun innerComputeForMap(field: Field, info: FieldInfo): FieldModifier? {
        if (info !is MapField) return null
        val keyModifier = compute(field, info.key)
        val valueModifier = compute(field, info.value)

        if (keyModifier == null && valueModifier == null) return null
        return FieldModifier.InnerMapModifier(keyModifier, valueModifier)
    }

    /**
     * Checks if the given field is a custom field and tries to compute the modifier for the custom field's type.
     * This can be called inside the [compute] method.
     *
     * @param field The field to check
     * @param info The info of the field
     */
    fun innerComputeForCustom(field: Field, info: FieldInfo): FieldModifier? {
        if (info !is CustomField) return null
        val customInfo = info.fieldInfo ?: return null
        return compute(field, customInfo)?.let { FieldModifier.InnerCustomModifier(it) }
    }

    /**
     * Checks if the given field is a list, map or custom field and tries to compute the modifier for the field's type.
     * This can be called inside the [compute] method.
     *
     * @param field The field to check
     * @param info The info of the field
     */
    fun innerCompute(field: Field, info: FieldInfo): FieldModifier? {
        return innerComputeForList(field, info) ?: innerComputeForMap(field, info) ?: innerComputeForCustom(field, info)
    }
}

private val computers: List<ModifierComputer> by lazy {
    listOf(
        HelpModifierComputer,
        TriggersModifierComputer,
        EntrySelectorModifierComputer,
        SnakeCaseModifierComputer,
        MultiLineModifierComputer,
        MaterialPropertiesModifierComputer,
        WithRotationModifierComputer,
        SegmentModifierComputer,
        MinModifierComputer,
        MaxModifierComputer,
        PageModifierComputer,
        GeneratedModifierComputer,
        CaptureModifierComputer,
        PlaceholderModifierComputer,
        ColoredModifierComputer,
        RegexModifierComputer,
        IconModifierComputer,
    )
}


/**
 * If a field has a modifier for the ui. E.g. it is a trigger or fact or something else.
 * We add a bit of extra information to the field. This is used by the UI to
 * display the field differently.
 */
fun computeFieldModifiers(field: Field, info: FieldInfo) {
    computers.mapNotNull { it.compute(field, info) }.forEach { it.appendModifier(info) }
}