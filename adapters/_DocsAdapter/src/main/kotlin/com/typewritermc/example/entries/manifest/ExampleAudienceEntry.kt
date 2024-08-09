package com.typewritermc.example.entries.manifest

import com.typewritermc.example.entries.trigger.SomeBukkitEvent
import me.gabber235.typewriter.adapters.Colors
import me.gabber235.typewriter.adapters.Entry
import me.gabber235.typewriter.entry.entries.AudienceDisplay
import me.gabber235.typewriter.entry.entries.AudienceEntry
import me.gabber235.typewriter.entry.entries.TickableDisplay
import me.gabber235.typewriter.utils.ThreadType
import org.bukkit.entity.Player
import org.bukkit.event.EventHandler

//<code-block:audience_entry>
@Entry("example_audience", "An example audience entry.", Colors.GREEN, "material-symbols:chat-rounded")
class ExampleAudienceEntry(
    override val id: String,
    override val name: String,
) : AudienceEntry {
    override fun display(): AudienceDisplay {
        return ExampleAudienceDisplay()
    }
}
//</code-block:audience_entry>

//<code-block:audience_display>
class ExampleAudienceDisplay : AudienceDisplay() {
    override fun initialize() {
        // This is called when the first player is added to the audience.
        super.initialize()
        // Do something when the audience is initialized
    }

    override fun onPlayerAdd(player: Player) {
        // Do something when a player gets added to the audience
    }

    override fun onPlayerRemove(player: Player) {
        // Do something when a player gets removed from the audience
    }

    override fun dispose() {
        super.dispose()
        // Do something when the audience is disposed
        // It will always call onPlayerRemove for all players.
        // So no player cleanup is needed here.
    }
}
//</code-block:audience_display>

//<code-block:tickable_audience_display>
// highlight-next-line
class TickableAudienceDisplay : AudienceDisplay(), TickableDisplay {
    override fun onPlayerAdd(player: Player) {}
    override fun onPlayerRemove(player: Player) {}

    // highlight-start
    override fun tick() {
        // Do something when the audience is ticked
        players.forEach { player ->
            // Do something with the player
        }

        // This is running asynchronously
        // If you need to do something on the main thread
        ThreadType.SYNC.launch {
            // Though this will run a tick later, to sync with the bukkit scheduler.
        }
    }
    // highlight-end
}
//</code-block:tickable_audience_display>

//<code-block:audience_display_with_events>
class AudienceDisplayWithEvents : AudienceDisplay() {
    override fun onPlayerAdd(player: Player) {}
    override fun onPlayerRemove(player: Player) {}

    // highlight-start
    @EventHandler
    fun onSomeEvent(event: SomeBukkitEvent) {
        // Do something when the event is triggered
        // This will trigger for all players, not just the ones in the audience.
        // So we need to check if the player is in the audience.
        if (event.player in this) {
            // Do something with the player
        }
    }
    // highlight-end
}
//</code-block:audience_display_with_events>