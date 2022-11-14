package me.gabber235.typewriter.adapters

import com.google.gson.GsonBuilder
import com.google.gson.JsonArray
import com.google.gson.reflect.TypeToken
import me.gabber235.typewriter.Typewriter.Companion.plugin
import me.gabber235.typewriter.entry.dialogue.DialogueMessenger
import me.gabber235.typewriter.entry.entries.DialogueEntry
import me.gabber235.typewriter.utils.RuntimeTypeAdapterFactory
import me.gabber235.typewriter.utils.get
import java.io.File
import java.net.URLClassLoader
import java.util.jar.JarFile
import kotlin.reflect.KClass
import kotlin.reflect.full.companionObject
import kotlin.reflect.full.isSubclassOf

private val gson =
	GsonBuilder().registerTypeAdapterFactory(
		RuntimeTypeAdapterFactory.of(FieldType::class.java, "kind")
			.registerSubtype(PrimitiveField::class.java, "primitive")
			.registerSubtype(EnumField::class.java, "enum")
			.registerSubtype(ListField::class.java, "list")
			.registerSubtype(MapField::class.java, "map")
			.registerSubtype(ObjectField::class.java, "object")
	).enableComplexMapKeySerialization()
		.create()

object AdapterLoader {
	private var adapters = listOf<AdapterData>()
	fun loadAdapters() {
		adapters = plugin.dataFolder["adapters"].listFiles()?.mapNotNull {
			if (it.extension == "jar") {
				plugin.logger.info("Loading adapter ${it.nameWithoutExtension}")
				loadAdapter(it)
			} else null
		} ?: listOf()

		// Write the adapters to a file
		val file = plugin.dataFolder["adapters.json"]
		if (!file.exists()) {
			file.createNewFile()
		}

		val jsonArray = JsonArray()

		adapters.forEach {
			jsonArray.add(gson.toJsonTree(it))
		}

		file.writeText(jsonArray.toString())
	}

	private val ignorePrefixes = listOf("kotlin", "java", "META-INF", "org/bukkit", "org/intellij", "org/jetbrains")

	private fun loadAdapter(file: File): AdapterData {
		val jarFile = JarFile(file)
		val loader = URLClassLoader(arrayOf(file.toURI().toURL()), javaClass.classLoader)

		var adapterClass: Class<*>? = null
		val entryClasses = mutableListOf<Class<*>>()
		val messengerClasses = mutableListOf<Class<*>>()

		val jars = jarFile.entries()
		while (jars.hasMoreElements()) {
			val entry = jars.nextElement()
			if (entry.name.endsWith(".class") && ignorePrefixes.none { entry.name.startsWith(it) }) {
				val className = entry.name.replace("/", ".").substring(0, entry.name.length - 6)
				val clazz = loader.loadClass(className)

				if (clazz.isAnnotationPresent(Adapter::class.java)) {
					adapterClass = clazz
				} else if (clazz.isAnnotationPresent(Entry::class.java)) {
					entryClasses.add(clazz)
				} else if (clazz.isAnnotationPresent(Messenger::class.java)) {
					messengerClasses.add(clazz)
				}
			}
		}

		if (adapterClass == null) {
			throw IllegalArgumentException("No adapter class found in ${file.name}")
		}


		val adapter = {
			// Apdapter info
			val adapterAnnotation = adapterClass.getAnnotation(Adapter::class.java)

			// Entries info
			val entries = entryClasses.filter { me.gabber235.typewriter.entry.Entry::class.java.isAssignableFrom(it) }
				.map { it as Class<out me.gabber235.typewriter.entry.Entry> }
				.map { entryClass ->
					val entryAnnotation = entryClass.getAnnotation(Entry::class.java)

					EntryBlueprint(
						entryAnnotation.name,
						entryAnnotation.description,
						ObjectField.fromTypeToken(TypeToken.get(entryClass)),
						entryClass,
					)
				}

			// Messengers info
			val messengers = messengerClasses.map { messengerClass ->
				val messengerAnnotation = messengerClass.getAnnotation(Messenger::class.java)

				//TODO: Make compatible with java.
				val filter = if (messengerClass.kotlin.companionObject?.isSubclassOf(MessengerFilter::class) == true) {
					messengerClass.kotlin.companionObject!!.java
				} else {
					EmptyMessengerFilter::class.java
				}


				MessengerData(
					messengerClass as Class<out DialogueMessenger<*>>,
					messengerAnnotation.dialogue.java,
					filter as Class<out MessengerFilter>,
					messengerAnnotation.priority,
				)
			}

			// Create the adapter data
			AdapterData(
				adapterAnnotation?.name ?: "",
				adapterAnnotation?.description ?: "",
				adapterAnnotation?.version ?: "",
				entries,
				messengers,
				adapterClass,
			)
		}


		return adapter()
	}

	fun getAdapterData(): List<AdapterData> {
		return adapters
	}
}

data class AdapterData(
	val name: String,
	val description: String,
	val version: String,
	val entries: List<EntryBlueprint>,
	@Transient
	val messengers: List<MessengerData>,
	@Transient
	val clazz: Class<*>,
)


// Annotation for marking a class as an adapter
@Target(AnnotationTarget.CLASS)
annotation class Adapter(
	val name: String,
	val description: String,
	val version: String,
)

@Target(AnnotationTarget.CLASS)
annotation class Entry(
	val name: String,
	val description: String,
)

@Target(AnnotationTarget.CLASS)
annotation class Messenger(
	val dialogue: KClass<out DialogueEntry>,
	val priority: Int = 0,
)

