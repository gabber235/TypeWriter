package lirand.api.dsl.command.implementation.tree.nodes

import com.mojang.brigadier.Command
import com.mojang.brigadier.RedirectModifier
import com.mojang.brigadier.tree.CommandNode
import com.mojang.brigadier.tree.LiteralCommandNode
import java.util.function.Consumer
import java.util.function.Predicate

/**
 * A `LiteralCommandNode` subclass that provides additional convenience methods
 * and support for aliases.
 *
 * @param <T> the type of the source
</T> */
class BrigadierLiteral<T>(
	name: String,
	override val aliases: MutableList<LiteralCommandNode<T>> = mutableListOf(),
	override val isAlias: Boolean = false,
	command: Command<T>? = null,
	requirement: Predicate<T>? = null,
	private var destination: CommandNode<T>? = null,
	modifier: RedirectModifier<T>? = null,
	isFork: Boolean = false
) : LiteralCommandNode<T>(name, command, requirement, destination, modifier, isFork), AliasableCommandNode<T>,
	MutableCommandNode<T> {

	private val addition: Consumer<CommandNode<T>> = Consumer { node: CommandNode<T> -> super.addChild(node) }

	override fun addChild(child: CommandNode<T>) {
		addAliasedChild(child, addition)
		for (alias in aliases) {
			alias.addChild(child)
		}
	}

	override fun removeChild(child: String): CommandNode<T>? {
		val removed = removeAliasedChild(child)
		for (alias in aliases) {
			alias.removeAliasedChild(child)
		}
		return removed
	}


	override fun getCommand(): Command<T>? {
		return super.getCommand()
	}

	override fun setCommand(command: Command<T>?) {
		setMutableCommand(command)
		for (alias in aliases) {
			alias.setMutableCommand(command)
		}
	}

	override fun getRedirect(): CommandNode<T>? {
		return destination
	}

	override fun setRedirect(redirect: CommandNode<T>?) {
		this.destination = redirect
		for (alias in aliases) {
			if (alias is MutableCommandNode<*>) {
				(alias as MutableCommandNode<T>).setRedirect(redirect)
			}
		}
	}
}

internal fun <T> LiteralCommandNode<T>.createAlias(alias: String): BrigadierLiteral<T> {
	val literal = BrigadierLiteral<T>(
		alias, mutableListOf(), true,
		command, requirement, redirect, redirectModifier, isFork
	)

	for (child in children) {
		literal.addChild(child)
	}

	if (this is AliasableCommandNode<*>) {
		(this as AliasableCommandNode<T>).aliases.add(literal)
	}
	return literal
}