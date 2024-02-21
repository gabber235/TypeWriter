package me.gabber235.typewriter.entries.artifact

import me.gabber235.typewriter.adapters.Colors
import me.gabber235.typewriter.adapters.Entry
import me.gabber235.typewriter.adapters.Tags
import me.gabber235.typewriter.capture.AssetCapturer
import me.gabber235.typewriter.capture.CapturerCreator
import me.gabber235.typewriter.capture.MultiTapeRecordedCapturer
import me.gabber235.typewriter.capture.RecorderRequestContext
import me.gabber235.typewriter.capture.capturers.*
import me.gabber235.typewriter.entry.entries.ArtifactEntry
import me.gabber235.typewriter.entry.entries.AssetEntry
import me.gabber235.typewriter.entry.entries.getAssetFromFieldValue
import me.gabber235.typewriter.utils.onFail
import org.bukkit.Location
import org.bukkit.inventory.ItemStack
import org.bukkit.inventory.PlayerInventory

@Deprecated("Use the EntityAdapter instead")
@Entry("fancy_npc_movement_artifact", "Movement data for an npc", Colors.PINK, "fa6-solid:person-walking")
@Tags("npc_movement_artifact")
/**
 * The `Npc Movement Artifact` is an artifact that stores the movement data of an NPC.
 * There is no reason to create this on its own.
 * It should always be connected to another entry
 */
class FancyNpcMovementArtifact(
    override val id: String = "",
    override val name: String = "",
    override val artifactId: String = "",
) : ArtifactEntry

data class NpcFrame(
    val location: Location?,
    val sneaking: Boolean?,
    val swing: ArmSwing?,

    val mainHand: ItemStack?,
    val offHand: ItemStack?,
    val helmet: ItemStack?,
    val chestplate: ItemStack?,
    val leggings: ItemStack?,
    val boots: ItemStack?,
)

class NpcRecordedDataCapturer(title: String, entry: AssetEntry) :
    AssetCapturer<Tape<NpcFrame>>(title, entry, NpcTapeCapturer(title)) {
    companion object : CapturerCreator<NpcRecordedDataCapturer> {
        override fun create(context: RecorderRequestContext): Result<NpcRecordedDataCapturer> {
            val entry = getAssetFromFieldValue(context.fieldValue) onFail { return it }

            return Result.success(NpcRecordedDataCapturer(context.title, entry))
        }
    }
}

class NpcTapeCapturer(title: String) : MultiTapeRecordedCapturer<NpcFrame>(title) {
    private val location by tapeCapturer(::LocationTapeCapturer)
    private val sneaking by tapeCapturer(::SneakingTapeCapturer)
    private val swing by tapeCapturer(::SwingTapeCapturer)
    private val mainHand by tapeCapturer(inventorySlotTapeCapturer(PlayerInventory::getItemInMainHand))
    private val offHand by tapeCapturer(inventorySlotTapeCapturer(PlayerInventory::getItemInOffHand))
    private val helmet by tapeCapturer(inventorySlotTapeCapturer(PlayerInventory::getHelmet))
    private val chestplate by tapeCapturer(inventorySlotTapeCapturer(PlayerInventory::getChestplate))
    private val leggings by tapeCapturer(inventorySlotTapeCapturer(PlayerInventory::getLeggings))
    private val boots by tapeCapturer(inventorySlotTapeCapturer(PlayerInventory::getBoots))
    override fun combineFrame(frame: Int): NpcFrame {
        return NpcFrame(
            location = location[frame],
            sneaking = sneaking[frame],
            swing = swing[frame],

            mainHand = mainHand[frame],
            offHand = offHand[frame],
            helmet = helmet[frame],
            chestplate = chestplate[frame],
            leggings = leggings[frame],
            boots = boots[frame],
        )
    }
}
