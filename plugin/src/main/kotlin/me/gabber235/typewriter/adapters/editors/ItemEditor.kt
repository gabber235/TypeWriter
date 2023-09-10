package me.gabber235.typewriter.adapters.editors

import com.google.gson.JsonObject
import com.google.gson.reflect.TypeToken
import me.gabber235.typewriter.adapters.*
import me.gabber235.typewriter.utils.Item
import java.lang.reflect.Modifier

@CustomEditor(Item::class)
fun ObjectEditor<Item>.item() = reference {
    default { token ->
        val obj = JsonObject()

        token.rawType.declaredFields.filter { !Modifier.isStatic(it.modifiers) }.forEach { field ->
            val name = field.name
            val info = FieldInfo.fromTypeToken(TypeToken.get(field.genericType))
            val default = info.default()
            obj.add(name, default)
        }

        obj
    }

    fieldInfo { token ->
        val fields = token.rawType.declaredFields.filter { !Modifier.isStatic(it.modifiers) }.associate { field ->
            val name = field.name
            // Check if the type is `Optional`
            val type = if (field.type.name == "java.util.Optional") {
                val type = field.genericType as java.lang.reflect.ParameterizedType
                type.actualTypeArguments[0]
            } else {
                field.genericType
            }
            val info = FieldInfo.fromTypeToken(TypeToken.get(type))
            computeFieldModifiers(field, info)
            name to info
        }

        ObjectField(fields)
    }
}