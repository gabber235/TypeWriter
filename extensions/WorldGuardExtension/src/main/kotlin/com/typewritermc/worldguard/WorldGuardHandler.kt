package com.typewritermc.worldguard

import com.sk89q.worldedit.util.Location
import com.sk89q.worldguard.LocalPlayer
import com.sk89q.worldguard.protection.ApplicableRegionSet
import com.sk89q.worldguard.protection.regions.ProtectedRegion
import com.sk89q.worldguard.session.MoveType
import com.sk89q.worldguard.session.Session
import com.sk89q.worldguard.session.handler.Handler
import lirand.api.extensions.server.server
import org.bukkit.entity.Player
import org.bukkit.event.Event
import org.bukkit.event.HandlerList
import org.bukkit.event.player.PlayerEvent

class WorldGuardHandler(session: Session?) : Handler(session) {

    object Factory : Handler.Factory<WorldGuardHandler>() {
        override fun create(session: Session?): WorldGuardHandler {
            return WorldGuardHandler(session)
        }
    }

    override fun onCrossBoundary(
        player: LocalPlayer?,
        from: Location?,
        to: Location?,
        toSet: ApplicableRegionSet?,
        entered: MutableSet<ProtectedRegion>?,
        exited: MutableSet<ProtectedRegion>?,
        moveType: MoveType?
    ): Boolean {
        if (player == null) return false
        val bukkitPlayer = server.getPlayer(player.uniqueId) ?: return false

        if (!entered.isNullOrEmpty()) entered.let {
            RegionsEnterEvent(bukkitPlayer, it).callEvent()
        }
        if (!exited.isNullOrEmpty()) exited.let {
            RegionsExitEvent(bukkitPlayer, it).callEvent()
        }

        return super.onCrossBoundary(player, from, to, toSet, entered, exited, moveType)
    }
}

class RegionsEnterEvent(player: Player, val regions: Set<ProtectedRegion>) : PlayerEvent(player) {
    operator fun contains(regionName: String) = regions.any { it.id == regionName }

    override fun getHandlers(): HandlerList = HANDLER_LIST

    companion object {
        @JvmStatic
        val HANDLER_LIST = HandlerList()

        @JvmStatic
        fun getHandlerList(): HandlerList = HANDLER_LIST
    }
}

class RegionsExitEvent(player: Player, val regions: Set<ProtectedRegion>) : PlayerEvent(player) {
    operator fun contains(regionName: String) = regions.any { it.id == regionName }

    override fun getHandlers(): HandlerList = HANDLER_LIST

    companion object {
        @JvmStatic
        val HANDLER_LIST = HandlerList()

        @JvmStatic
        fun getHandlerList(): HandlerList = HANDLER_LIST
    }
}