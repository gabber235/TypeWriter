package me.gabber235.typewriter.content

import org.bukkit.entity.Player

interface ContentComponent {
    suspend fun initialize(player: Player)
    suspend fun tick(player: Player)
    suspend fun dispose(player: Player)
}