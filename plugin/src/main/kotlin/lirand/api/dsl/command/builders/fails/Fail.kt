package lirand.api.dsl.command.builders.fails

import lirand.api.dsl.command.builders.BrigadierCommandContext
import me.gabber235.typewriter.utils.asMini
import net.kyori.adventure.text.Component


fun BrigadierCommandContext<*>.fail(
    message: Component? = null
): Nothing = throw CommandFailException(message, source)

fun BrigadierCommandContext<*>.fail(
    message: String
): Nothing = fail(message.asMini())