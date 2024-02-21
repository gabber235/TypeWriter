package me.gabber235.typewriter.citizens.entries.cinematic

import me.gabber235.typewriter.capture.capturers.ArmSwing
import me.gabber235.typewriter.citizens.CitizensAdapter.temporaryRegistry
import me.gabber235.typewriter.utils.ThreadType
import net.citizensnpcs.api.CitizensAPI
import net.citizensnpcs.api.npc.NPC
import net.citizensnpcs.api.trait.trait.Equipment
import net.citizensnpcs.api.trait.trait.Equipment.EquipmentSlot.*
import net.citizensnpcs.api.trait.trait.MobType
import net.citizensnpcs.api.trait.trait.PlayerFilter
import net.citizensnpcs.trait.HologramTrait
import net.citizensnpcs.trait.SkinTrait
import net.citizensnpcs.trait.SneakTrait
import org.bukkit.Location
import org.bukkit.entity.EntityType
import org.bukkit.entity.LivingEntity
import org.bukkit.entity.Player
import org.bukkit.inventory.EquipmentSlot
import org.bukkit.inventory.ItemStack


interface CitizensNpcData : NpcData<NPC> {
    override val threadType: ThreadType
        get() = ThreadType.SYNC

    override fun spawn(player: Player, npc: NPC, location: Location) {
        val filter = npc.getOrAddTrait(PlayerFilter::class.java)
        filter.setAllowlist()
        filter.addPlayer(player.uniqueId)
        npc.spawn(location)
    }

    override fun handleMovement(player: Player, npc: NPC, location: Location) {
        npc.entity?.teleport(location)
    }

    override fun handleSneaking(player: Player, npc: NPC, sneaking: Boolean) {
        val sneakingTrait = npc.getOrAddTrait(SneakTrait::class.java)
        if (sneakingTrait.isSneaking != sneaking) {
            sneakingTrait.isSneaking = sneaking
        }
    }

    override fun handlePunching(player: Player, npc: NPC, punching: ArmSwing) {
        val entity = npc.entity ?: return
        if (entity !is LivingEntity) return
        if (punching.swingLeft) entity.swingOffHand()
        if (punching.swingRight) entity.swingMainHand()
    }

    override fun handleInventory(player: Player, npc: NPC, slot: EquipmentSlot, itemStack: ItemStack) {
        val equipmentTrait: Equipment = npc.getOrAddTrait(Equipment::class.java)
        val citizensSlot = when (slot) {
            EquipmentSlot.HAND -> HAND
            EquipmentSlot.OFF_HAND -> OFF_HAND
            EquipmentSlot.HEAD -> HELMET
            EquipmentSlot.CHEST -> CHESTPLATE
            EquipmentSlot.LEGS -> LEGGINGS
            EquipmentSlot.FEET -> BOOTS
        }
        equipmentTrait.set(citizensSlot, itemStack)
    }

    override fun teardown(player: Player, npc: NPC) {
        npc.destroy()
    }
}

class PlayerNpcData : CitizensNpcData {
    override fun create(player: Player, location: Location): NPC {
        val npc = temporaryRegistry.createNPC(EntityType.PLAYER, player.name)

        npc.getOrAddTrait(SkinTrait::class.java).skinName = player.name

        npc.getOrAddTrait(Equipment::class.java).apply {
            set(HELMET, player.inventory.helmet)
            set(CHESTPLATE, player.inventory.chestplate)
            set(LEGGINGS, player.inventory.leggings)
            set(BOOTS, player.inventory.boots)
        }

        npc.data()[NPC.Metadata.NAMEPLATE_VISIBLE] = false

        return npc
    }
}

data class ReferenceNpcData(val id: Int) : CitizensNpcData {
    override fun create(player: Player, location: Location): NPC {
        val original =
            CitizensAPI.getNPCRegistry().getById(id) ?: throw IllegalArgumentException("NPC with id $id not found.")

        val type = original.getOrAddTrait(MobType::class.java).type
        val npc = temporaryRegistry.createNPC(type, original.name)

        if (original.hasTrait(SkinTrait::class.java)) {
            val originalSkin = original.getOrAddTrait(SkinTrait::class.java)

            if (originalSkin.signature == null || originalSkin.texture == null) {
                npc.getOrAddTrait(SkinTrait::class.java).skinName = originalSkin.skinName ?: ""
            } else {
                npc.getOrAddTrait(SkinTrait::class.java)
                    .setSkinPersistent(originalSkin.skinName ?: "", originalSkin.signature, originalSkin.texture)
            }
        }

        if (original.requiresNameHologram()) {
            npc.setAlwaysUseNameHologram(true)
            npc.name = original.fullName
        }

        if (original.hasTrait(HologramTrait::class.java)) {
            val originalHologram = original.getOrAddTrait(HologramTrait::class.java)

            npc.getOrAddTrait(HologramTrait::class.java).apply {
                lineHeight = originalHologram.lineHeight
                originalHologram.lines.forEach { addLine(it) }
            }
        }

        val namePlateVisible = original.data().get(NPC.Metadata.NAMEPLATE_VISIBLE, true).toString().toBoolean()
        npc.data()[NPC.Metadata.NAMEPLATE_VISIBLE] = namePlateVisible

        return npc
    }

    override fun spawn(player: Player, npc: NPC, location: Location) {
        val original =
            CitizensAPI.getNPCRegistry().getById(id) ?: throw IllegalArgumentException("NPC with id $id not found.")
        val filter = original.getOrAddTrait(PlayerFilter::class.java)

        if (filter.isAllowlist) {
            filter.removePlayer(player.uniqueId)
        } else {
            filter.addPlayer(player.uniqueId)
        }

        super.spawn(player, npc, location)
    }

    override fun teardown(player: Player, npc: NPC) {
        super.teardown(player, npc)

        val original =
            CitizensAPI.getNPCRegistry().getById(id) ?: throw IllegalArgumentException("NPC with id $id not found.")
        val filter = original.getOrAddTrait(PlayerFilter::class.java)
        if (filter.isAllowlist) {
            filter.addPlayer(player.uniqueId)
        } else {
            filter.removePlayer(player.uniqueId)
        }
    }
}
