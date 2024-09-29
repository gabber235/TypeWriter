package com.typewritermc.engine.paper.entry

import com.typewritermc.core.entries.Entry
import com.typewritermc.core.entries.Ref
import com.typewritermc.core.extension.annotations.Help
import com.typewritermc.core.extension.annotations.Tags
import com.typewritermc.engine.paper.entry.entries.EntryTrigger
import com.typewritermc.engine.paper.entry.entries.EventTrigger
import com.typewritermc.engine.paper.entry.entries.SystemTrigger
import com.typewritermc.engine.paper.interaction.InteractionHandler
import org.bukkit.entity.Player
import org.koin.java.KoinJavaComponent.get

@Tags("trigger")
interface TriggerEntry : Entry {
    @Help("The entries that will be fired after this entry.")
    val triggers: List<Ref<TriggerableEntry>>
}

@Tags("triggerable")
interface TriggerableEntry : TriggerEntry {
    @Help("The criteria that must be met before this entry is triggered")
    val criteria: List<Criteria>

    @Help("The modifiers that will be applied when this entry is triggered")
    val modifiers: List<Modifier>
}

/**
 * Trigger all triggers for all entries in a list.
 *
 * Example:
 * ```kotlin
 * val entries: List<SomeEntry> = ...
 * entries triggerAllFor player
 * ```
 *
 * @param player The player to trigger the triggers for.
 */
infix fun <E : TriggerEntry> List<E>.triggerAllFor(player: Player) {
    val triggers = this.flatMap { it.triggers }.map { EntryTrigger(it) }
    if (triggers.isEmpty()) return
    get<InteractionHandler>(InteractionHandler::class.java).triggerActions(player, triggers)
}

/**
 * Trigger all triggers for all entries in a sequence.
 *
 * Example:
 * ```kotlin
 * val entries: Sequence<SomeEntry> = ...
 * entries triggerAllFor player
 * ```
 *
 * @param player The player to trigger the triggers for.
 */
infix fun <E : TriggerEntry> Sequence<E>.triggerAllFor(player: Player) {
    val triggers = this.flatMap { it.triggers }.map { EntryTrigger(it) }.toList()
    if (triggers.isEmpty()) return
    get<InteractionHandler>(InteractionHandler::class.java).triggerActions(player, triggers)
}

/**
 * Trigger all triggers for an entry.
 *
 * Example:
 * ```kotlin
 * val entry: SomeEntry = ...
 * entry triggerAllFor player
 * ```
 *
 * @param player The player to trigger the triggers for.
 */
infix fun <E : TriggerEntry> E.triggerAllFor(player: Player) {
    val triggers = this.triggers.map { EntryTrigger(it) }
    if (triggers.isEmpty()) return
    get<InteractionHandler>(InteractionHandler::class.java).triggerActions(player, triggers)
}

/**
 * Trigger all triggers for all entries in a list.
 * This is a convenience method for [triggerAllFor] that takes a list of [Ref].
 *
 * Example:
 * ```kotlin
 * val triggers: List<Ref<TriggerableEntry>> = ...
 * triggers triggerEntriesFor player
 * ```
 *
 * @param player The player to trigger the triggers for.
 */
infix fun List<Ref<out TriggerableEntry>>.triggerEntriesFor(player: Player) {
    val triggers = this.map { EntryTrigger(it) }
    if (triggers.isEmpty()) return
    get<InteractionHandler>(InteractionHandler::class.java).triggerActions(player, triggers)
}

/**
 * Trigger all triggers for all entries in a sequence.
 * This is a convenience method for [triggerAllFor] that takes a sequence of [Ref].
 *
 * Example:
 * ```kotlin
 * val triggers: Sequence<Ref<TriggerableEntry>> = ...
 * triggers triggerEntriesFor player
 * ```
 *
 * @param player The player to trigger the triggers for.
 */
infix fun Sequence<Ref<out TriggerableEntry>>.triggerEntriesFor(player: Player) {
    val triggers = this.map { EntryTrigger(it) }.toList()
    if (triggers.isEmpty()) return
    get<InteractionHandler>(InteractionHandler::class.java).triggerActions(player, triggers)
}

/**
 * Trigger all triggers for an entry.
 * This is a convenience method for [triggerAllFor] that takes a [Ref].
 *
 * Example:
 * ```kotlin
 * val trigger: Ref<TriggerableEntry> = ...
 * trigger triggerFor player
 * ```
 *
 * @param player The player to trigger the trigger for.
 */
infix fun Ref<out TriggerableEntry>.triggerFor(player: Player) {
    EntryTrigger(this) triggerFor player
}

/**
 * Trigger a specific trigger for a player.
 *
 * Example:
 * ```kotlin
 * val trigger: EventTrigger = ...
 * trigger triggerFor player
 * ```
 *
 * @param player The player to trigger the trigger for.
 */
infix fun EventTrigger.triggerFor(player: Player) = listOf(this) triggerFor player

/**
 * Forcefully Trigger a specific trigger for a player.
 *
 * This will bypass the event queue and execute the event immediately.
 * This is useful for events that need to be executed immediately.
 * **This should only be used sparingly.**
 *
 * Example:
 * ```kotlin
 * val trigger: EventTrigger = ...
 * trigger forceTriggerFor player
 * ```
 */
suspend infix fun EventTrigger.forceTriggerFor(player: Player) = listOf(this) forceTriggerFor player

/**
 * Trigger all triggers for a player.
 *
 * Example:
 * ```kotlin
 * val triggers: List<EventTrigger> = ...
 * triggers triggerAllFor player
 * ```
 *
 * @param player The player to trigger the triggers for.
 */
infix fun List<EventTrigger>.triggerFor(player: Player) {
    get<InteractionHandler>(InteractionHandler::class.java).triggerActions(player, this)
}

/**
 * Forcefully Trigger all triggers for a player.
 *
 * This will bypass the event queue and execute the event immediately.
 * This is useful for events that need to be executed immediately.
 * **This should only be used sparingly.**
 *
 * Example:
 * ```kotlin
 * val triggers: List<EventTrigger> = ...
 * triggers forceTriggerFor player
 * ```
 */
suspend infix fun List<EventTrigger>.forceTriggerFor(player: Player) {
    get<InteractionHandler>(InteractionHandler::class.java).forceTriggerActions(player, this)
}


/**
 * If the player is not in a dialogue, trigger all triggers for all entries in a list.
 * If the player is in a dialogue, only trigger the [continueTrigger].
 *
 * This can be useful for actions that should not be triggered again if the player is already in a dialogue.
 * Like clicking on a npc to start a conversation. As we don't want to start the conversation again
 * if the player is already in a dialogue.
 *
 * Example:
 * ```kotlin
 * val entries: List<SomeEntry> = ...
 * entries.startDialogueWithOrTrigger(player, continueTrigger)
 * ```
 */
fun <E : TriggerEntry> List<E>.startDialogueWithOrTrigger(player: Player, continueTrigger: EventTrigger) {
    val triggers = this.flatMap { it.triggers }.map { EntryTrigger(it) }
    if (triggers.isEmpty()) return
    get<InteractionHandler>(InteractionHandler::class.java).startDialogueWithOrTriggerEvent(
        player,
        triggers,
        continueTrigger
    )
}


/**
 * If the player is not in a dialogue, trigger all triggers for all entries in a list.
 * If the player is in a dialogue, only trigger the [continueTrigger].
 *
 * This can be useful for actions that should not be triggered again if the player is already in a dialogue.
 * Like clicking on a npc to start a conversation. As we don't want to start the conversation again
 * if the player is already in a dialogue.
 *
 * Example:
 * ```kotlin
 * val entries: Sequence<SomeEntry> = ...
 * entries.startDialogueWithOrTrigger(player, continueTrigger)
 * ```
 */
fun <E : TriggerEntry> Sequence<E>.startDialogueWithOrTrigger(player: Player, continueTrigger: EventTrigger) =
    toList().startDialogueWithOrTrigger(player, continueTrigger)

/**
 * If the player is not in a dialogue, trigger all triggers for all entries in a list.
 * If the player is in a dialogue, it will trigger the next dialogue.
 *
 * This can be useful for actions that should not be triggered again if the player is already in a dialogue.
 * Like clicking on a npc to start a conversation. As we don't want to start the conversation again
 * if the player is already in a dialogue.
 *
 * Example:
 * ```kotlin
 * val entries: List<SomeEntry> = ...
 * entries startDialogueWithOrTrigger player
 * ```
 */
infix fun <E : TriggerEntry> List<E>.startDialogueWithOrNextDialogue(player: Player) =
    startDialogueWithOrTrigger(player, SystemTrigger.DIALOGUE_NEXT_OR_COMPLETE)


/**
 * If the player is not in a dialogue, trigger all triggers for all entries in a sequence.
 * If the player is in a dialogue, it will trigger the next dialogue.
 *
 * This can be useful for actions that should not be triggered again if the player is already in a dialogue.
 * Like clicking on a npc to start a conversation.
 * As we don't want to start the conversation again
 * if the player is already in a dialogue.
 *
 * Example:
 * ```kotlin
 * val entries: Sequence<SomeEntry> = ...
 * entries startDialogueWithOrTrigger player
 * ```
 */
infix fun <E : TriggerEntry> Sequence<E>.startDialogueWithOrNextDialogue(player: Player) =
    toList().startDialogueWithOrTrigger(player, SystemTrigger.DIALOGUE_NEXT_OR_COMPLETE)


