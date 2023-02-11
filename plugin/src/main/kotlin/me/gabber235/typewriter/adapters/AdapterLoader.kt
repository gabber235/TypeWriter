package me.gabber235.typewriter.adapters

import com.google.gson.GsonBuilder
import com.google.gson.JsonArray
import com.google.gson.reflect.TypeToken
import me.gabber235.typewriter.Typewriter.Companion.plugin
import me.gabber235.typewriter.entry.dialogue.DialogueMessenger
import me.gabber235.typewriter.entry.entries.DialogueEntry
import me.gabber235.typewriter.utils.*
import java.io.File
import java.net.URLClassLoader
import java.util.jar.JarEntry
import java.util.jar.JarFile
import kotlin.reflect.KClass
import kotlin.reflect.full.*

private val gson =
	GsonBuilder().registerTypeAdapterFactory(
		RuntimeTypeAdapterFactory.of(FieldInfo::class.java, "kind")
			.registerSubtype(PrimitiveField::class.java, "primitive")
			.registerSubtype(EnumField::class.java, "enum")
			.registerSubtype(ListField::class.java, "list")
			.registerSubtype(MapField::class.java, "map")
			.registerSubtype(ObjectField::class.java, "object")
			.registerSubtype(CustomField::class.java, "custom")
	).enableComplexMapKeySerialization()
		.create()

object AdapterLoader {
	private var adapters = listOf<AdapterData>()
	fun loadAdapters() {
		adapters = plugin.dataFolder["adapters"].listFiles()?.filter { it.extension == "jar" }?.mapNotNull {
			plugin.logger.info("Loading adapter ${it.nameWithoutExtension}")
			try {
				loadAdapter(it)
			} catch (e: ClassNotFoundException) {
				plugin.logger.warning("Failed to load adapter ${it.nameWithoutExtension}. Error: ${e.message}. This is likely due to a missing dependency. Skipping...")
				null
			} catch (e: Exception) {
				plugin.logger.warning("Failed to load adapter ${it.nameWithoutExtension}. Skipping...")
				e.printStackTrace()
				null
			}
		} ?: listOf()

		// Write the adapters to a file
		val file = plugin.dataFolder["adapters.json"]
		if (!file.exists()) {
			// Make sure the parent directory exists
			file.parentFile.mkdirs()
			file.createNewFile()
		}

		val jsonArray = JsonArray()

		adapters.forEach {
			jsonArray.add(gson.toJsonTree(it))
		}

		file.writeText(jsonArray.toString())
	}

	fun initializeAdapters() {
		adapters.forEach {
			if (TypewriteAdapter::class.isSuperclassOf(it.clazz.kotlin)) {
				TypewriteAdapter::class.cast(it.clazz.kotlin.objectInstance).initialize()
			}
		}
	}

	private val ignorePrefixes = listOf("kotlin", "java", "META-INF", "org/bukkit", "org/intellij", "org/jetbrains")

	private fun loadAdapter(file: File): AdapterData {
		val classes = loadClasses(file)

		val adapterClass: Class<*> = classes.firstOrNull { it.hasAnnotation(Adapter::class) }
			?: throw IllegalArgumentException("No adapter class found in ${file.name}")

		val entryClasses = classes.filter { it.hasAnnotation(Entry::class) }
		val messengerClasses = classes.filter { it.hasAnnotation(Messenger::class) }


		return constructAdapter(classes, adapterClass, entryClasses, messengerClasses)
	}

	private fun constructAdapter(
		classes: List<Class<*>>,
		adapterClass: Class<*>,
		entryClasses: List<Class<*>>,
		messengerClasses: List<Class<*>>
	): AdapterData {
		val adapterAnnotation = adapterClass.getAnnotation(Adapter::class.java)

		// Entries info
		val blueprints = constructEntryBlueprints(entryClasses)

		// Messengers info
		val messengers = constructMessengers(messengerClasses)

		val adapterListeners = AdapterListeners.constructAdapterListeners(classes)

		// Create the adapter data
		return AdapterData(
			adapterAnnotation?.name ?: "",
			adapterAnnotation?.description ?: "",
			adapterAnnotation?.version ?: "",
			blueprints,
			messengers,
			adapterListeners,
			adapterClass,
		)
	}

	private fun constructEntryBlueprints(entryClasses: List<Class<*>>) =
		entryClasses.filter { me.gabber235.typewriter.entry.Entry::class.java.isAssignableFrom(it) }
			.map { it as Class<out me.gabber235.typewriter.entry.Entry> }
			.map { entryClass ->
				val entryAnnotation = entryClass.getAnnotation(Entry::class.java)

				EntryBlueprint(
					entryAnnotation.name,
					entryAnnotation.description,
					ObjectField.fromTypeToken(TypeToken.get(entryClass)),
					entryAnnotation.color,
					entryAnnotation.icon.id,
					getTags(entryClass),
					entryClass,
				)
			}

	private fun constructMessengers(messengerClasses: List<Class<*>>) =
		messengerClasses.map { messengerClass ->
			val messengerAnnotation = messengerClass.getAnnotation(Messenger::class.java)

			val filter = findFilterForMessenger(messengerClass)

			MessengerData(
				messengerClass as Class<out DialogueMessenger<*>>,
				messengerAnnotation.dialogue.java,
				filter as Class<out MessengerFilter>,
				messengerAnnotation.priority,
			)
		}

	//TODO: Make compatible with java.
	private fun findFilterForMessenger(messengerClass: Class<*>) =
		if (messengerClass.kotlin.companionObject?.isSubclassOf(MessengerFilter::class) == true) {
			messengerClass.kotlin.companionObject!!.java
		} else {
			EmptyMessengerFilter::class.java
		}

	private fun loadClasses(file: File): List<Class<*>> {
		val jarFile = JarFile(file)
		val loader = URLClassLoader(arrayOf(file.toURI().toURL()), javaClass.classLoader)
		val entries = jarFile.entries()

		val classes = mutableListOf<Class<*>>()
		while (entries.hasMoreElements()) {
			val entry = entries.nextElement()
			if (isClassFile(entry) && notIgnored(entry)) {
				val className = entry.name.replace("/", ".").substring(0, entry.name.length - 6)
				classes.add(loader.loadClass(className))
			}
		}

		return classes
	}

	private fun Class<*>.hasAnnotation(annotation: KClass<out Annotation>): Boolean {
		return isAnnotationPresent(annotation.java)
	}

	private fun notIgnored(entry: JarEntry) = ignorePrefixes.none { entry.name.startsWith(it) }

	private fun isClassFile(entry: JarEntry) = entry.name.endsWith(".class")

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
	val eventListeners: List<AdapterListener>,
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
	val color: String, // Hex color
	val icon: Icons, // Font awesome icon
)

@Target(AnnotationTarget.CLASS)
annotation class Messenger(
	val dialogue: KClass<out DialogueEntry>,
	val priority: Int = 0,
)

