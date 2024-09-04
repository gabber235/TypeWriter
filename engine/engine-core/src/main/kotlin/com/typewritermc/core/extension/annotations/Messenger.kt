package com.typewritermc.core.extension.annotations

import com.typewritermc.core.entries.Entry
import kotlin.reflect.KClass

@Target(AnnotationTarget.CLASS)
/**
 * The [Messenger] annotation is used to mark a class as a messenger for a specific [DialogueEntry].
 * Messengers are responsible for displaying the dialogue to the player.
 *
 * Only one messenger will be selected to display a given [DialogueEntry] to a player.
 * The [priority] property is used to determine which messenger to use.
 * The higher the value, the earlier the messenger will be looked for.
 */
annotation class Messenger(
    // TODO: Only allow DialogueEntry
    val dialogue: KClass<out Entry>,
    val priority: Int = 0,
)