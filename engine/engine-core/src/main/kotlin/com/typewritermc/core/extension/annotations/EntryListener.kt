package com.typewritermc.core.extension.annotations

import com.typewritermc.core.entries.Entry
import com.typewritermc.loader.ListenerPriority
import kotlin.reflect.KClass

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
 * IMPORTANT: The function must be a top-level function.
 */
@Target(AnnotationTarget.FUNCTION)
@Retention(AnnotationRetention.RUNTIME)
annotation class EntryListener(
    // TODO: Make this only accept EventEntry
    val entry: KClass<out Entry>,
    val priority: ListenerPriority = ListenerPriority.NORMAL,
    val ignoreCancelled: Boolean = false
)

