package me.gabber235.typewriter.capture.capturers

import me.gabber235.typewriter.capture.ImmediateCapturer
import me.gabber235.typewriter.utils.Item
import me.gabber235.typewriter.utils.toItem
import org.bukkit.entity.Player

class ItemSnapshotCapturer(override val title: String) : ImmediateCapturer<Item> {
    override fun capture(player: Player): Item {
        return player.inventory.itemInMainHand.toItem()
    }
}