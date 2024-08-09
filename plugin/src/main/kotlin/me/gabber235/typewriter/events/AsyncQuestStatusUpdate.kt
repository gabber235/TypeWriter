package me.gabber235.typewriter.events

import me.gabber235.typewriter.entry.Ref
import me.gabber235.typewriter.entry.entries.QuestEntry
import me.gabber235.typewriter.entry.quest.QuestStatus
import org.bukkit.entity.Player
import org.bukkit.event.HandlerList
import org.bukkit.event.player.PlayerEvent

class AsyncQuestStatusUpdate(
    player: Player,
    val quest: Ref<QuestEntry>,
    val from: QuestStatus,
    val to: QuestStatus,
) :
    PlayerEvent(player, true) {
    override fun getHandlers(): HandlerList = HANDLER_LIST

    companion object {
        @JvmStatic
        val HANDLER_LIST = HandlerList()

        @JvmStatic
        fun getHandlerList(): HandlerList = HANDLER_LIST
    }
}