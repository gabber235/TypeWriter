package lirand.api.dsl.command.implementation.tree

import com.mojang.brigadier.Command
import com.mojang.brigadier.arguments.ArgumentType
import com.mojang.brigadier.context.CommandContext
import com.mojang.brigadier.suggestion.SuggestionProvider
import com.mojang.brigadier.suggestion.SuggestionsBuilder
import com.mojang.brigadier.tree.ArgumentCommandNode
import com.mojang.brigadier.tree.CommandNode
import com.mojang.brigadier.tree.LiteralCommandNode
import com.mojang.brigadier.tree.RootCommandNode
import lirand.api.dsl.command.implementation.tree.nodes.BrigadierArgument
import lirand.api.dsl.command.implementation.tree.nodes.BrigadierLiteral
import java.util.function.Predicate

internal open class Mapper<T, R> {

	fun mapNode(node: CommandNode<T>): CommandNode<R> {
		return when (node) {
			is ArgumentCommandNode<*, *> -> {
				mapArgumentNode(node)
			}
			is LiteralCommandNode<*> -> {
				mapLiteralNode(node)
			}
			is RootCommandNode<*> -> {
				mapRootNode(node)
			}
			else -> {
				throw IllegalArgumentException("Unsupported command, '${node.name}' of type: ${node.javaClass.name}")
			}
		}
	}

	protected fun mapArgumentNode(node: CommandNode<T>): CommandNode<R> {
		val argument = node as ArgumentCommandNode<T, *>

		return BrigadierArgument(
			argument.name,
			getType(argument),
			mapCommand(argument),
			mapRequirement(argument),
			mapSuggestions(argument)
		)
	}

	protected fun mapLiteralNode(node: CommandNode<T>): CommandNode<R> {
		return BrigadierLiteral(node.name, command = mapCommand(node), requirement = mapRequirement(node))
	}

	protected fun mapRootNode(node: CommandNode<T>?): CommandNode<R> {
		return RootCommandNode()
	}

	protected open fun getType(node: ArgumentCommandNode<T, *>): ArgumentType<*>? {
		return node.type
	}

	protected open fun mapCommand(node: CommandNode<T>): Command<R>? {
		return NONE as Command<R>
	}

	protected open fun mapRequirement(node: CommandNode<T>): Predicate<R> {
		return TRUE as Predicate<R>
	}

	protected open fun mapSuggestions(node: ArgumentCommandNode<T, *>): SuggestionProvider<R>? {
		return null
	}

	protected companion object {

		val NONE: Command<Any> = Command { context: CommandContext<Any> -> 0 }

		val TRUE: Predicate<Any> = Predicate { source: Any -> true }

		val EMPTY: SuggestionProvider<Any> =
			SuggestionProvider { suggestions: CommandContext<Any>, builder: SuggestionsBuilder -> builder.buildFuture() }
	}
}