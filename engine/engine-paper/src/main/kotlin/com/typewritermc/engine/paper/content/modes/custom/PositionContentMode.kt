package com.typewritermc.engine.paper.content.modes.custom

import com.typewritermc.core.utils.point.Position
import com.typewritermc.core.utils.point.World
import com.typewritermc.engine.paper.content.ContentContext
import com.typewritermc.engine.paper.content.modes.ImmediateFieldValueContentMode
import com.typewritermc.engine.paper.utils.round
import org.bukkit.entity.Player
import java.lang.reflect.Type

class PositionContentMode(context: ContentContext, player: Player) :
    ImmediateFieldValueContentMode<Position>(context, player) {
    override val type: Type = Position::class.java

    override fun value(): Position {
        val location = player.location
        return Position(
            World(location.world.name),
            location.x.round(2),
            location.y.round(2),
            location.z.round(2),
            location.yaw.round(2),
            location.pitch.round(2),
        )
    }
}