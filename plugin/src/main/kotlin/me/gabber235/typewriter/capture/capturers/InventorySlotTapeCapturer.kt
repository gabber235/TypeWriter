package me.gabber235.typewriter.capture.capturers

import me.gabber235.typewriter.capture.RecordedCapturer
import org.bukkit.Material
import org.bukkit.entity.Player
import org.bukkit.inventory.ItemStack
import org.bukkit.inventory.PlayerInventory

fun inventorySlotTapeCapturer(itemGetter: (PlayerInventory) -> ItemStack?): (String) -> InventorySlotTapeCapturer {
    return { title ->
        InventorySlotTapeCapturer(title, itemGetter)
    }
}

class InventorySlotTapeCapturer(
    override val title: String,
    val itemGetter: (PlayerInventory) -> ItemStack?
) : RecordedCapturer<Tape<ItemStack>> {
    private val tape = mutableTapeOf<ItemStack>()
    private var lastItem: ItemStack? = ItemStack(Material.AIR, 0)

    override fun startRecording(player: Player) {}

    override fun captureFrame(player: Player, frame: Int) {
        val item = itemGetter(player.inventory)
        if (item?.isSimilar(lastItem) == true) return
        lastItem = item
        if (item == null) return
        tape[frame] = item
    }

    override fun stopRecording(player: Player): Tape<ItemStack> = tape
}