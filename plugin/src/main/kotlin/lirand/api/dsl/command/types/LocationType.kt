package lirand.api.dsl.command.types

import com.mojang.brigadier.StringReader
import com.mojang.brigadier.exceptions.CommandSyntaxException
import com.mojang.brigadier.suggestion.SuggestionsBuilder
import lirand.api.dsl.command.types.exceptions.ChatCommandSyntaxException
import lirand.api.dsl.command.types.extensions.RelativeLocation
import lirand.api.extensions.math.set
import org.bukkit.Axis

/**
 * A [RelativeLocation] type.
 *
 * @see VectorType
 */
abstract class LocationType(
    private vararg val axes: Axis
) {

    fun parse(reader: StringReader): RelativeLocation {
        val location = RelativeLocation().apply {
            isRelativeWorld = true
        }

        for (axis in axes) {
            parse(reader, location, axis)
        }

        return location
    }

    /**
     * Parses a value from [reader] and sets it on the given axis of [location].
     *
     * @param reader the reader
     * @param location the point
     * @param axis the axis
     * @throws ChatCommandSyntaxException if the input was invalid
     */
    fun parse(reader: StringReader, location: RelativeLocation, axis: Axis) {
        reader.skipWhitespace()

        if (reader.peek() == '~') {
            reader.skip()
            location.setRelative(axis, true)
        }

        location[axis] = try {
            reader.readDouble()
        } catch (exception: CommandSyntaxException) {
            if (location.isRelative(axis)) 0
            else throw exception
        }
    }
}


/**
 * A 2D [RelativeLocation] type.
 */
open class Location2DType protected constructor() : LocationType(Axis.X, Axis.Z), Cartesian2DType<RelativeLocation> {

    override fun suggest(builder: SuggestionsBuilder, parts: List<String>) {
        if (builder.remaining.isBlank()) {
            builder.suggest("~").suggest("~ ~")
        }
        else if (parts.size == 1) {
            builder.suggest("${parts[0]} ~")
        }
    }

    override fun getExamples(): Collection<String> = listOf("0 0", "0.0 0.0", "~ ~")



    companion object Instance : Location2DType()
}


/**
 * A 3D [RelativeLocation] type.
 */
open class Location3DType protected constructor() : LocationType(Axis.X, Axis.Y, Axis.Z), Cartesian3DType<RelativeLocation> {

    override fun suggest(builder: SuggestionsBuilder, parts: List<String>) {
        if (parts.isEmpty()) {
            builder.suggest("~").suggest("~ ~").suggest("~ ~ ~")
        }
        else if (parts.size == 1) {
            builder.suggest("${parts[0]} ~").suggest("${parts[0]} ~ ~")
        }
        else if (parts.size == 2) {
            builder.suggest("${parts[0]} ${parts[1]} ~")
        }
    }

    override fun getExamples(): Collection<String> = listOf("0 0 0", "0.0 0.0 0.0", "~ ~ ~")



    companion object Instance : Location3DType()
}