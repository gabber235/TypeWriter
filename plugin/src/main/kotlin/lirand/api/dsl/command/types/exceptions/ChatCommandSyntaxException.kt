package lirand.api.dsl.command.types.exceptions

import com.mojang.brigadier.LiteralMessage
import com.mojang.brigadier.exceptions.CommandExceptionType
import com.mojang.brigadier.exceptions.CommandSyntaxException
import me.gabber235.typewriter.utils.plainText
import net.kyori.adventure.text.Component
import net.md_5.bungee.api.chat.BaseComponent

class ChatCommandSyntaxException(
	type: CommandExceptionType,
	val chatMessage: Component,
	input: String? = null,
	cursor: Int = -1
) : CommandSyntaxException(
	type, LiteralMessage(chatMessage.plainText()),
	input, cursor
)