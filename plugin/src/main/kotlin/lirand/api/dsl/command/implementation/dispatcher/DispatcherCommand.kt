package lirand.api.dsl.command.implementation.dispatcher

import com.mojang.brigadier.CommandDispatcher
import com.mojang.brigadier.StringReader
import com.mojang.brigadier.exceptions.CommandSyntaxException
import lirand.api.dsl.command.types.exceptions.ChatCommandSyntaxException
import net.kyori.adventure.text.Component
import net.kyori.adventure.text.event.ClickEvent
import net.kyori.adventure.text.format.NamedTextColor
import net.kyori.adventure.text.format.TextDecoration
import org.bukkit.command.Command
import org.bukkit.command.CommandSender
import org.bukkit.command.PluginIdentifiableCommand
import org.bukkit.plugin.Plugin
import java.lang.reflect.Method
import kotlin.math.max
import kotlin.math.min

/**
 * A [Command] subclass that forwards execution to a underlying `CommandDispatcher`.
 *
 * **Note:**
 * This class was adapted from Spigot's `VanillaCommand`.
 */
class DispatcherCommand(
    name: String,
    private val plugin: Plugin,
    val dispatcher: CommandDispatcher<CommandSender>,
    usage: String,
    aliases: List<String>
) : Command(name, "", usage, aliases), PluginIdentifiableCommand {

    /**
     * Forwards execution with the rejoined label and arguments to the underlying
     * [CommandDispatcher] if the [sender] has sufficient permission.
     *
     * @param sender the sender
     * @param label the label
     * @param arguments the arguments
     * @return true
     */
    override fun execute(sender: CommandSender, label: String, vararg arguments: String): Boolean {
        if (!testPermission(sender)) {
            return true
        }

        val reader = StringReader(join(label, arguments))

        if (reader.canRead() && reader.peek() == '/') {
            reader.skip()
        }

        try {
            dispatcher.execute(reader, sender)
        } catch (exception: CommandSyntaxException) {
            if (exception is ChatCommandSyntaxException) {
                sender.sendMessage(exception.chatMessage.color(NamedTextColor.RED))
            } else {
                sender.sendMessage(Component.text(exception.rawMessage.string).color(NamedTextColor.RED))
            }

            report(sender, exception.input, exception.cursor)
        }

        return true
    }

    override fun getPlugin(): Plugin {
        return plugin
    }


    private fun report(sender: CommandSender, input: String?, cursor: Int) {
        if (input == null || cursor < 0) return

        val index = min(input.length, cursor)
        val errorStart = input.lastIndexOf(' ', index - 1) + 1

        var failedCommandMessage = Component.text().clickEvent(ClickEvent.suggestCommand(input))


        if (errorStart > 10) {
            failedCommandMessage = failedCommandMessage.append(Component.text("..."))
        }
        failedCommandMessage =
            failedCommandMessage.append(Component.text(input.substring(max(0, errorStart - 10), errorStart)))

        if (errorStart < input.length) {
            val error = Component.text(input.substring(errorStart, cursor))
                .color(NamedTextColor.RED)
                .decorate(TextDecoration.UNDERLINED)
            failedCommandMessage = failedCommandMessage.append(error)
        }

        val context = Component.translatable("command.context.here")
            .color(NamedTextColor.RED)
            .decorate(TextDecoration.ITALIC)
        failedCommandMessage = failedCommandMessage.append(context)

        sender.sendMessage(failedCommandMessage)
    }

    private fun join(name: String, arguments: Array<out String>): String {
        var command = "/$name"
        if (arguments.isNotEmpty()) {
            command += " ${arguments.joinToString(" ")}"
        }
        return command
    }
}