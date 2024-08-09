package com.caleb.typewriter.worldguard

import com.sk89q.worldedit.util.Location
import com.sk89q.worldguard.LocalPlayer
import com.sk89q.worldguard.protection.ApplicableRegionSet
import com.sk89q.worldguard.protection.regions.ProtectedRegion
import com.sk89q.worldguard.session.MoveType
import com.sk89q.worldguard.session.Session
import com.sk89q.worldguard.session.handler.Handler
import org.bukkit.event.Event
import org.bukkit.event.HandlerList

class WorldGuardHandler(session: Session?) : Handler(session) {

    class Factory : Handler.Factory<WorldGuardHandler>() {
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

        if (!entered.isNullOrEmpty()) entered.let { RegionsEnterEvent(player, it).callEvent() }
        if (!exited.isNullOrEmpty()) exited.let { RegionsExitEvent(player, it).callEvent() }

        return super.onCrossBoundary(player, from, to, toSet, entered, exited, moveType)
    }
}

class RegionsEnterEvent(val player: LocalPlayer, val regions: Set<ProtectedRegion>) : Event() {

    operator fun contains(regionName: String) = regions.any { it.id == regionName }

    companion object {
        @JvmStatic
        val handlerList = HandlerList()
    }

    override fun getHandlers(): HandlerList {
        return handlerList
    }
}

class RegionsExitEvent(val player: LocalPlayer, val regions: Set<ProtectedRegion>) : Event() {

    operator fun contains(regionName: String) = regions.any { it.id == regionName }

    companion object {
        @JvmStatic
        val handlerList = HandlerList()
    }

    override fun getHandlers(): HandlerList {
        return handlerList
    }
}