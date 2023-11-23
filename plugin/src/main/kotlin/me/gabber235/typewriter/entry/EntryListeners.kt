package me.gabber235.typewriter.entry

import lirand.api.extensions.events.listen
import lirand.api.extensions.events.unregister
import me.gabber235.typewriter.adapters.AdapterListener
import me.gabber235.typewriter.adapters.AdapterLoader
import me.gabber235.typewriter.entry.entries.EventEntry
import me.gabber235.typewriter.logger
import me.gabber235.typewriter.plugin
import org.bukkit.event.Event
import org.bukkit.event.EventPriority
import org.bukkit.event.Listener
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject
import java.lang.reflect.Method
import kotlin.reflect.KClass
import kotlin.reflect.full.isSubclassOf
import kotlin.reflect.full.isSuperclassOf

/**
 * Annotate a function to let Typewriter know that it should be called when a specific event occurs.
 *
 * When no entry of the specified class is registered, the bukkit listener will not be registered.
 *
 * The function must have at least one parameter, which is the event that occurred.
 * Other parameters are optional and can be used to inject dependencies.
 * Such as [Query].
 *
 * Example:
 * ```kotlin
 * @EventListener(InteractEventEntry::class)
 * fun onEntryCreated(event: PlayerInteractEvent, query: Query<InteractEventEntry>) {
 *    // ...
 * }
 * ```
 *
 * IMPORTANT: The function must be static.
 */
@Target(AnnotationTarget.FUNCTION)
annotation class EntryListener(
    val entry: KClass<out EventEntry>,
    val priority: EventPriority = EventPriority.NORMAL,
    val ignoreCancelled: Boolean = false
)

/**
 * Manages all the active entry listeners.
 */
class EntryListeners : KoinComponent {
    private val adapterLoader: AdapterLoader by inject()
    private val entryDatabase: EntryDatabase by inject()

    private val listener = object : Listener {}


    private fun onEvent(event: Event, adapterListener: AdapterListener) {
        val parameters = adapterListener.generators.map { it.generate(event, adapterListener) }
        try {
            adapterListener.method.invoke(null, *parameters.toTypedArray())
        } catch (e: Exception) {
            logger.severe("Failed to invoke entry listener ${adapterListener.method.name} for event ${event::class.simpleName}")
            e.printStackTrace()
        }
    }

    /**
     * Registers all the entry listeners.
     */
    fun register() {
        unregister()

        val entryListeners = adapterLoader.adapters.flatMap { it.eventListeners }

        entryDatabase.events.map { it::class }.distinct().mapNotNull { klass ->
            entryListeners.firstOrNull { it.entry.isSuperclassOf(klass) }
        }.forEach {
            val eventClass = findEventFromMethod(it.method) ?: return@forEach

            listener.listen(plugin, eventClass, it.priority, it.ignoreCancelled) { event ->
                onEvent(event, it)
            }
        }
    }

    private fun findEventFromMethod(method: Method): KClass<out Event>? {
        return method.parameters.firstNotNullOfOrNull { it.type.kotlin.takeIf { type -> type.isSubclassOf(Event::class) } } as? KClass<out Event>
    }

    /**
     * Unregisters all the entry listeners.
     */
    fun unregister() {
        listener.unregister()
    }
}