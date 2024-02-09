package me.gabber235.typewriter.entries.action

import com.google.gson.annotations.SerializedName
import me.gabber235.typewriter.adapters.Colors
import me.gabber235.typewriter.adapters.Entry
import me.gabber235.typewriter.entry.Criteria
import me.gabber235.typewriter.entry.Modifier
import me.gabber235.typewriter.entry.Ref
import me.gabber235.typewriter.entry.TriggerableEntry
import me.gabber235.typewriter.entry.entries.AudienceEntry
import me.gabber235.typewriter.entry.entries.CustomTriggeringActionEntry
import me.gabber235.typewriter.utils.Icons
import org.bukkit.entity.Player

@Entry(
    "audience_trigger_action",
    "Trigger the next entries for everyone in the same audience as the player",
    Colors.PURPLE,
    Icons.GLOBE
)
/**
 * The `Audience Trigger Action` is an action that triggers the next entries for everyone in the same audience as the player.
 *
 * :::caution
 * The modifiers will only be applied to the player that triggered the action.
 * If you want to modify the other players, you will need to do it in the next entries.
 * :::
 *
 * ## How could this be used?
 * This could be used to trigger the next entries for everyone in the same audience as the player,
 * when a player joins a faction, all the other players in the same faction could be notified.
 */
class AudienceTriggerActionEntry(
    override val id: String,
    override val name: String,
    override val criteria: List<Criteria>,
    override val modifiers: List<Modifier>,
    @SerializedName("triggers")
    override val customTriggers: List<Ref<TriggerableEntry>>,
    val audience: Ref<AudienceEntry>
) : CustomTriggeringActionEntry {
    override fun execute(player: Player) {
        super.execute(player)

        val audienceEntry = audience.get() ?: return
        val audience = audienceEntry.audience(player) ?: return
        audience.players.forEach {
            it.triggerCustomTriggers()
        }
    }
}