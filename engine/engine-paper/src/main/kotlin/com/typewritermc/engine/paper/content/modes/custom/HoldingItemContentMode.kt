package com.typewritermc.engine.paper.content.modes.custom

import com.typewritermc.engine.paper.content.ContentContext
import com.typewritermc.engine.paper.content.modes.ImmediateFieldValueContentMode
import com.typewritermc.engine.paper.utils.Item
import com.typewritermc.engine.paper.utils.toItem
import org.bukkit.entity.Player

class HoldingItemContentMode(context: ContentContext, player: Player) :
    ImmediateFieldValueContentMode<Item>(context, player) {

    override fun value(): Item {
        val item = player.inventory.itemInMainHand
        item.persistentDataContainer
        return item.toItem()
    }
}