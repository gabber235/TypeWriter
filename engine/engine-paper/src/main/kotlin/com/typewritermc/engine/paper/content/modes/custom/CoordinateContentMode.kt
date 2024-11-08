package com.typewritermc.engine.paper.content.modes.custom

import com.typewritermc.core.utils.point.Coordinate
import com.typewritermc.engine.paper.content.ContentContext
import com.typewritermc.engine.paper.content.modes.ImmediateFieldValueContentMode
import com.typewritermc.engine.paper.utils.round
import org.bukkit.entity.Player
import java.lang.reflect.Type

class CoordinateContentMode(context: ContentContext, player: Player) :
    ImmediateFieldValueContentMode<Coordinate>(context, player) {
    override val type: Type = Coordinate::class.java

    override fun value(): Coordinate {
        val location = player.location
        return Coordinate(
            location.x.round(2),
            location.y.round(2),
            location.z.round(2),
            location.yaw.round(2),
            location.pitch.round(2),
        )
    }
}
