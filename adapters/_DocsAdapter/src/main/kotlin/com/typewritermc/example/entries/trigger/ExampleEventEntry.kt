package com.typewritermc.example.entries.trigger

import me.gabber235.typewriter.adapters.Colors
import me.gabber235.typewriter.adapters.Entry
import me.gabber235.typewriter.entry.*
import me.gabber235.typewriter.entry.entries.EventEntry
import org.bukkit.entity.Player
import org.bukkit.event.HandlerList
import org.bukkit.event.player.PlayerEvent

//<code-block:event_entry>
@Entry("example_event", "An example event entry.", Colors.YELLOW, "material-symbols:bigtop-updates")
class ExampleEventEntry(
    override val id: String = "",
    override val name: String = "",
    override val triggers: List<Ref<TriggerableEntry>> = emptyList(),
) : EventEntry
//</code-block:event_entry>

//<code-block:event_entry_listener>
// Must be scoped to be public
@EntryListener(ExampleEventEntry::class)
fun onEvent(event: SomeBukkitEvent, query: Query<ExampleEventEntry>) {
    // Do something
    val entries = query.find() // Find all the entries of this type, for more information see the Query section
    // Do something with the entries, for example trigger them
    entries triggerAllFor event.player
}
//</code-block:event_entry_listener>

class SomeBukkitEvent(val player: Player) : PlayerEvent(player) {
    override fun getHandlers(): HandlerList = HANDLER_LIST

    companion object {
        @JvmStatic
        val HANDLER_LIST = HandlerList()
    }
}