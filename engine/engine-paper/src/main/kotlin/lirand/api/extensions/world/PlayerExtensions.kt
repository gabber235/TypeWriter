package lirand.api.extensions.world

import org.bukkit.Effect
import org.bukkit.Instrument
import org.bukkit.Location
import org.bukkit.Note
import org.bukkit.Particle
import org.bukkit.Sound
import org.bukkit.SoundCategory
import org.bukkit.block.data.BlockData
import org.bukkit.entity.Player
import org.bukkit.inventory.ItemStack
import org.bukkit.inventory.PlayerInventory
import org.bukkit.plugin.Plugin
import org.bukkit.util.Vector

fun PlayerInventory.clearArmor() {
	armorContents = arrayOfNulls(4)
}

fun PlayerInventory.clearAll() {
	clear()
	clearArmor()
}