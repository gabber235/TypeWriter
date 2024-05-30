package lirand.api.nbt.serialization

import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.SerializationException
import kotlinx.serialization.SerializationStrategy
import kotlinx.serialization.descriptors.SerialDescriptor
import kotlinx.serialization.descriptors.StructureKind
import kotlinx.serialization.descriptors.elementDescriptors
import kotlinx.serialization.encoding.AbstractEncoder
import kotlinx.serialization.encoding.CompositeEncoder
import lirand.api.nbt.*
import java.util.*

@OptIn(ExperimentalSerializationApi::class)
abstract class AbstractNbtEncoder(protected val nbt: Nbt) : AbstractEncoder() {
	override val serializersModule = nbt.serializersModule

	protected var isNextNullable = false


	@Suppress("UNCHECKED_CAST")
	override fun <T> encodeSerializableValue(serializer: SerializationStrategy<T>, value: T) {
		val nbtDataType = getNbtDataType(serializer.descriptor)
		if (value != null && nbtDataType != null && nbtDataType is NbtListType<*> && nbtDataType.innerType !is NbtCompoundType)
			encodeValue(value)
		else
			super.encodeSerializableValue(serializer, value)
	}

	override fun beginStructure(descriptor: SerialDescriptor): CompositeEncoder {
		return if (descriptor.kind !is StructureKind.LIST) {
			NbtDataEncoder(NbtData(), this::consumeStructure, nbt)
		}
		else {
			NbtListEncoder(this::consumeStructure, nbt)
		}
	}

	override fun encodeEnum(enumDescriptor: SerialDescriptor, index: Int) {
		encodeValue(enumDescriptor.getElementName(index))
	}

	override fun encodeNotNullMark() {
		isNextNullable = true
	}

	override fun shouldEncodeElementDefault(descriptor: SerialDescriptor, index: Int): Boolean {
		return nbt.configuration.encodeDefaults
	}


	abstract fun consumeStructure(descriptor: SerialDescriptor, nbtData: NbtData)

	abstract fun consumeStructure(descriptor: SerialDescriptor, nbtList: List<Any>)
}

class NbtRootEncoder(
	val nbtData: NbtData = NbtData(),
	val redirectKey: String? = null,
	nbt: Nbt = Nbt
) : AbstractNbtEncoder(nbt) {

	override fun encodeValue(value: Any) {
		check(redirectKey != null) { "redirectKey must be specified" }

		val dataType = NbtDataType.getFor(value)
		require(dataType != null) { "Unknown data type of provided value" }

		nbtData[redirectKey, dataType] = value
	}

	override fun consumeStructure(descriptor: SerialDescriptor, nbtData: NbtData) {
		if (redirectKey == null) {
			this.nbtData.clear()
			this.nbtData.putAll(nbtData)
		}
		else {
			this.nbtData[redirectKey, NbtCompoundType] = nbtData
		}
	}

	@Suppress("UNCHECKED_CAST")
	override fun consumeStructure(descriptor: SerialDescriptor, nbtList: List<Any>) {
		check(redirectKey != null) { "redirectKey must be specified" }

		nbtData[redirectKey, getNbtDataType(descriptor) as NbtDataType<Any>] = nbtList
	}
}


@OptIn(ExperimentalSerializationApi::class)
class NbtDataEncoder(
	val nbtData: NbtData,
	private val consumer: (SerialDescriptor, NbtData) -> Unit,
	nbt: Nbt = Nbt
) : AbstractNbtEncoder(nbt) {
	private val keys = ArrayDeque<String>()

	override fun encodeElement(descriptor: SerialDescriptor, index: Int): Boolean {
		keys.push(descriptor.getElementName(index))
		return true
	}

	override fun encodeValue(value: Any) {
		NbtDataType.getFor(value)?.let {
			if (isNextNullable) {
				isNextNullable = false
				nbtData[keys.pop(), NbtListType(it)] = listOf(value)
			}
			else
				nbtData[keys.pop(), it] = value
		}
	}

	override fun encodeNull() {
		nbtData[keys.pop(), NbtListType(NbtByteType)] = emptyList()
	}

	override fun endStructure(descriptor: SerialDescriptor) {
		consumer(descriptor, nbtData)
	}

	override fun consumeStructure(descriptor: SerialDescriptor, nbtData: NbtData) {
		this.nbtData[keys.pop(), NbtCompoundType] = nbtData
	}

	@Suppress("UNCHECKED_CAST")
	override fun consumeStructure(descriptor: SerialDescriptor, nbtList: List<Any>) {
		this.nbtData[keys.pop(), getNbtDataType(descriptor) as NbtDataType<Any>] = nbtList
	}
}


class NbtListEncoder(
	private val consumer: (SerialDescriptor, List<Any>) -> Unit,
	nbt: Nbt = Nbt
) : AbstractNbtEncoder(nbt) {
	private val list = mutableListOf<Any>()

	override fun encodeValue(value: Any) {
		list.add(value)
	}

	override fun endStructure(descriptor: SerialDescriptor) {
		consumer(descriptor, list)
	}

	override fun consumeStructure(descriptor: SerialDescriptor, nbtData: NbtData) {
		list.add(nbtData)
	}

	override fun consumeStructure(descriptor: SerialDescriptor, nbtList: List<Any>) {
		list.add(nbtList)
	}
}