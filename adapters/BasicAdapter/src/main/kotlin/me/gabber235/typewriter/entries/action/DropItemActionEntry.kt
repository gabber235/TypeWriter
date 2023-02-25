package me.gabber235.typewriter.entries.action

import com.github.shynixn.mccoroutine.launch
import lirand.api.extensions.inventory.meta
import me.gabber235.typewriter.Typewriter.Companion.plugin
import me.gabber235.typewriter.adapters.Colors
import me.gabber235.typewriter.adapters.Entry
import me.gabber235.typewriter.adapters.modifiers.Help
import me.gabber235.typewriter.adapters.modifiers.MultiLine
import me.gabber235.typewriter.entry.Criteria
import me.gabber235.typewriter.entry.Modifier
import me.gabber235.typewriter.entry.entries.ActionEntry
import me.gabber235.typewriter.utils.Icons
import me.gabber235.typewriter.utils.asMini
import org.bukkit.Location
import org.bukkit.Material
import org.bukkit.entity.Player
import org.bukkit.inventory.ItemStack
import org.bukkit.inventory.meta.ItemMeta
import java.util.*

@Entry("drop_item", "Drop an item at location, or on player", Colors.RED, Icons.DROPBOX)
class DropItemActionEntry(
	override val id: String = "",
	override val name: String = "",
	override val criteria: List<Criteria>,
	override val modifiers: List<Modifier>,
	override val triggers: List<String> = emptyList(),
	@Help("The item to drop.")
	private val material: Material = Material.AIR,
	@Help("The amount of items to drop.")
	private val amount: Int = 1,
	@Help("The display name of the item. (Defaults to the item's display name)")
	private val displayName: String = "",
	@MultiLine
	@Help("The lore of the item. (Defaults to the item's lore)")
	private val lore: String,
	@Help("The location to drop the item. (Defaults to the player's location)")
	private val location: Optional<Location> = Optional.empty(),
) : ActionEntry {
	override fun execute(player: Player) {
		super.execute(player)
		val item = ItemStack(material, amount).meta<ItemMeta> {
			if (this@DropItemActionEntry.displayName.isNotBlank()) displayName(this@DropItemActionEntry.displayName.asMini())
			if (this@DropItemActionEntry.lore.isNotBlank()) {
				lore(this@DropItemActionEntry.lore.split("\n").map { "<gray>$it".asMini() })
			}
		}
		// Run on main thread
		plugin.launch {
			if (location.isPresent) {
				location.get().world.dropItem(location.get(), item)
			} else {
				player.location.world.dropItem(player.location, item)
			}
		}
	}
}