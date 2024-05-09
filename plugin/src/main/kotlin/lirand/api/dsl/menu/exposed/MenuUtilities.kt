package lirand.api.dsl.menu.exposed

import lirand.api.dsl.menu.builders.dynamic.chest.ChestMenuDSL
import lirand.api.dsl.menu.builders.fixed.chest.StaticChestMenuDSL
import lirand.api.dsl.menu.exposed.fixed.StaticMenu
import lirand.api.dsl.menu.exposed.fixed.StaticSlot
import org.bukkit.entity.Player
import org.bukkit.inventory.Inventory

fun ChestMenuDSL.calculateSlot(line: Int, slot: Int) = (line * 9 - 10) + slot
fun StaticChestMenuDSL.calculateSlot(line: Int, slot: Int) = (line * 9 - 10) + slot

fun <S : StaticSlot<I>, I : Inventory> StaticMenu<S, I>.getSlotOrBaseSlot(slot: Int): S = slots[slot] ?: baseSlot


fun <I : Inventory> StaticMenu<*, I>.getViewsFromPlayers(players: Collection<Player>) =
	views.filterKeys { it in players }

fun <S : StaticSlot<I>, I : Inventory> StaticMenu<S, I>.getSlotsWithBaseSlot(): Collection<S> = slots.values + baseSlot

fun StaticMenu<*, *>.hasPlayer(player: Player) = views.containsKey(player)
fun StaticMenu<*, *>.takeIfHasPlayer(player: Player): StaticMenu<*, *>? = if (hasPlayer(player)) this else null

fun Inventory.isMenu() = holder is StaticMenu<*, *>
fun Inventory.asMenu(): StaticMenu<*, *>? = holder as? StaticMenu<*, *>

fun Player.getMenuView(): MenuView<Inventory>? {
	return openInventory.topInventory.asMenu()?.takeIfHasPlayer(this)?.views?.get(this)
}