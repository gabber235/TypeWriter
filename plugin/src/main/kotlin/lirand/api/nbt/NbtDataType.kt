package lirand.api.nbt

import java.lang.reflect.Constructor
import java.lang.reflect.Method
import java.lang.reflect.Modifier
import kotlin.reflect.KClass
import kotlin.reflect.KType
import kotlin.reflect.KVariance
import kotlin.reflect.full.isSubtypeOf
import kotlin.reflect.typeOf

sealed interface NbtDataType<T : Any> {

	val typeId: Int

	fun decode(nbt: Any): T?

	fun encode(data: T): Any


	companion object {
		/**
		 * Gets [NbtDataType] of the corresponding [T] type.
		 */
		@Suppress("UNCHECKED_CAST")
		inline fun <reified T : Any> getFor(): NbtDataType<T>? {
			return getFor(typeOf<T>()) as NbtDataType<T>?
		}

		/**
		 * Gets [NbtDataType] of the corresponding value.
		 */
		@Suppress("UNCHECKED_CAST")
		fun <T : Any> getFor(value: T): NbtDataType<T>? {
			return when (value) {
				is NbtData -> NbtCompoundType
				is String -> NbtStringType
				is Char -> NbtCharType
				is Byte -> NbtByteType
				is Boolean -> NbtBooleanType
				is Short -> NbtShortType
				is Int -> NbtIntType
				is Long -> NbtLongType
				is Float -> NbtFloatType
				is Double -> NbtDoubleType
				is ByteArray -> NbtByteArrayType
				is IntArray -> NbtIntArrayType
				is LongArray -> NbtLongArrayType
				is Collection<*> -> {
					if ((value as Collection<Any>).isNotEmpty())
						NbtListType(getFor(value.first()) ?: return null)
					else
						NbtListType(NbtByteType)
				}
				else -> null
			} as NbtDataType<T>?
		}

		/**
		 * Gets [NbtDataType] of the corresponding [nbtTag].
		 */
		@Suppress("UNCHECKED_CAST")
		fun getForTag(nbtTag: Any): NbtDataType<*>? {
			return when (getNbtTypeId(nbtTag)) {
				10 -> NbtCompoundType
				8 -> NbtStringType
				1 -> NbtByteType
				2 -> NbtShortType
				3 -> NbtIntType
				4 -> NbtLongType
				5 -> NbtFloatType
				6 -> NbtDoubleType
				7 -> NbtByteArrayType
				11 -> NbtIntArrayType
				12 -> NbtLongArrayType
				9 -> {
					if ((nbtTag as List<Any>).isNotEmpty())
						NbtListType(getForTag(nbtTag.first()) ?: return null)
					else
						NbtListType(NbtByteType)
				}
				else -> null
			}
		}

		@PublishedApi
		internal fun getFor(type: KType): NbtDataType<*>? {
			return when (type.classifier) {
				NbtData::class -> NbtCompoundType
				String::class -> NbtStringType
				Char::class -> NbtCharType
				Byte::class -> NbtByteType
				Boolean::class -> NbtBooleanType
				Short::class -> NbtShortType
				Int::class -> NbtIntType
				Long::class -> NbtLongType
				Float::class -> NbtFloatType
				Double::class -> NbtDoubleType
				ByteArray::class -> NbtByteArrayType
				IntArray::class -> NbtIntArrayType
				LongArray::class -> NbtLongArrayType
				else -> {
					if (!type.isSubtypeOf(typeOf<Collection<*>>())) return null

					val innerType = type.arguments.firstOrNull()
						?.takeIf { it.variance != KVariance.IN }
						?.type
						?: return null

					NbtListType(getFor(innerType) ?: return null)
				}
			}
		}
	}
}


abstract class AbstractNbtDataType<T : Any>(override val typeId: Int) : NbtDataType<T> {
	final override fun decode(nbt: Any): T? {
		return if (getNbtTypeId(nbt) == typeId)
			decodeCorrectlyTyped(nbt)
		else
			null
	}

	protected abstract fun decodeCorrectlyTyped(nbt: Any): T?

	override fun toString(): String {
		return this::class.simpleName ?: "NbtDataType"
	}
}


object NbtCompoundType : AbstractNbtDataType<NbtData>(10) {
	override fun encode(data: NbtData): Any {
		return data.nbtTagCompound
	}

	override fun decodeCorrectlyTyped(nbt: Any): NbtData {
		return NbtData(nbt)
	}
}

object NbtStringType : AbstractNbtDataType<String>(8) {
	override fun encode(data: String): Any {
		return stringTagFactoryMethod.invoke(null, data)
	}

	override fun decodeCorrectlyTyped(nbt: Any): String {
		return (nbtStringAsStringMethod.invoke(nbt) as String).removeSurrounding("\"")
	}
}

object NbtCharType : AbstractNbtDataType<Char>(8) {
	override fun encode(data: Char): Any {
		return stringTagFactoryMethod.invoke(null, data.toString())
	}

	override fun decodeCorrectlyTyped(nbt: Any): Char? {
		return (nbtStringAsStringMethod.invoke(nbt) as String).removeSurrounding("\"").singleOrNull()
	}
}

object NbtByteType : AbstractNbtDataType<Byte>(1) {
	override fun encode(data: Byte): Any {
		return byteTagFactoryMethod.invoke(null, data)
	}

	override fun decodeCorrectlyTyped(nbt: Any): Byte {
		return nbtNumberAsByteMethod.invoke(nbt) as Byte
	}
}

object NbtBooleanType : AbstractNbtDataType<Boolean>(1) {
	override fun encode(data: Boolean): Any {
		return NbtByteType.encode(if (data) 1 else 0)
	}

	override fun decodeCorrectlyTyped(nbt: Any): Boolean? {
		val byte = NbtByteType.decode(nbt)?.takeIf { it in 0..1 } ?: return null
		return byte > 0
	}
}

object NbtShortType : AbstractNbtDataType<Short>(2) {
	override fun encode(data: Short): Any {
		return shortTagFactoryMethod.invoke(null, data)
	}

	override fun decodeCorrectlyTyped(nbt: Any): Short {
		return nbtNumberAsShortMethod.invoke(nbt) as Short
	}
}

object NbtIntType : AbstractNbtDataType<Int>(3) {
	override fun encode(data: Int): Any {
		return intTagFactoryMethod.invoke(null, data)
	}

	override fun decodeCorrectlyTyped(nbt: Any): Int {
		return nbtNumberAsIntMethod.invoke(nbt) as Int
	}
}

object NbtLongType : AbstractNbtDataType<Long>(4) {
	override fun encode(data: Long): Any {
		return longTagFactoryMethod.invoke(null, data)
	}

	override fun decodeCorrectlyTyped(nbt: Any): Long {
		return nbtNumberAsLongMethod.invoke(nbt) as Long
	}
}

object NbtFloatType : AbstractNbtDataType<Float>(5) {
	override fun encode(data: Float): Any {
		return floatTagFactoryMethod.invoke(null, data)
	}

	override fun decodeCorrectlyTyped(nbt: Any): Float {
		return nbtNumberAsFloatMethod.invoke(nbt) as Float
	}
}

object NbtDoubleType : AbstractNbtDataType<Double>(6) {
	override fun encode(data: Double): Any {
		return doubleTagFactoryMethod.invoke(null, data)
	}

	override fun decodeCorrectlyTyped(nbt: Any): Double {
		return nbtNumberAsDoubleMethod.invoke(nbt) as Double
	}

}

object NbtByteArrayType : AbstractNbtDataType<ByteArray>(7) {
	override fun encode(data: ByteArray): Any {
		return byteArrayTagConstructor.newInstance(data)
	}

	override fun decodeCorrectlyTyped(nbt: Any): ByteArray {
		return nbtByteArrayGetBytesMethod.invoke(nbt) as ByteArray
	}
}

object NbtIntArrayType : AbstractNbtDataType<IntArray>(11) {
	override fun encode(data: IntArray): Any {
		return intArrayTagConstructor.newInstance(data)
	}

	override fun decodeCorrectlyTyped(nbt: Any): IntArray {
		return nbtIntArrayGetIntsMethod.invoke(nbt) as IntArray
	}
}

object NbtLongArrayType : AbstractNbtDataType<LongArray>(12) {
	override fun encode(data: LongArray): Any {
		return longArrayTagConstructor.newInstance(data)
	}

	override fun decodeCorrectlyTyped(nbt: Any): LongArray {
		return nbtLongArrayGetLongsMethod.invoke(nbt) as LongArray
	}
}

@Suppress("UNCHECKED_CAST")
class NbtListType<T : Any>(val innerType: NbtDataType<T>) : AbstractNbtDataType<Collection<T>>(9) {
	override fun encode(data: Collection<T>): Any {
		return (nbtListConstructor.newInstance() as MutableList<Any>).apply {
			addAll(data.map { innerType.encode(it) })
		}
	}

	override fun decodeCorrectlyTyped(nbt: Any): List<T> {
		val list = nbt as MutableList<Any>

		return list.map { innerType.decode(it)!! }
	}

	override fun toString(): String {
		return "NbtListType($innerType)"
	}
}


private fun getNbtTypeId(nbtTag: Any): Int {
	return (nbtBaseGetTypeIdMethod.invoke(nbtTag) as Byte).toInt()
}

private val byteTagFactoryMethod = getTagFactoryMethod("NBTTagByte", Byte::class)
private val shortTagFactoryMethod = getTagFactoryMethod("NBTTagShort", Short::class)
private val intTagFactoryMethod = getTagFactoryMethod("NBTTagInt", Int::class)
private val longTagFactoryMethod = getTagFactoryMethod("NBTTagLong", Long::class)
private val floatTagFactoryMethod = getTagFactoryMethod("NBTTagFloat", Float::class)
private val doubleTagFactoryMethod = getTagFactoryMethod("NBTTagDouble", Double::class)
private val stringTagFactoryMethod = getTagFactoryMethod("NBTTagString", String::class)

private fun getTagFactoryMethod(className: String, argumentType: KClass<*>): Method {
	val clazz = Class.forName("${nmsPackage}.$className")

	return clazz.methods.find {
		it.returnType == clazz && Modifier.isStatic(it.modifiers)
				&& it.parameterTypes.let { it.size == 1 && it[0] == argumentType.java }
	}!!
}


private val nbtListConstructor = Class.forName("${nmsPackage}.NBTTagList").getConstructor()

private val byteArrayTagConstructor = getTagConstructor("NBTTagByteArray", ByteArray::class)
private val intArrayTagConstructor = getTagConstructor("NBTTagIntArray", IntArray::class)
private val longArrayTagConstructor = getTagConstructor("NBTTagLongArray", LongArray::class)

private fun getTagConstructor(className: String, dataArgumentClass: KClass<*>): Constructor<*> {
	val clazz = Class.forName("${nmsPackage}.$className")

	return clazz.getConstructor(dataArgumentClass.java)
}



private val nbtNumberClass = Class.forName("${nmsPackage}.NBTNumber")

private val nbtNumberAsByteMethod = getDecodeMethod(nbtNumberClass, Byte::class)
private val nbtNumberAsDoubleMethod = getDecodeMethod(nbtNumberClass, Double::class)
private val nbtNumberAsFloatMethod = getDecodeMethod(nbtNumberClass, Float::class)
private val nbtNumberAsIntMethod = getDecodeMethod(nbtNumberClass, Int::class)
private val nbtNumberAsLongMethod = getDecodeMethod(nbtNumberClass, Long::class)
private val nbtNumberAsShortMethod = getDecodeMethod(nbtNumberClass, Short::class)
private val nbtByteArrayGetBytesMethod = getDecodeMethod("NBTTagByteArray", ByteArray::class)
private val nbtIntArrayGetIntsMethod = getDecodeMethod("NBTTagIntArray", IntArray::class)
private val nbtLongArrayGetLongsMethod = getDecodeMethod("NBTTagLongArray", LongArray::class)
private val nbtStringAsStringMethod = getDecodeMethod("NBTTagString", String::class)

private fun getDecodeMethod(clazz: Class<*>, returnType: KClass<*>): Method {
	return clazz.methods.find { it.returnType == returnType.java && it.parameterCount == 0 }!!
}

private fun getDecodeMethod(className: String, returnType: KClass<*>): Method {
	return getDecodeMethod(Class.forName("${nmsPackage}.$className"), returnType)
}


internal val nbtBaseGetTypeIdMethod = Class.forName("${nmsPackage}.NBTBase").methods
	.find { it.returnType == Byte::class.java && it.parameterCount == 0 }!!