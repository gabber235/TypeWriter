package lirand.api.dsl.command.implementation.tree.nodes

import com.mojang.brigadier.Command
import com.mojang.brigadier.RedirectModifier
import com.mojang.brigadier.arguments.ArgumentType
import com.mojang.brigadier.suggestion.SuggestionProvider
import com.mojang.brigadier.tree.ArgumentCommandNode
import com.mojang.brigadier.tree.CommandNode
import java.util.function.Consumer
import java.util.function.Predicate

/**
 * An `ArgumentCommandNode` subclass that provides additional convenience
 * methods.
 *
 * @param T the type of the source
 * @param V the type of the argument
 */
class BrigadierArgument<T, V>(
	name: String?,
	type: ArgumentType<V>?,
	command: Command<T>?,
	requirement: Predicate<T>?,
	suggestions: SuggestionProvider<T>?,
	private var destination: CommandNode<T>? = null,
	modifier: RedirectModifier<T>? = null,
	isFork: Boolean = false
) : ArgumentCommandNode<T, V>(name, type, command, requirement, destination, modifier, isFork, suggestions),
	MutableCommandNode<T> {

	private val addition = Consumer { node: CommandNode<T> -> super.addChild(node) }

	override fun addChild(child: CommandNode<T>) {
		addAliasedChild(child, addition)
	}

	override fun removeChild(child: String): CommandNode<T>? {
		return removeAliasedChild(child)
	}

	override fun setCommand(command: Command<T>?) {
		setMutableCommand(command)
	}

	override fun getRedirect(): CommandNode<T>? {
		return destination
	}

	override fun setRedirect(redirect: CommandNode<T>?) {
		this.destination = redirect
	}
}