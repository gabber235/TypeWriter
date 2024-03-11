package me.gabber235.typewriter.content.components

import lirand.api.extensions.inventory.meta
import me.gabber235.typewriter.content.ContentComponent
import me.gabber235.typewriter.content.ContentMode
import me.gabber235.typewriter.content.inLastContentMode
import me.gabber235.typewriter.entry.entries.SystemTrigger
import me.gabber235.typewriter.entry.triggerFor
import me.gabber235.typewriter.utils.loreString
import me.gabber235.typewriter.utils.name
import org.bukkit.Material
import org.bukkit.entity.Player
import org.bukkit.inventory.ItemStack

fun ContentMode.exit() = +ExitComponent()
class ExitComponent : ContentComponent, ItemsComponent {
    override suspend fun initialize(player: Player) {}
    override suspend fun tick(player: Player) {}
    override suspend fun dispose(player: Player) {}

    override fun items(player: Player): Map<Int, IntractableItem> {
        val item = if (player.inLastContentMode) {
            ItemStack(Material.BARRIER).meta {
                name = "<red><bold>Exit Editor"
                loreString = """
                    |
                    |<gray>Click to exit the editor.
                """.trimMargin()
            }
        } else {
            ItemStack(Material.END_CRYSTAL).meta {
                name = "<yellow><bold>Previous Editor"
                loreString = """
                    |
                    |<gray>Click to go back to the previous editor.
                """.trimMargin()
            }
        }

        return mapOf(8 to item {
            SystemTrigger.CONTENT_POP triggerFor player
        })
    }
}