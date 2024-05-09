package lirand.api.dsl.command.builders.fails

import me.gabber235.typewriter.utils.plainText
import net.kyori.adventure.text.Component
import net.md_5.bungee.api.chat.BaseComponent
import org.bukkit.command.CommandSender

class CommandFailException(
	val failMessage: Component? = null,
	val source: CommandSender
) : RuntimeException() {

	override val message: String?
		get() = failMessage?.plainText()

}