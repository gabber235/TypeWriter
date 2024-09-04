package com.typewritermc.engine.paper.entry.roadnetwork.pathfinding

import com.extollit.gaming.ai.path.model.IBlockObject
import com.extollit.gaming.ai.path.model.IInstanceSpace
import org.bukkit.World
import java.util.concurrent.ConcurrentHashMap

class PFInstanceSpace(val world: World) : IInstanceSpace {
    private val chunkSpaces = ConcurrentHashMap<Long, PFColumnarSpace>()

    override fun blockObjectAt(x: Int, y: Int, z: Int): IBlockObject {
        val chunkX = x shr 4
        val chunkZ = z shr 4

        val columnarSpace = columnarSpaceAt(chunkX, chunkZ)
        val relativeX = x and 15
        val relativeY = y and 15
        val relativeZ = z and 15
        return columnarSpace.blockAt(relativeX, relativeY, relativeZ)
    }

    override fun columnarSpaceAt(cx: Int, cz: Int): PFColumnarSpace {
        val key = cx.toLong() and 4294967295L or ((cz.toLong() and 4294967295L) shl 32)
        return chunkSpaces.computeIfAbsent(key) {
            val chunk = world.getChunkAt(cx, cz)
            val snapshot = chunk.getChunkSnapshot(false, false, false)
            PFColumnarSpace(world, snapshot, this)
        }
    }
}