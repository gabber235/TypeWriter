package lirand.api.dsl.command.implementation.tree.nodes

import com.mojang.brigadier.Command
import com.mojang.brigadier.tree.CommandNode
import com.mojang.brigadier.tree.LiteralCommandNode
import com.mojang.brigadier.tree.RootCommandNode
import lirand.api.dsl.command.implementation.dispatcher.PlatformMap
import org.bukkit.command.CommandSender

/**
 * A `RootCommandNode` subclass that facilities the wrapping and registration
 * of a `CommandNode` to a `CommandMap`.
 */
class BrigadierRoot(private val prefix: String, val map: PlatformMap) :
	RootCommandNode<CommandSender>(), MutableCommandNode<CommandSender> {

	/**
	 * Adds the `command` if the provided `DispatcherMap` does not contain
	 * a command with the same name. In addition, a fallback alias of the `command`
	 * is always created and added. If the `command` implements [AliasableCommandNode],
	 * the aliases and the fallback of the aliases are also added in a similar fashion.
	 *
	 * @param child the command to be added
	 * @throws IllegalArgumentException if the `command` is not a `LiteralCommandNode`
	 */
	override fun addChild(child: CommandNode<CommandSender>) {
		require(getChild(child.name) == null) { "Invalid command: '" + child.name + "', root already contains a child with the same name" }
		require(child is LiteralCommandNode<*>) { "Invalid command: '" + child.name + "', commands registered to root must be a literal" }

		val literal = child as LiteralCommandNode<CommandSender>
		val wrapper = map.register(literal) ?: return
		super.addChild(literal.createAlias("$prefix:${literal.name}"))
		if (wrapper.name == wrapper.label) {
			super.addChild(literal)
		}
		if (literal is AliasableCommandNode<*>) {
			for (alias in ArrayList((literal as AliasableCommandNode<CommandSender>).aliases)) {
				if (wrapper.aliases.contains(alias!!.name)) {
					super.addChild(literal.createAlias("$prefix:${alias.name}"))
					super.addChild(alias)
				}
			}
		}
	}

	override fun removeChild(child: String): CommandNode<CommandSender>? {
		return removeAliasedChild(child)
	}

	override fun getRedirect(): CommandNode<CommandSender> = super.getRedirect()
	override fun setRedirect(redirect: CommandNode<CommandSender>?) {}

	override fun getCommand(): Command<CommandSender> = super.getCommand()
	override fun setCommand(command: Command<CommandSender>?) {}
}