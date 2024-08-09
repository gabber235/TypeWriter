package lirand.api.nbt

import kotlinx.serialization.SerializationException
import kotlinx.serialization.serializer
import lirand.api.nbt.serialization.NbtRootDecoder
import lirand.api.nbt.serialization.NbtRootEncoder

/**
 * Gives access to change [NbtData] values of certain generic types.
 */
class NbtDataAccessor(@PublishedApi internal val nbtData: NbtData) {


	/**
	 * Gets the value at the given [key]
	 * or throws an [IllegalStateException] if it
	 * was not possible to find any value
	 * at the given [key], or if its type
	 * is not [T].
	 */
	inline operator fun <reified T : Any> get(key: String): T {
		return getOrNull(key)
			?: error("There is no value under the \"$key\" key or its type is not specified generic type")
	}

	/**
	 * @see get
	 */
	inline operator fun <reified T : Any> invoke(key: String): T {
		return get(key)
	}

	/**
	 * Gets the value at the given [key].
	 * The returned value is null if it
	 * was not possible to find any value
	 * at the given [key], or if its type
	 * is not [T].
	 */
	inline fun <reified T : Any> getOrNull(key: String): T? {
		val dataType = NbtDataType.getFor<T>() ?: run {
			val encoder = NbtRootDecoder(nbtData, key)
			return try {
				encoder.decodeSerializableValue(serializer())
			} catch (_: SerializationException) {
				null
			}
		}
		return nbtData[key, dataType]
	}

	/**
	 * Gets the value at the given [key].
	 * If it was not possible to find any value
	 * at the given [key], or if its type is not [T],
	 * the result of calling [defaultValue] was put into specified location.
	 */
	inline fun <reified T : Any> getOrSet(key: String, defaultValue: () -> T): T {
		return getOrNull(key) ?: defaultValue().also {
			set(key, it)
		}
	}

	/**
	 * Gets the value at the given [key].
	 * The returned value is [defaultValue], if it
	 * was not possible to find any value
	 * at the given [key], or if the type
	 * is not [T].
	 */
	inline fun <reified T : Any> getOrDefault(key: String, defaultValue: () -> T): T {
		return getOrNull(key) ?: defaultValue()
	}


	/**
	 * Sets some [value]
	 * at the position of the given [key].
	 */
	inline operator fun <reified T : Any> set(key: String, value: T) {
		val dataType = NbtDataType.getFor<T>() ?: run {
			val encoder = NbtRootEncoder(nbtData, key)
			encoder.encodeSerializableValue(serializer(), value)
			return
		}
		nbtData[key, dataType] = value
	}
}