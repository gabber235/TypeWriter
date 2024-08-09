package me.gabber235.typewriter.adapters

import com.google.gson.*
import com.google.gson.annotations.SerializedName
import com.google.gson.reflect.TypeToken
import lirand.api.utilities.allFields
import me.gabber235.typewriter.entry.Entry
import java.lang.reflect.Modifier
import java.lang.reflect.ParameterizedType

data class EntryBlueprint(
    val name: String,
    val description: String,
    val adapter: String,
    val fields: FieldInfo,
    val color: String, // Hex color
    val icon: String, // Font Awesome icon from [Icons]
    val tags: List<String>,
    @Transient
    val clazz: Class<out Entry>,
)

sealed class FieldInfo {
    val modifiers: MutableList<JsonObject> = mutableListOf()

    companion object {

        fun fromTypeToken(token: TypeToken<*>): FieldInfo {
            val customEditor = computeCustomEditor(token)
            if (customEditor != null) {
                return CustomField(
                    editor = customEditor.name,
                    fieldInfo = customEditor.generateFieldInfo(token),
                    default = customEditor.generateDefault(token),
                ).apply {
                    customEditor.generateModifiers(token, this).forEach { it.appendModifier(this) }
                }
            }

            val primitive = PrimitiveFieldType.fromTokenType(token)
            if (primitive != null) {
                return PrimitiveField(primitive)
            }


            return when {
                token.rawType.isEnum -> EnumField.fromTypeToken(token)
                List::class.java.isAssignableFrom(token.rawType) -> {
                    val type = token.type
                    if (type is ParameterizedType) {
                        val typeArg = type.actualTypeArguments[0]
                        ListField(fromTypeToken(TypeToken.get(typeArg)))
                    } else {
                        throw IllegalArgumentException("Unknown type for list field: $type")
                    }
                }

                Map::class.java.isAssignableFrom(token.rawType) -> {
                    val type = token.type
                    if (type is ParameterizedType) {
                        val typeArgs = type.actualTypeArguments
                        MapField(fromTypeToken(TypeToken.get(typeArgs[0])), fromTypeToken(TypeToken.get(typeArgs[1])))
                    } else {
                        throw IllegalArgumentException("Unknown type for map field: $type")
                    }
                }

                else -> ObjectField.fromTypeToken(token)
            }
        }
    }

    abstract fun default(): JsonElement
}

class PrimitiveField(val type: PrimitiveFieldType) : FieldInfo() {
    override fun default(): JsonElement = type.default
}


enum class PrimitiveFieldType {
    @SerializedName("boolean")
    BOOLEAN,

    @SerializedName("double")
    DOUBLE,

    @SerializedName("integer")
    INTEGER,

    @SerializedName("string")
    STRING,
    ;

    val default: JsonElement
        get() = when (this) {
            BOOLEAN -> JsonPrimitive(false)
            DOUBLE -> JsonPrimitive(0.0)
            INTEGER -> JsonPrimitive(0)
            STRING -> JsonPrimitive("")
        }

    companion object {
        fun fromTokenType(token: TypeToken<*>): PrimitiveFieldType? {
            return when (token.rawType) {
                Boolean::class.java -> BOOLEAN
                Double::class.java -> DOUBLE
                Double::class.javaObjectType -> DOUBLE
                Float::class.java -> DOUBLE
                Float::class.javaObjectType -> DOUBLE
                Int::class.java -> INTEGER
                Int::class.javaObjectType -> INTEGER
                Long::class.java -> INTEGER
                Long::class.javaObjectType -> INTEGER
                String::class.java -> STRING
                else -> null
            }
        }
    }
}

// If the field is an enum, the values will be the enum constants
class EnumField(private val values: List<String>) : FieldInfo() {
    companion object {
        fun fromTypeToken(token: TypeToken<*>): EnumField {
            /// If the enum fields have an @SerializedName annotation, use that as the value
            /// Otherwise, use the enum constant name

            val values = token.rawType.enumConstants.filterIsInstance<Enum<*>>().map { enumConstant ->
                val field = enumConstant.javaClass.getField(enumConstant.name)
                val annotation = field.getAnnotation(SerializedName::class.java)
                annotation?.value ?: enumConstant.name
            }

            return EnumField(values)
        }
    }

    override fun default(): JsonElement = JsonPrimitive(values.first())
}

// If the field is a list, this is the type of the list
class ListField(val type: FieldInfo) : FieldInfo() {
    override fun default(): JsonElement = JsonArray()
}

// If the field is a map, this is the type of the map
class MapField(val key: FieldInfo, val value: FieldInfo) : FieldInfo() {
    override fun default(): JsonElement = JsonObject()
}

// If the field is an object, this is the type of the object
class ObjectField(val fields: Map<String, FieldInfo>) : FieldInfo() {
    override fun default(): JsonElement {
        val obj = JsonObject()
        fields.forEach { (name, field) ->
            obj.add(name, field.default())
        }
        return obj
    }

    companion object {
        fun fromTypeToken(token: TypeToken<*>): ObjectField {
            val fields = mutableMapOf<String, FieldInfo>()

            token.rawType.allFields.filter { !Modifier.isStatic(it.modifiers) }.forEach { field ->
                val name =
                    if (field.isAnnotationPresent(SerializedName::class.java)) {
                        field.getAnnotation(SerializedName::class.java).value
                    } else {
                        field.name
                    }

                val info = FieldInfo.fromTypeToken(TypeToken.get(field.genericType))

                // If a field has a modifier for the ui. E.g. it is a trigger or fact or something else.
                // We add a bit of extra information to the field. This is used by the UI to
                // display the field differently.
                computeFieldModifiers(field, info)

                fields[name] = info
            }

            return ObjectField(fields)
        }

    }
}

// If a custom editor takes over a field, this is the type of the object.
// The custom editor will be responsible for displaying the field.
class CustomField(
    val editor: String,
    val fieldInfo: FieldInfo? = null,
    val default: JsonElement = JsonNull.INSTANCE,
) : FieldInfo() {
    override fun default(): JsonElement = default
}


