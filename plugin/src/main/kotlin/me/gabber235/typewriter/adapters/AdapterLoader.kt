package me.gabber235.typewriter.adapters

import com.google.gson.GsonBuilder
import com.google.gson.JsonArray
import com.google.gson.reflect.TypeToken
import me.gabber235.typewriter.adapters.editors.ItemFieldCapturer
import me.gabber235.typewriter.adapters.editors.LocationFieldCapturer
import me.gabber235.typewriter.capture.Capturer
import me.gabber235.typewriter.capture.CapturerCreator
import me.gabber235.typewriter.entry.EntryMigrations
import me.gabber235.typewriter.entry.EntryMigrator
import me.gabber235.typewriter.entry.dialogue.DialogueMessenger
import me.gabber235.typewriter.entry.entries.DialogueEntry
import me.gabber235.typewriter.logger
import me.gabber235.typewriter.plugin
import me.gabber235.typewriter.utils.*
import org.koin.core.component.KoinComponent
import java.io.File
import java.net.URLClassLoader
import java.util.jar.JarEntry
import java.util.jar.JarFile
import kotlin.reflect.KClass
import kotlin.reflect.full.cast
import kotlin.reflect.full.companionObject
import kotlin.reflect.full.isSubclassOf
import kotlin.reflect.full.isSuperclassOf

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

val staticCaptureClasses by lazy {
    listOf(
        LocationFieldCapturer::class,
        ItemFieldCapturer::class,
    )
}

interface AdapterLoader {
    val adapters: List<AdapterData>
    val adaptersJson: JsonArray

    fun loadAdapters()
    fun initializeAdapters()
    fun getEntryBlueprint(type: String): EntryBlueprint?

    fun getCaptureClasses(): List<KClass<out Capturer<*>>>

    fun getEntryMigrators(): List<EntryMigrator>
}

class AdapterLoaderImpl : AdapterLoader, KoinComponent {
    override var adapters = listOf<AdapterData>()
    override var adaptersJson: JsonArray = JsonArray()

    override fun loadAdapters() {
        val dir = plugin.dataFolder["adapters"]
        if (!dir.exists()) {
            dir.mkdirs()
        }

        adapters = dir.listFiles()?.filter { it.extension == "jar" }?.mapNotNull {
            logger.info("Loading adapter ${it.nameWithoutExtension}")
            try {
                loadAdapter(it)
            } catch (e: ClassNotFoundException) {
                logger.warning("Failed to load adapter ${it.nameWithoutExtension}. Error: ${e.message}. This is likely due to a missing dependency. Skipping...")
                null
            } catch (e: Exception) {
                logger.warning("Failed to load adapter ${it.nameWithoutExtension}. Skipping...")
                e.printStackTrace()
                null
            }
        } ?: listOf()

        val jsonArray = JsonArray()

        adapters.forEach {
            jsonArray.add(gson.toJsonTree(it))
        }

        if (adapters.isEmpty()) {
            logger.warning(
                """
                |
                |${"-".repeat(15)}{ No Adapters Loaded }${"-".repeat(15)}
                |
                |No adapters were loaded. 
                |You should always have at least the BasicAdapter loaded.
                |
                |${"-".repeat(50)}
                """.trimMargin()
            )
        } else {
            val maxAdapterLength = adapters.maxOf { it.name.length }
            val maxVersionLength = adapters.maxOf { it.version.length }
            val maxDigits = adapters.maxOf { it.entries.size.digits }
            logger.info(
                """
                |
                |${"-".repeat(15)}{ Loaded Adapters }${"-".repeat(15)}
                |
                |${adapters.joinToString("\n") { it.displayString(maxAdapterLength, maxVersionLength, maxDigits) }}
                |
                |${"-".repeat(50)}
                """.trimMargin()
            )
        }

        adaptersJson = jsonArray
    }

    override fun initializeAdapters() {
        adapters.forEach {
            if (TypewriteAdapter::class.isSuperclassOf(it.clazz.kotlin)) {
                val objectInstance = it.clazz.kotlin.objectInstance
                if (objectInstance == null) {
                    logger.warning("Failed to initialize adapter ${it.name}. Error: ${it.clazz.kotlin.simpleName} is not an object. Skipping initialization...")
                    return@forEach
                }
                TypewriteAdapter::class.cast(objectInstance).initialize()
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
        val captureClasses = classes.filter {
            Capturer::class.java.isAssignableFrom(it) &&
                    it.kotlin.companionObject?.isSubclassOf(CapturerCreator::class) == true
        }


        return constructAdapter(classes, adapterClass, entryClasses, messengerClasses, captureClasses)
    }

    private fun constructAdapter(
        classes: List<Class<*>>,
        adapterClass: Class<*>,
        entryClasses: List<Class<*>>,
        messengerClasses: List<Class<*>>,
        captureClasses: List<Class<*>>,
    ): AdapterData {
        val adapterAnnotation = adapterClass.getAnnotation(Adapter::class.java)

        // Entries info
        val blueprints = constructEntryBlueprints(adapterAnnotation, entryClasses)

        // Messengers info
        val messengers = constructMessengers(messengerClasses)

        val adapterListeners = AdapterListeners.constructAdapterListeners(classes)

        val capturers = constructCapturers(captureClasses)

        val entryMigrators = EntryMigrations.constructEntryMigrators(classes)

        // Create the adapter data
        return AdapterData(
            adapterAnnotation?.name ?: "",
            adapterAnnotation?.description ?: "",
            adapterAnnotation?.version ?: "",
            blueprints,
            messengers,
            adapterListeners,
            capturers,
            entryMigrators,
            adapterClass,
        )
    }

    private fun constructEntryBlueprints(adapter: Adapter, entryClasses: List<Class<*>>) =
        entryClasses.filter { me.gabber235.typewriter.entry.Entry::class.java.isAssignableFrom(it) }
            .map { it as Class<out me.gabber235.typewriter.entry.Entry> }
            .map { entryClass ->
                val entryAnnotation = entryClass.getAnnotation(Entry::class.java)

                EntryBlueprint(
                    entryAnnotation.name,
                    entryAnnotation.description,
                    adapter.name,
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

    private fun constructCapturers(captureClasses: List<Class<*>>) =
        captureClasses.map { captureClass ->
            captureClass.kotlin as KClass<out Capturer<*>>
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
        val loader = URLClassLoader(arrayOf(file.toURI().toURL()), plugin.javaClass.classLoader)
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

    override fun getEntryBlueprint(type: String): EntryBlueprint? {
        return adapters.asSequence().flatMap { it.entries }.firstOrNull { it.name == type }
    }

    override fun getCaptureClasses(): List<KClass<out Capturer<*>>> {
        return adapters.asSequence().flatMap { it.captureClasses }.toList() + staticCaptureClasses
    }

    override fun getEntryMigrators(): List<EntryMigrator> {
        return adapters.asSequence().flatMap { it.entryMigrators }.toList()
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
    val captureClasses: List<KClass<out Capturer<*>>>,
    @Transient
    val entryMigrators: List<EntryMigrator>,
    @Transient
    val clazz: Class<*>,
) {
    /**
     * Returns a string that can be used to display information about the adapter.
     * It is nicely formatted to align the information between adapters.
     */
    fun displayString(maxAdapterLength: Int, maxVersionLength: Int, maxDigits: Int): String {
        var display = "${name}Adapter".rightPad(maxAdapterLength + "Adapter".length)
        display += " (${version})".rightPad(maxVersionLength + 2)
        display += padCount("📚", entries.size, maxDigits)
        display += padCount("👂", eventListeners.size, maxDigits)
        display += padCount("💬", messengers.size, maxDigits)
        display += padCount("📸", captureClasses.size, maxDigits)
        display += padCount("🚚", entryMigrators.size, maxDigits)

        return display
    }

    private fun padCount(prefix: String, count: Int, maxDigits: Int): String {
        return " ${prefix}: ${" ".repeat((maxDigits - count.digits).coerceAtLeast(0))}$count"
    }
}

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

