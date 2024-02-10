package me.gabber235.typewriter.entries.action

import com.google.gson.annotations.SerializedName
import me.gabber235.typewriter.adapters.Colors
import me.gabber235.typewriter.adapters.Entry
import me.gabber235.typewriter.adapters.modifiers.Help
import me.gabber235.typewriter.entry.*
import me.gabber235.typewriter.entry.entries.CustomTriggeringActionEntry
import me.gabber235.typewriter.entry.entries.GroupEntry
import org.bukkit.entity.Player
import java.util.*

@Entry(
    "group_trigger_action",
    "Trigger the next entries for everyone in the same group as the player",
    Colors.RED,
    "fluent:globe-arrow-forward-16-filled"
)
/**
 * The `Group Trigger Action` is an action that triggers the next entries for everyone in the same group as the player.
 *
 * :::caution
 * The modifiers will only be applied to the player that triggered the action.
 * If you want to modify the other players, you will need to do it in the next entries.
 * :::
 *
 * ## How could this be used?
 * This could be used to trigger the next entries for everyone in the same group as the player,
 * when a player joins a faction, all the other players in the same faction could be notified.
 */
class GroupTriggerActionEntry(
    override val id: String = "",
    override val name: String = "",
    override val criteria: List<Criteria> = emptyList(),
    override val modifiers: List<Modifier> = emptyList(),
    @SerializedName("triggers")
    override val customTriggers: List<Ref<TriggerableEntry>> = emptyList(),
    val group: Ref<GroupEntry> = emptyRef(),
    @Help("The group to trigger the next entries for. If not set, the action will trigger for the group of the player that triggered the action.")
    val forceGroup: Optional<String> = Optional.empty(),
) : CustomTriggeringActionEntry {
    override fun execute(player: Player) {
        super.execute(player)

        val groupEntry = group.get() ?: return

        val group = groupEntry.group(player) ?: return
        group.players.forEach {
            it.triggerCustomTriggers()
        }
    }
}