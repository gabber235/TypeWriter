package lirand.api.nbt.serialization

import kotlinx.serialization.DeserializationStrategy
import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.SerializationException
import kotlinx.serialization.descriptors.PolymorphicKind
import kotlinx.serialization.descriptors.SerialDescriptor
import kotlinx.serialization.descriptors.StructureKind
import kotlinx.serialization.encoding.AbstractDecoder
import kotlinx.serialization.encoding.CompositeDecoder
import kotlinx.serialization.modules.SerializersModule
import lirand.api.nbt.*

@OptIn(ExperimentalSerializationApi::class)
abstract class AbstractNbtDecoder(protected val nbt: Nbt) : AbstractDecoder() {
	override val serializersModule: SerializersModule = nbt.serializersModule

	private var nextNullable: Any? = null



	@Suppress("UNCHECKED_CAST")
	override fun <T> decodeSerializableValue(deserializer: DeserializationStrategy<T>): T {
		val nbtDataType = getNbtDataType(deserializer.descriptor)
		return if (nbtDataType != null && nbtDataType is NbtListType<*> && nbtDataType.innerType !is NbtCompoundType)
			decodeValue() as T
		else
			super.decodeSerializableValue(deserializer)
	}


	override fun decodeValue(): Any {
		val value = nextNullable ?: next()
		nextNullable = null

		return NbtDataType.getForTag(value)?.decode(value)
			?: throw SerializationException("NbtDataType not found for current value")
	}

	abstract fun next(): Any


	@Suppress("UNCHECKED_CAST")
	override fun beginStructure(descriptor: SerialDescriptor): CompositeDecoder {
		return if (descriptor.kind !is StructureKind.LIST) {
			NbtDataDecoder(decodeValue() as NbtData, nbt)
		}
		else {
			NbtListDecoder(next() as List<Any>, nbt)
		}
	}


	override fun decodeNotNullMark(): Boolean {
		return (next() as? List<*>)?.let {
			if (it.isEmpty()) {
				false
			}
			else {
				nextNullable = it[0]
				true
			}
		} ?: true
	}

	override fun decodeNull(): Nothing? {
		nextNullable = null
		return null
	}
}

class NbtRootDecoder(
	private val nbtData: NbtData,
	private val key: String? = null,
	nbt: Nbt = Nbt
) : AbstractNbtDecoder(nbt) {

	override fun next(): Any {
		return key?.let { nbtData.getNbtTag(it) } ?: nbtData.nbtTagCompound
	}

	override fun decodeElementIndex(descriptor: SerialDescriptor): Int {
		return 0
	}
}


@OptIn(ExperimentalSerializationApi::class)
class NbtDataDecoder(
	val nbtData: NbtData,
	nbt: Nbt = Nbt
) : AbstractNbtDecoder(nbt) {
	private lateinit var currentElement: Any
	private var currentIndex = 0


	override fun next(): Any {
		return currentElement
	}

	override fun decodeElementIndex(descriptor: SerialDescriptor): Int {
		while (currentIndex < descriptor.elementsCount) {
			val name = descriptor.getElementName(currentIndex++)
			nbtData.getNbtTag(name)?.let {
				currentElement = it
				return currentIndex - 1
			}
		}
		return CompositeDecoder.DECODE_DONE
	}

	override fun decodeCollectionSize(descriptor: SerialDescriptor): Int {
		return nbtData.keys.size
	}

	override fun endStructure(descriptor: SerialDescriptor) {
		if (nbt.configuration.ignoreUnknownKeys || descriptor.kind is PolymorphicKind) return

		val names = (0 until descriptor.elementsCount).map { descriptor.getElementName(it) }.distinct()
		for (key in nbtData.keys) {
			if (key !in names) {
				throw SerializationException("Unknown key: $key")
			}
		}
	}
}

@ExperimentalSerializationApi
class NbtListDecoder(
	val list: List<Any>,
	nbt: Nbt = Nbt
) : AbstractNbtDecoder(nbt) {
	private val iterator = list.iterator()
	private var currentIndex = 0

	override fun next(): Any {
		return iterator.next().also {
			currentIndex++
		}
	}

	override fun decodeElementIndex(descriptor: SerialDescriptor): Int {
		return if (iterator.hasNext()) {
			currentIndex + 1
		}
		else {
			CompositeDecoder.DECODE_DONE
		}
	}

	override fun decodeCollectionSize(descriptor: SerialDescriptor): Int {
		return list.size
	}

	override fun decodeSequentially(): Boolean {
		return true
	}
}