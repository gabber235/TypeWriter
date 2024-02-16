package me.gabber235.typewriter.entries.cinematic

import lol.pyr.znpcsplus.api.NpcApiProvider
import lol.pyr.znpcsplus.api.entity.EntityProperty
import lol.pyr.znpcsplus.api.npc.NpcEntry
import lol.pyr.znpcsplus.api.skin.SkinDescriptor
import lol.pyr.znpcsplus.util.NpcLocation
import lol.pyr.znpcsplus.util.NpcPose
import me.gabber235.typewriter.capture.capturers.ArmSwing
import me.gabber235.typewriter.entry.entries.NpcData
import me.gabber235.typewriter.logger
import org.bukkit.Location
import org.bukkit.entity.Player
import org.bukkit.inventory.EquipmentSlot
import org.bukkit.inventory.ItemStack
import java.util.*

interface ZNPCData : NpcData<NpcEntry> {
    override fun spawn(player: Player, npc: NpcEntry, location: Location) {
        if (player.world != location.world) {
            logger.severe(
                """|
                |Player and NPC are not in the same world! Cannot spawn NPC. 
                |(Player: ${player.world}, NPC: ${location.world})
                |Make sure that the player is teleported before the NPC is spawned. 
            """.trimMargin()
            )
        }
        npc.npc.location = NpcLocation(location)
        npc.isProcessed = false
        npc.isSave = false
        npc.npc.show(player)
    }

    override fun handleMovement(player: Player, npc: NpcEntry, location: Location) {
        npc.npc.location = NpcLocation(location)
    }

    override fun handleSneaking(player: Player, npc: NpcEntry, sneaking: Boolean) {
        val property = NpcApiProvider.get().propertyRegistry.getByName("pose", NpcPose::class.java)

        if (sneaking) {
            npc.npc.setProperty(property, NpcPose.CROUCHING)
        } else {
            npc.npc.setProperty(property, NpcPose.STANDING)
        }
    }

    override fun handlePunching(player: Player, npc: NpcEntry, punching: ArmSwing) {
        npc.npc.swingHand(punching.swingLeft)
    }

    override fun handleInventory(player: Player, npc: NpcEntry, slot: EquipmentSlot, itemStack: ItemStack) {
        val zNpcSlot = when (slot) {
            EquipmentSlot.HAND -> "hand"
            EquipmentSlot.OFF_HAND -> "offhand"
            EquipmentSlot.HEAD -> "helmet"
            EquipmentSlot.CHEST -> "chestplate"
            EquipmentSlot.LEGS -> "leggings"
            EquipmentSlot.FEET -> "boots"
        }

        val property = NpcApiProvider.get().propertyRegistry.getByName(zNpcSlot, ItemStack::class.java)
        npc.npc.setItemProperty(property, itemStack)
    }

    override fun teardown(player: Player, npc: NpcEntry) {
        npc.npc.hide(player)
        NpcApiProvider.get().npcRegistry.delete(npc.id)
    }
}

class PlayerNpcData : ZNPCData {
    override fun create(player: Player, location: Location): NpcEntry {
        val api = NpcApiProvider.get()
        val type = api.npcTypeRegistry.getByName("player")
        val entry = api.npcRegistry.create(
            UUID.randomUUID().toString(),
            location.world,
            type,
            NpcLocation(location)
        )

        val npc = entry.npc

        val skin = api.skinDescriptorFactory.createStaticDescriptor(player.name)
        npc.setProperty(api.propertyRegistry.getByName("skin", SkinDescriptor::class.java), skin)

        player.inventory.helmet?.let { handleInventory(player, entry, EquipmentSlot.HEAD, it) }
        player.inventory.chestplate?.let { handleInventory(player, entry, EquipmentSlot.CHEST, it) }
        player.inventory.leggings?.let { handleInventory(player, entry, EquipmentSlot.LEGS, it) }
        player.inventory.boots?.let { handleInventory(player, entry, EquipmentSlot.FEET, it) }

        return entry
    }
}

class ReferenceNpcData(private val npcId: String) : ZNPCData {
    override fun create(player: Player, location: Location): NpcEntry {
        val api = NpcApiProvider.get()
        val npcRegistry = api.npcRegistry

        val original = npcRegistry.getById(npcId) ?: throw IllegalArgumentException("NPC with id $npcId not found.")
        val type = original.npc.type

        val entry = api.npcRegistry.create(
            UUID.randomUUID().toString(),
            location.world,
            type,
            NpcLocation(location)
        )

        for (property in original.npc.appliedProperties) {
            val value = original.npc.getProperty(property)
            entry.npc.setProperty(property as EntityProperty<Any>, value)
        }

        return entry
    }

    override fun spawn(player: Player, npc: NpcEntry, location: Location) {
        val original = NpcApiProvider.get().npcRegistry.getById(npcId)
            ?: throw IllegalArgumentException("NPC with id $npcId not found.")

        original.npc.hide(player)

        super.spawn(player, npc, location)
    }

    override fun teardown(player: Player, npc: NpcEntry) {
        super.teardown(player, npc)

        val original = NpcApiProvider.get().npcRegistry.getById(npcId)
            ?: throw IllegalArgumentException("NPC with id $npcId not found.")

        original.npc.show(player)
    }
}