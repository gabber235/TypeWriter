package me.gabber235.typewriter.entries.cinematic

import de.oliver.fancynpcs.api.FancyNpcsPlugin
import de.oliver.fancynpcs.api.Npc
import de.oliver.fancynpcs.api.utils.NpcEquipmentSlot.*
import de.oliver.fancynpcs.api.utils.SkinFetcher
import me.gabber235.typewriter.capture.capturers.ArmSwing
import me.gabber235.typewriter.extensions.packetevents.swingArm
import me.gabber235.typewriter.utils.ThreadType
import net.kyori.adventure.text.format.NamedTextColor
import org.bukkit.Location
import org.bukkit.entity.EntityType
import org.bukkit.entity.Player
import org.bukkit.inventory.EquipmentSlot
import org.bukkit.inventory.ItemStack
import java.util.*


interface FancyNpcData : NpcData<Npc> {
    override val threadType: ThreadType
        get() = ThreadType.ASYNC

    override fun spawn(player: Player, npc: Npc, location: Location) {
        npc.data.location = location
        npc.spawn(player)
    }

    override fun handleMovement(player: Player, npc: Npc, location: Location) {
        npc.data.location = location
        npc.update(player)
    }

    override fun handleSneaking(player: Player, npc: Npc, sneaking: Boolean) {
        val poseAttr = FancyNpcsPlugin.get().attributeManager.getAttributeByName(EntityType.PLAYER, "pose")
        if (sneaking) {
            npc.data.addAttribute(poseAttr, "CROUCHING")
        } else {
            npc.data.addAttribute(poseAttr, "STANDING")
        }
        npc.update(player)
    }

    override fun handlePunching(player: Player, npc: Npc, punching: ArmSwing) {
        // TODD: Wait for FancyNPCs API to add punching
        // For now we directly send the packet
        player.swingArm(npc.entityId, punching)
    }

    override fun handleInventory(player: Player, npc: Npc, slot: EquipmentSlot, itemStack: ItemStack) {
        val fancySlot = when (slot) {
            EquipmentSlot.HAND -> MAINHAND
            EquipmentSlot.OFF_HAND -> OFFHAND
            EquipmentSlot.HEAD -> HEAD
            EquipmentSlot.CHEST -> CHEST
            EquipmentSlot.LEGS -> LEGS
            EquipmentSlot.FEET -> FEET
        }
        npc.data.equipment[fancySlot] = itemStack
        npc.update(player)
    }

    override fun teardown(player: Player, npc: Npc) {
        npc.remove(player)
    }
}

class PlayerNpcData : FancyNpcData {
    override fun create(player: Player, location: Location): Npc {
        val data = de.oliver.fancynpcs.api.NpcData(player.name, player.uniqueId, location)
        data.skin = SkinFetcher(player.uniqueId.toString())

        data.equipment[HEAD] = player.inventory.helmet
        data.equipment[CHEST] = player.inventory.chestplate
        data.equipment[LEGS] = player.inventory.leggings
        data.equipment[FEET] = player.inventory.boots

        val npc = FancyNpcsPlugin.get().npcAdapter.apply(data)

        npc.isSaveToFile = false
        npc.create()
        return npc
    }
}

class ReferenceNpcData(private val npcId: String) : FancyNpcData {
    override fun create(player: Player, location: Location): Npc {
        val original = FancyNpcsPlugin.get().npcManager.getNpc(npcId)
            ?: throw IllegalArgumentException("NPC with id '$npcId' not found.")
        val ogData = original.data

        val data = de.oliver.fancynpcs.api.NpcData(
            UUID.randomUUID().toString(),
            ogData.name,
            ogData.creator,
            ogData.displayName,
            ogData.skin,
            location,
            false,
            true,
            false,
            false,
            NamedTextColor.WHITE,
            ogData.type,
            ogData.equipment,
            false,
            {},
            emptyList(),
            false,
            "",
            emptyList(),
            0f,
            emptyMap(),
            false
        )

        val npc = FancyNpcsPlugin.get().npcAdapter.apply(data)
        npc.create()
        return npc
    }

    override fun spawn(player: Player, npc: Npc, location: Location) {
        val original = FancyNpcsPlugin.get().npcManager.getNpc(npcId)
            ?: throw IllegalArgumentException("NPC with id '$npcId' not found.")

        original.remove(player)

        super.spawn(player, npc, location)
    }

    override fun teardown(player: Player, npc: Npc) {
        super.teardown(player, npc)

        val original = FancyNpcsPlugin.get().npcManager.getNpc(npcId)
            ?: throw IllegalArgumentException("NPC with id '$npcId' not found.")

        original.spawn(player)
    }
}