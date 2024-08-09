package me.gabber235.typewriter.entries.data.minecraft.living

import com.github.retrooper.packetevents.protocol.item.ItemStack
import com.github.retrooper.packetevents.protocol.player.EquipmentSlot
import me.gabber235.typewriter.adapters.Colors
import me.gabber235.typewriter.adapters.Entry
import me.gabber235.typewriter.adapters.Tags
import me.gabber235.typewriter.entry.entries.*
import me.gabber235.typewriter.extensions.packetevents.toPacketItem
import me.gabber235.typewriter.utils.Item
import me.tofaa.entitylib.wrapper.WrapperEntity
import me.tofaa.entitylib.wrapper.WrapperLivingEntity
import org.bukkit.Material
import org.bukkit.entity.Player
import org.bukkit.inventory.EntityEquipment
import java.util.*
import kotlin.reflect.KClass

@Entry("equipment_data", "Equipment data", Colors.RED, "game-icons:chest-armor")
@Tags("equipment_data")
class EquipmentData(
    override val id: String = "",
    override val name: String = "",
    val equipment: Map<EquipmentSlot, Item> = emptyMap(),
    override val priorityOverride: Optional<Int>,
) : LivingEntityData<EquipmentProperty> {
    override fun type(): KClass<EquipmentProperty> = EquipmentProperty::class

    override fun build(player: Player): EquipmentProperty =
        EquipmentProperty(equipment.mapValues { (_, item) -> item.build(player).toPacketItem() })
}

data class EquipmentProperty(val data: Map<EquipmentSlot, ItemStack>) : EntityProperty {
    constructor(equipmentSlot: EquipmentSlot, item: ItemStack) : this(mapOf(equipmentSlot to item))

    companion object : PropertyCollectorSupplier<EquipmentProperty> {
        override val type: KClass<EquipmentProperty> = EquipmentProperty::class

        override fun collector(suppliers: List<PropertySupplier<out EquipmentProperty>>): PropertyCollector<EquipmentProperty> {
            return EquipmentCollector(suppliers.filterIsInstance<PropertySupplier<EquipmentProperty>>())
        }
    }
}

fun EntityEquipment.toProperty(): EquipmentProperty {
    return EquipmentProperty(org.bukkit.inventory.EquipmentSlot.entries.mapNotNull {
        // TODO: Replace this with `EquipmentSlot.BODY` when the support for 1.20.4 is dropped.
        if (it.name.contains("BODY")) return@mapNotNull null
        val item = getItem(it)
        if (item.isEmpty) return@mapNotNull null
        it.toPacketEquipmentSlot() to getItem(it).toPacketItem()
    }.toMap())
}

fun org.bukkit.inventory.EquipmentSlot.toPacketEquipmentSlot() = when (this) {
    org.bukkit.inventory.EquipmentSlot.HAND -> EquipmentSlot.MAIN_HAND
    org.bukkit.inventory.EquipmentSlot.OFF_HAND -> EquipmentSlot.OFF_HAND
    org.bukkit.inventory.EquipmentSlot.HEAD -> EquipmentSlot.HELMET
    org.bukkit.inventory.EquipmentSlot.CHEST -> EquipmentSlot.CHEST_PLATE
    org.bukkit.inventory.EquipmentSlot.LEGS -> EquipmentSlot.LEGGINGS
    org.bukkit.inventory.EquipmentSlot.FEET -> EquipmentSlot.BOOTS
    else -> EquipmentSlot.CHEST_PLATE
}

private class EquipmentCollector(
    private val suppliers: List<PropertySupplier<EquipmentProperty>>,
) : PropertyCollector<EquipmentProperty> {
    override val type: KClass<EquipmentProperty> = EquipmentProperty::class

    override fun collect(player: Player): EquipmentProperty {
        val properties = suppliers.filter { it.canApply(player) }
            .map { it.build(player) }
        val data = mutableMapOf<EquipmentSlot, ItemStack>()
        properties.asSequence()
            .flatMap { it.data.asSequence() }
            .filter { (slot, _) -> !data.containsKey(slot) }
            .forEach { (slot, item) -> data[slot] = item }

        // Reset empty slots
        EquipmentSlot.entries.filter { !data.containsKey(it) && EquipmentSlot.BODY != it }.forEach { data[it] = ItemStack.EMPTY }

        return EquipmentProperty(data)
    }
}

fun org.bukkit.inventory.ItemStack.toProperty(equipmentSlot: EquipmentSlot) =
    EquipmentProperty(equipmentSlot, this.toPacketItem())

fun applyEquipmentData(entity: WrapperEntity, property: EquipmentProperty) {
    if (entity !is WrapperLivingEntity) return
    property.data.forEach { (slot, item) ->
        if (item.isEmpty) entity.equipment.setItem(slot, null)
        else entity.equipment.setItem(slot, item)
    }
}