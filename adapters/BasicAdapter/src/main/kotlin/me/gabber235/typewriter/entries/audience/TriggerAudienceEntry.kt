package me.gabber235.typewriter.entries.audience

import me.gabber235.typewriter.adapters.Colors
import me.gabber235.typewriter.adapters.Entry
import me.gabber235.typewriter.adapters.modifiers.Help
import me.gabber235.typewriter.entry.Ref
import me.gabber235.typewriter.entry.TriggerableEntry
import me.gabber235.typewriter.entry.emptyRef
import me.gabber235.typewriter.entry.entries.AudienceDisplay
import me.gabber235.typewriter.entry.entries.AudienceEntry
import me.gabber235.typewriter.entry.triggerFor
import org.bukkit.entity.Player

@Entry(
    "trigger_audience",
    "Triggers a sequence when the player enters or exits the audience",
    Colors.GREEN,
    "mdi:account-arrow-right"
)
/**
 * The `Trigger Audience` entry is an audience filter that triggers a sequence when the player enters or exits the audience.
 *
 * ## How could this be used?
 * This can be used to bridge the gap between audiences and sequence pages.
 */
class TriggerAudienceEntry(
    override val id: String = "",
    override val name: String = "",
    @Help("The sequence to trigger when the player enters the audience.")
    val onEnter: Ref<TriggerableEntry> = emptyRef(),
    @Help("The sequence to trigger when the player exits the audience.")
    val onExit: Ref<TriggerableEntry> = emptyRef(),
) : AudienceEntry {
    override fun display(): AudienceDisplay = TriggerAudienceDisplay(onEnter, onExit)
}

class TriggerAudienceDisplay(
    private val onEnter: Ref<TriggerableEntry>,
    private val onExit: Ref<TriggerableEntry>,
) : AudienceDisplay() {
    override fun onPlayerAdd(player: Player) {
        onEnter triggerFor player
    }

    override fun onPlayerRemove(player: Player) {
        onExit triggerFor player
    }
}