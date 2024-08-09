package me.gabber235.typewriter.entry.roadnetwork.pathfinding

import com.extollit.gaming.ai.path.model.IBlockDescription
import com.extollit.gaming.ai.path.model.IBlockObject
import com.extollit.linalg.immutable.AxisAlignedBBox
import org.bukkit.Location
import org.bukkit.Material
import org.bukkit.block.data.BlockData
import org.bukkit.util.VoxelShape

class PFBlock(
    val location: Location,
    val type: Material,
    val data: BlockData,
) : IBlockDescription, IBlockObject {
    override fun bounds(): AxisAlignedBBox {
        val collisionShape = data.getCollisionShape(location)
        return collisionShape.toAABB()
    }

    override fun isFenceLike(): Boolean {
        if (type.key.key.contains("fence"))
            return true
        if (type.key.key.endsWith("wall"))
            return true
        return false
    }

    override fun isClimbable(): Boolean {
        return type == Material.LADDER || type.key.key.contains("vine")
    }

    override fun isDoor(): Boolean {
        return type.key.key.endsWith("door")
    }

    override fun isIntractable(): Boolean {
        // TODO: Intractability of blocks
        return false
    }

    override fun isImpeding(): Boolean {
        return type.isSolid
    }

    override fun isFullyBounded(): Boolean {
        val voxelShape = data.getCollisionShape(location)
        if (voxelShape.boundingBoxes.size != 1) return false
        val boundingBox = voxelShape.boundingBoxes.first()

        return boundingBox.minX == 0.0
                && boundingBox.minY == 0.0
                && boundingBox.minZ == 0.0
                && boundingBox.widthX == 1.0
                && boundingBox.height == 1.0
                && boundingBox.widthZ == 1.0
    }

    override fun isLiquid(): Boolean {
        return type == Material.WATER || type == Material.LAVA
    }

    override fun isIncinerating(): Boolean {
        return type == Material.LAVA || type == Material.FIRE || type == Material.SOUL_FIRE || type == Material.MAGMA_BLOCK
    }
}

private fun VoxelShape.toAABB(): AxisAlignedBBox {
    val boundingBoxes = boundingBoxes

    if (boundingBoxes.isEmpty()) return AxisAlignedBBox(0.0, 0.0, 0.0, 0.0, 0.0, 0.0)

    return AxisAlignedBBox(
        boundingBoxes.minOf { it.minX },
        boundingBoxes.minOf { it.minY },
        boundingBoxes.minOf { it.minZ },
        boundingBoxes.maxOf { it.maxX },
        boundingBoxes.maxOf { it.maxY },
        boundingBoxes.maxOf { it.maxZ },
    )
}