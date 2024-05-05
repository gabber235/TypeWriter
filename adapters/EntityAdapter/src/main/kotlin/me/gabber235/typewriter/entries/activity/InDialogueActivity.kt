package me.gabber235.typewriter.entries.activity

import me.gabber235.typewriter.adapters.Colors
import me.gabber235.typewriter.adapters.Entry
import me.gabber235.typewriter.adapters.modifiers.Help
import me.gabber235.typewriter.entry.Ref
import me.gabber235.typewriter.entry.descendants
import me.gabber235.typewriter.entry.dialogue.currentDialogue
import me.gabber235.typewriter.entry.entity.*
import me.gabber235.typewriter.entry.entries.AudienceEntry
import me.gabber235.typewriter.entry.entries.DialogueEntry
import me.gabber235.typewriter.entry.entries.EntityActivityEntry
import me.gabber235.typewriter.entry.priority
import me.gabber235.typewriter.entry.ref
import org.bukkit.entity.Player
import java.time.Duration
import java.util.*

@Entry("in_dialogue_activity", "An in dialogue activity", Colors.BLUE, "bi:chat-square-quote-fill")
/**
 * The `InDialogueActivityEntry` is an activity that activates child activities when a player is in a dialogue with the NPC.
 *
 * The activity will only activate when the player is in a dialogue with the NPC.
 *
 * ## How could this be used?
 * This can be used to stop a npc from moving when a player is in a dialogue with it.
 */
class InDialogueActivityEntry(
    override val id: String = "",
    override val name: String = "",
    override val children: List<Ref<out AudienceEntry>> = emptyList(),
    @Help("When a player is considered to be idle in the same dialogue")
    /**
     * The duration a player can be idle in the same dialogue before the activity deactivates.
     *
     * When set to 0, it won't use the timer.
     *
     * <Admonition type="info">
     *     When the dialogue priority is higher than this activity's priority, this timer will be ignored.
     *     And will only exit when the dialogue is finished.
     * </Admonition>
     */
    val dialogueIdleDuration: Duration = Duration.ofSeconds(30),
    override val priorityOverride: Optional<Int> = Optional.empty(),
) : EntityActivityEntry {
    override fun create(context: TaskContext): EntityActivity = InDialogueActivity(
        ref(),
        children
            .descendants(EntityActivityEntry::class)
            .mapNotNull { it.get() }
            .sortedByDescending { it.priority }
            .map { it.create(context) },
        dialogueIdleDuration,
        priority,
    )
}

class InDialogueActivity(
    val ref: Ref<InDialogueActivityEntry>,
    children: List<EntityActivity>,
    private val dialogueIdleDuration: Duration,
    private val priority: Int,
) : FilterActivity(children) {
    private var trackers = mutableMapOf<UUID, PlayerDialogueTracker>()

    override fun canActivate(context: TaskContext, currentLocation: LocationProperty): Boolean {
        if (!ref.canActivateFor(context)) {
            return false
        }
        val definition = context.instanceRef.get()?.definition ?: return false
        val trackingPlayers = context.viewers
            .filter { it.currentDialogue?.speaker == definition }

        val trackingPlayerIds = trackingPlayers.map { it.uniqueId }

        trackers.keys.removeAll { it !in trackingPlayerIds }

        trackingPlayers.forEach { player ->
            trackers.computeIfAbsent(player.uniqueId) { PlayerDialogueTracker(player.currentDialogue) }
                .update(player)
        }

        val canActivate =
            !trackers.all { (_, playerLocation) -> playerLocation.canIgnore(dialogueIdleDuration) } &&
                    super.canActivate(context, currentLocation)
        return canActivate
    }

    private inner class PlayerDialogueTracker(
        var dialogue: DialogueEntry?,
        var lastInteraction: Long = System.currentTimeMillis()
    ) {
        fun update(player: Player) {
            val currentDialogue = player.currentDialogue
            if (dialogue?.id == currentDialogue?.id) return
            lastInteraction = System.currentTimeMillis()
            dialogue = currentDialogue
        }

        fun canIgnore(maxIdleDuration: Duration): Boolean {
            if (maxIdleDuration.isZero) return false
            val dialogue = dialogue ?: return true
            if (dialogue.priority > priority) return false
            return System.currentTimeMillis() - lastInteraction > maxIdleDuration.toMillis()
        }
    }
}