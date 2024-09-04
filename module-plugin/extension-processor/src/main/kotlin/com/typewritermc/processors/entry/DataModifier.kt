package com.typewritermc.processors.entry

import kotlinx.serialization.Serializable
import kotlinx.serialization.json.JsonElement
import kotlinx.serialization.json.JsonNull
import kotlinx.serialization.json.JsonPrimitive

sealed interface DataModifier {
    fun appendModifier(blueprint: DataBlueprint)

    @Serializable
    data class Modifier(val name: String, val data: JsonElement) : DataModifier {
        constructor(name: String) : this(name, JsonNull)
        constructor(name: String, data: String) : this(name, JsonPrimitive(data))
        constructor(name: String, data: Int) : this(name, JsonPrimitive(data))

        override fun appendModifier(blueprint: DataBlueprint) {
            blueprint.modifiers.add(this)
        }
    }

    data class InnerModifier(val modifier: DataModifier) : DataModifier {
        override fun appendModifier(blueprint: DataBlueprint) {
            when (blueprint) {
                is DataBlueprint.ListBlueprint -> {
                    modifier.appendModifier(blueprint.type)
                }

                is DataBlueprint.MapBlueprint -> {
                    modifier.appendModifier(blueprint.key)
                    modifier.appendModifier(blueprint.value)
                }

                is DataBlueprint.CustomBlueprint -> {
                    modifier.appendModifier(blueprint.shape)
                }

                else -> throw CouldNotAppendModifierException(blueprint, this)
            }
        }
    }

    data class InnerMapModifier(private val key: DataModifier?, private val value: DataModifier?) : DataModifier {
        override fun appendModifier(blueprint: DataBlueprint) {
            if (blueprint !is DataBlueprint.MapBlueprint) throw CouldNotAppendModifierException(blueprint, this)
            key?.appendModifier(blueprint.key)
            value?.appendModifier(blueprint.value)
        }
    }

    data class InnerObjectModifier(private val fields: Map<String, DataModifier>) : DataModifier {
        override fun appendModifier(blueprint: DataBlueprint) {
            if (blueprint !is DataBlueprint.ObjectBlueprint) throw CouldNotAppendModifierException(blueprint, this)
            fields.forEach { (key, modifier) ->
                modifier.appendModifier(blueprint.fields[key] ?: throw CouldNotAppendModifierException(blueprint, this))
            }
        }
    }
}

class CouldNotAppendModifierException(blueprint: DataBlueprint, modifier: DataModifier) :
    Exception("Could not append modifier $modifier to blueprint $blueprint")