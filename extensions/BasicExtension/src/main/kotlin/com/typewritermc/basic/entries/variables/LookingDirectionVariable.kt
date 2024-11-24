package com.typewritermc.basic.entries.variables

import com.typewritermc.core.books.pages.Colors
import com.typewritermc.core.extension.annotations.*
import com.typewritermc.core.utils.point.Vector
import com.typewritermc.engine.paper.entry.entries.VarContext
import com.typewritermc.engine.paper.entry.entries.VariableEntry
import com.typewritermc.engine.paper.entry.entries.getData
import com.typewritermc.engine.paper.loader.serializers.VectorSerializer
import com.typewritermc.engine.paper.utils.toVector
import kotlin.reflect.cast

@Entry(
    "looking_direction_variable",
    "A variable that returns the direction the player is looking at",
    Colors.GREEN,
    "mingcute:look-left-fill"
)
@GenericConstraint(Vector::class)
@VariableData(LookingDirectionVariableData::class)
class LookingDirectionVariable(
    override val id: String = "",
    override val name: String = "",
    @Help("The vector to add to the direction")
    val vector: Vector = Vector.ZERO,
    @Default("1.0")
    val scalar: Double = 1.0,
) : VariableEntry {
    override fun <T : Any> get(context: VarContext<T>): T {
        val dataScalar = context.getData<LookingDirectionVariableData>()?.scalar ?: 1.0
        val scalar = this.scalar * dataScalar

        val dataVector = context.getData<LookingDirectionVariableData>()?.vector ?: Vector.ZERO
        val vector = this.vector.mul(dataVector)

        val player = context.player
        val lookDirection = player.location.direction.toVector()
        return context.klass.cast(lookDirection.add(vector).mul(scalar))
    }
}

data class LookingDirectionVariableData(
    @Help("The vector to add to the direction")
    val vector: Vector = Vector.ZERO,
    @Default("1.0")
    val scalar: Double = 1.0,
)