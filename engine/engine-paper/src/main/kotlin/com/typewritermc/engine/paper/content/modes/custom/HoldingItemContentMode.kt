package com.typewritermc.engine.paper.content.modes.custom

import com.typewritermc.engine.paper.content.ContentContext
import com.typewritermc.engine.paper.content.modes.ImmediateFieldValueContentMode
import com.typewritermc.engine.paper.utils.item.Item
import com.typewritermc.engine.paper.utils.item.toItem
import org.bukkit.entity.Player
import java.lang.reflect.Type

class HoldingItemContentMode(context: ContentContext, player: Player) :
    ImmediateFieldValueContentMode<Item>(context, player) {
    override val type: Type = Item::class.java

    override fun value(): Item {
        val item = player.inventory.itemInMainHand
        return item.toItem()
    }
}