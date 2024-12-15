package io.devgiga.gigaextension.entries

import com.typewritermc.core.books.pages.Colors
import com.typewritermc.core.entries.Query
import com.typewritermc.core.entries.Ref
import com.typewritermc.core.extension.annotations.Entry
import com.typewritermc.core.extension.annotations.EntryListener
import com.typewritermc.core.utils.point.Position
import com.typewritermc.engine.paper.entry.TriggerableEntry
import com.typewritermc.engine.paper.entry.entries.EventEntry
import com.typewritermc.engine.paper.entry.startDialogueWithOrNextDialogue
import com.typewritermc.engine.paper.utils.toPosition
import org.bukkit.Location
import org.bukkit.event.player.PlayerInteractEvent
import java.util.*

@Entry("interact_event_entry", "triggers when a player interacts with a block", Colors.YELLOW, "hugeicons:touch-interaction-02")
class InteractEventEntry(
    override val id: String = "",
    override val name: String = "",
    override val triggers: List<Ref<TriggerableEntry>> = emptyList(),
    val location: Optional<Position> = Optional.empty(),
): EventEntry

fun Location.isSameBlock(location: Location): Boolean {
    return this.world == location.world && this.blockX == location.blockX && this.blockY == location.blockY && this.blockZ == location.blockZ
}

@EntryListener(InteractEventEntry::class)
fun onInteractBlock(event: PlayerInteractEvent, query: Query<InteractEventEntry>) {
    val player = event.player
    val block = event.clickedBlock
    val location = block?.location ?: player.location
    // The even triggers twice. Both for the main hand and offhand.
    // We only want to trigger once.
    println("interact detected")
    if (event.hand == org.bukkit.inventory.EquipmentSlot.OFF_HAND) return // Disable off-hand interactions
    val entries = query.findWhere { entry ->
        println("is this working")
        // Check if the player clicked on the correct location
        if (!entry.location.map { it.sameBlock(location.toPosition()) }
                .orElse(true)) return@findWhere false
        true
    }.toList()
    if (entries.isEmpty()) return
    entries startDialogueWithOrNextDialogue player
}