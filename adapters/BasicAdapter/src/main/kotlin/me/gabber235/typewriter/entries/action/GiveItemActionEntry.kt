package me.gabber235.typewriter.entries.action

import lirand.api.extensions.inventory.meta
import me.gabber235.typewriter.adapters.Colors
import me.gabber235.typewriter.adapters.Entry
import me.gabber235.typewriter.adapters.modifiers.Help
import me.gabber235.typewriter.adapters.modifiers.MultiLine
import me.gabber235.typewriter.entry.Criteria
import me.gabber235.typewriter.entry.Modifier
import me.gabber235.typewriter.entry.entries.ActionEntry
import me.gabber235.typewriter.utils.Icons
import me.gabber235.typewriter.utils.asMini
import org.bukkit.Material
import org.bukkit.entity.Player
import org.bukkit.inventory.ItemStack
import org.bukkit.inventory.meta.ItemMeta

@Entry("give_item", "Give an item to the player", Colors.RED, Icons.WAND_SPARKLES)
class GiveItemActionEntry(
	override val id: String = "",
	override val name: String = "",
	override val criteria: List<Criteria>,
	override val modifiers: List<Modifier>,
	override val triggers: List<String> = emptyList(),
	@Help("The item to give.")
	private val material: Material = Material.AIR,
	@Help("The amount of items to give.")
	private val amount: Int = 1,
	@Help("The display name of the item. (Defaults to the item's display name)")
	private val displayName: String = "",
	@MultiLine
	@Help("The lore of the item. (Defaults to the item's lore)")
	private val lore: String,
) : ActionEntry {
	override fun execute(player: Player) {
		super.execute(player)

		val item = ItemStack(material, amount).meta<ItemMeta> {
			if (this@GiveItemActionEntry.displayName.isNotBlank()) displayName(this@GiveItemActionEntry.displayName.asMini())
			if (this@GiveItemActionEntry.lore.isNotBlank()) {
				lore(
					this@GiveItemActionEntry.lore.split("\n").map { "<gray>$it".asMini() })

			}
		}

		player.inventory.addItem(item)
	}
}