package com.typewritermc.engine.paper.utils.item

import com.typewritermc.core.books.pages.Colors
import com.typewritermc.core.extension.annotations.AlgebraicTypeInfo
import com.typewritermc.core.extension.annotations.Default
import com.typewritermc.engine.paper.utils.plainText
import org.bukkit.Material
import org.bukkit.entity.Player
import org.bukkit.inventory.ItemStack
import kotlin.io.encoding.Base64
import kotlin.io.encoding.ExperimentalEncodingApi

@OptIn(ExperimentalEncodingApi::class)
@AlgebraicTypeInfo("serialized_item", Colors.ORANGE, "mingcute:file-code-fill")
class SerializedItem(
    private val material: Material = Material.AIR,
    private val name: String = material.name,

    @Default("1")
    private val amount: Int = 1,

    private val bytes: String = "", // Base64 encoded bytes
) : Item {
    constructor(itemStack: ItemStack) : this(
        itemStack.type,
        itemStack.itemMeta.displayName()?.plainText() ?: itemStack.type.name,
        itemStack.amount,
        Base64.encode(itemStack.serializeAsBytes())
    )

    @delegate:Transient
    private val itemStack: ItemStack by lazy(LazyThreadSafetyMode.NONE) {
        val bytes = Base64.decode(bytes)
        ItemStack.deserializeBytes(bytes).apply {
            amount = this@SerializedItem.amount
        }
    }

    override fun build(player: Player?): ItemStack = itemStack
    override fun isSameAs(player: Player?, item: ItemStack?): Boolean = this.itemStack.isSimilar(item)
}

fun ItemStack.toItem(): SerializedItem = SerializedItem(this)