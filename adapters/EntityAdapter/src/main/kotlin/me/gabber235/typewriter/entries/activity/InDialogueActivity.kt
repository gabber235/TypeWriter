package me.gabber235.typewriter.entries.activity

import me.gabber235.typewriter.adapters.Colors
import me.gabber235.typewriter.adapters.Entry
import me.gabber235.typewriter.adapters.modifiers.Help
import me.gabber235.typewriter.entry.Ref
import me.gabber235.typewriter.entry.dialogue.currentDialogue
import me.gabber235.typewriter.entry.dialogue.speakersInDialogue
import me.gabber235.typewriter.entry.emptyRef
import me.gabber235.typewriter.entry.entity.ActivityContext
import me.gabber235.typewriter.entry.entity.EntityActivity
import me.gabber235.typewriter.entry.entity.LocationProperty
import me.gabber235.typewriter.entry.entity.SingleChildActivity
import me.gabber235.typewriter.entry.entries.DialogueEntry
import me.gabber235.typewriter.entry.entries.EntityActivityEntry
import me.gabber235.typewriter.entry.entries.EntityDefinitionEntry
import me.gabber235.typewriter.entry.entries.GenericEntityActivityEntry
import me.gabber235.typewriter.entry.priority
import me.gabber235.typewriter.utils.logErrorIfNull
import org.bukkit.entity.Player
import java.time.Duration
import java.util.*

@Entry("in_dialogue_activity", "An in dialogue activity", Colors.PALATINATE_BLUE, "bi:chat-square-quote-fill")
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
    @Help("The activity that will be used when the npc is in a dialogue")
    val talkingActivity: Ref<out EntityActivityEntry> = emptyRef(),
    @Help("The activity that will be used when the npc is not in a dialogue")
    val idleActivity: Ref<out EntityActivityEntry> = emptyRef(),
) : GenericEntityActivityEntry {
    override fun create(
        context: ActivityContext,
        currentLocation: LocationProperty
    ): EntityActivity<in ActivityContext> {
        return InDialogueActivity(
            dialogueIdleDuration,
            priority,
            talkingActivity,
            idleActivity,
            currentLocation,
        )
    }
}

class InDialogueActivity(
    private val dialogueIdleDuration: Duration,
    private val priority: Int,
    private val talkingActivity: Ref<out EntityActivityEntry>,
    private val idleActivity: Ref<out EntityActivityEntry>,
    startLocation: LocationProperty,
) : SingleChildActivity<ActivityContext>(startLocation) {
    private val trackers = mutableMapOf<UUID, PlayerDialogueTracker>()

    override fun currentChild(context: ActivityContext): Ref<out EntityActivityEntry> {
        val definition =
            context.instanceRef.get()?.definition.logErrorIfNull("Could not find definition, this should not happen. Please report this on the TypeWriter Discord!")
                ?: return idleActivity
        val inDialogue = context.viewers.filter { it.speakersInDialogue.any { it.id == definition.id } }

        trackers.keys.removeIf { uuid -> inDialogue.none { it.uniqueId == uuid } }

        if (inDialogue.isEmpty()) {
            return idleActivity
        }

        inDialogue.forEach { player ->
            trackers.computeIfAbsent(player.uniqueId) { PlayerDialogueTracker(player.currentDialogue) }.update(player)
        }

        val isTalking = trackers.any { (_, tracker) -> tracker.isActive(dialogueIdleDuration) }
        return if (isTalking) {
            talkingActivity
        } else {
            idleActivity
        }
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

        fun isActive(maxIdleDuration: Duration): Boolean {
            if (maxIdleDuration.isZero) return true
            val dialogue = dialogue ?: return false
            if (dialogue.priority > priority) return true
            return System.currentTimeMillis() - lastInteraction < maxIdleDuration.toMillis()
        }
    }
}