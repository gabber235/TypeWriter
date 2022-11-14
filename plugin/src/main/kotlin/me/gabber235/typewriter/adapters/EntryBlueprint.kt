package me.gabber235.typewriter.adapters

import com.google.gson.annotations.SerializedName
import com.google.gson.reflect.TypeToken
import lirand.api.utilities.allFields
import me.gabber235.typewriter.entry.Entry
import me.gabber235.typewriter.entry.entries.*
import java.lang.reflect.ParameterizedType

data class EntryBlueprint(
	val name: String,
	val description: String,
	val fields: FieldType,
	@Transient
	val clazz: Class<out Entry>,
)

sealed class FieldType {
	companion object {
		fun fromTypeToken(token: TypeToken<*>): FieldType {
			val primitive = PrimitiveFieldType.fromTokenType(token)
			if (primitive != null) {
				return PrimitiveField(primitive)
			}


			return when {
				token.rawType.isEnum                             -> EnumField.fromTypeToken(token)
				List::class.java.isAssignableFrom(token.rawType) -> {
					val type = token.type
					if (type is ParameterizedType) {
						val typeArg = type.actualTypeArguments[0]
						ListField(fromTypeToken(TypeToken.get(typeArg)))
					} else {
						throw IllegalArgumentException("Unknown type for list field: $type")
					}
				}

				Map::class.java.isAssignableFrom(token.rawType)  -> {
					val type = token.type
					if (type is ParameterizedType) {
						val typeArgs = type.actualTypeArguments
						MapField(fromTypeToken(TypeToken.get(typeArgs[0])), fromTypeToken(TypeToken.get(typeArgs[1])))
					} else {
						throw IllegalArgumentException("Unknown type for map field: $type")
					}
				}

				else                                             -> ObjectField.fromTypeToken(token)
			}
		}
	}
}

class PrimitiveField(val type: PrimitiveFieldType) : FieldType()


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

	companion object {
		fun fromTokenType(token: TypeToken<*>): PrimitiveFieldType? {
			return when (token.rawType) {
				Boolean::class.java          -> BOOLEAN
				Double::class.java           -> DOUBLE
				Double::class.javaObjectType -> DOUBLE
				Float::class.java            -> DOUBLE
				Float::class.javaObjectType  -> DOUBLE
				Int::class.java              -> INTEGER
				Int::class.javaObjectType    -> INTEGER
				Long::class.java             -> INTEGER
				Long::class.javaObjectType   -> INTEGER
				String::class.java           -> STRING
				else                         -> null
			}
		}
	}
}

// If the field is an enum, the values will be the enum constants
class EnumField(val values: List<String>) : FieldType() {
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
}

// If the field is a list, this is the type of the list
class ListField(val type: FieldType) : FieldType()

// If the field is a map, this is the type of the map
class MapField(val key: FieldType, val value: FieldType) : FieldType()

// If the field is an object, this is the type of the object
class ObjectField(val fields: Map<String, FieldType>) : FieldType() {
	companion object {
		fun fromTypeToken(token: TypeToken<*>): ObjectField {
			val fields = mutableMapOf<String, FieldType>()

			token.rawType.allFields.forEach { field ->
				val name =
					if (field.isAnnotationPresent(SerializedName::class.java)) {
						field.getAnnotation(SerializedName::class.java).value
					} else {
						field.name
					}

				val type = FieldType.fromTypeToken(TypeToken.get(field.genericType))

				fields[name] = type
			}

			return ObjectField(fields)
		}
	}
}
