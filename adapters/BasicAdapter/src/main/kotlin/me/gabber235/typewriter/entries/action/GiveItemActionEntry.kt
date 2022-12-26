package me.gabber235.typewriter.entries.action

import lirand.api.extensions.inventory.meta
import me.gabber235.typewriter.adapters.Colors
import me.gabber235.typewriter.adapters.Entry
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
	override val triggeredBy: List<String>,
	override val criteria: List<Criteria>,
	override val modifiers: List<Modifier>,
	override val triggers: List<String> = emptyList(),
	private val material: Material = Material.AIR,
	private val amount: Int = 1,
	private val displayName: String = "",
	@MultiLine
	val lore: String,
) : ActionEntry {
	override fun execute(player: Player) {
		super.execute(player)
		val item = ItemStack(material, amount).meta<ItemMeta> {
			displayName(this@GiveItemActionEntry.displayName.asMini())
			lore(this@GiveItemActionEntry.lore.split("\n").map { "<gray>$it".asMini() })
		}
		player.inventory.addItem(item)
	}
}