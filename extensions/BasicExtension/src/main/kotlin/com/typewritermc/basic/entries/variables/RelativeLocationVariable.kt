package com.typewritermc.basic.entries.variables

import com.typewritermc.core.books.pages.Colors
import com.typewritermc.core.extension.annotations.Entry
import com.typewritermc.core.extension.annotations.GenericConstraint
import com.typewritermc.core.extension.annotations.VariableData
import com.typewritermc.core.utils.point.Coordinate
import com.typewritermc.core.utils.point.Position
import com.typewritermc.engine.paper.entry.entries.VarContext
import com.typewritermc.engine.paper.entry.entries.VariableEntry
import com.typewritermc.engine.paper.entry.entries.getData
import com.typewritermc.engine.paper.utils.position
import kotlin.reflect.safeCast

@Entry(
    "relative_location_variable",
    "A variable that returns the location relative to the player",
    Colors.GREEN,
    "streamline:target-solid"
)
@GenericConstraint(Position::class)
@VariableData(RelativeLocationVariableData::class)
/**
 * The `RelativeLocationVariable` is a variable that returns the location relative to the player.
 * The location is calculated by adding the coordinate to the player's position.
 *
 * ## How could this be used?
 * This could be used to make a death cinematic that shows at player's location after they die.
 */
class RelativeLocationVariable(
    override val id: String = "",
    override val name: String = "",
) : VariableEntry {
    override fun <T : Any> get(context: VarContext<T>): T {
        val player = context.player
        val data = context.getData<PlayerWorldPositionVariableData>()
            ?: throw IllegalStateException("Could not find data for ${context.klass}, data: ${context.data}")

        val position = player.position + data.coordinate
        return context.klass.safeCast(position)
            ?: throw IllegalStateException("Could not cast position to ${context.klass}, PlayerWorldPositionVariable is only compatible with Position fields")
    }
}

data class RelativeLocationVariableData(
    val coordinate: Coordinate = Coordinate.ORIGIN,
)