package lirand.api.nbt.serialization

import kotlinx.serialization.*
import kotlinx.serialization.descriptors.PrimitiveKind
import kotlinx.serialization.descriptors.SerialDescriptor
import kotlinx.serialization.descriptors.StructureKind
import kotlinx.serialization.descriptors.elementDescriptors
import kotlinx.serialization.modules.EmptySerializersModule
import kotlinx.serialization.modules.SerializersModule
import lirand.api.nbt.*


/**
 * Represents [NbtData] serialization settings.
 */
sealed class Nbt(
	val configuration: NbtConfiguration,
	val serializersModule: SerializersModule
) {

	companion object Default : Nbt(NbtConfiguration(), EmptySerializersModule())


	inline fun <reified T> encodeToNbtData(value: T, redirectKey: String? = null): NbtData {
		return encodeToNbtData(serializer(), value, redirectKey)
	}

	inline fun <reified T> encodeToNbtData(
		serializer: SerializationStrategy<T>,
		value: T,
		redirectKey: String? = null
	): NbtData {
		val encoder = NbtRootEncoder(NbtData(), redirectKey, this)
		encoder.encodeSerializableValue(serializer, value)
		return encoder.nbtData
	}


	inline fun <reified T> decodeFromNbtData(nbtData: NbtData, key: String? = null): T {
		return decodeFromNbtData(serializer(), nbtData, key)
	}

	inline fun <reified T> decodeFromNbtData(
		deserializer: DeserializationStrategy<T>,
		nbtData: NbtData,
		key: String? = null
	): T {
		val decoder = NbtRootDecoder(nbtData, key, this)
		return decoder.decodeSerializableValue(deserializer)
	}
}

/**
 * Creates an instance of [Nbt] configured from the optionally given [Nbt] instance and adjusted with [builderAction].
 */
fun Nbt(from: Nbt = Nbt.Default, builderAction: NbtBuilder.() -> Unit): Nbt {
	val builder = NbtBuilder(from)
	builder.builderAction()
	val configuration = builder.build()
	return NbtImpl(configuration, builder.serializersModule)
}

private class NbtImpl(
	configuration: NbtConfiguration,
	serializersModule: SerializersModule
) : Nbt(configuration, serializersModule)



/**
 * Configuration of the current [Nbt] instance available through [Nbt.configuration]
 * and configured with [NbtBuilder] constructor.
 *
 * Can be used for debug purposes and for custom Json-specific serializers
 * via [NbtDataEncoder] and [NbtDataDecoder].
 *
 * Standalone configuration object is meaningless and can nor be used outside of the
 * [Nbt], neither new [Nbt] instance can be created from it.
 *
 * Detailed description of each property is available in [NbtBuilder] class.
 */
data class NbtConfiguration(
	val encodeDefaults: Boolean = false,
	val ignoreUnknownKeys: Boolean = false
)

/**
 * Builder of the [Nbt] instance provided by `Nbt { ... }` factory function.
 */
class NbtBuilder internal constructor(nbt: Nbt) {
	/**
	 * Module with contextual and polymorphic serializers to be used in the resulting [Nbt] instance.
	 */
	var serializersModule: SerializersModule = nbt.serializersModule

	/**
	 * Specifies whether default values of Kotlin properties should be encoded.
	 * `false` by default.
	 */
	var encodeDefaults: Boolean = nbt.configuration.encodeDefaults

	/**
	 * Specifies whether encounters of unknown properties in the input JSON
	 * should be ignored instead of throwing [SerializationException].
	 * `false` by default.
	 */
	var ignoreUnknownKeys: Boolean = nbt.configuration.ignoreUnknownKeys


	fun build(): NbtConfiguration {
		return NbtConfiguration(encodeDefaults, ignoreUnknownKeys)
	}
}




@OptIn(ExperimentalSerializationApi::class)
internal fun getNbtDataType(descriptor: SerialDescriptor): NbtDataType<*>? {
	return when (val kind = descriptor.kind) {
		is StructureKind -> when (kind) {
			is StructureKind.LIST -> {
				NbtListType(getNbtDataType(descriptor.elementDescriptors.single()) as NbtDataType<*>)
			}
			else -> NbtCompoundType
		}
		is PrimitiveKind -> when (kind) {
			is PrimitiveKind.STRING -> NbtStringType
			is PrimitiveKind.CHAR -> NbtCharType
			is PrimitiveKind.BYTE -> NbtByteType
			is PrimitiveKind.BOOLEAN -> NbtBooleanType
			is PrimitiveKind.SHORT -> NbtShortType
			is PrimitiveKind.INT -> NbtIntType
			is PrimitiveKind.LONG -> NbtLongType
			is PrimitiveKind.FLOAT -> NbtFloatType
			is PrimitiveKind.DOUBLE -> NbtDoubleType
		}
		else -> null
	}
}