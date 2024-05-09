package lirand.api.dsl.command.implementation.dispatcher

import com.mojang.brigadier.Command
import com.mojang.brigadier.CommandDispatcher
import com.mojang.brigadier.arguments.ArgumentType
import com.mojang.brigadier.context.CommandContext
import com.mojang.brigadier.suggestion.SuggestionProvider
import com.mojang.brigadier.suggestion.SuggestionsBuilder
import com.mojang.brigadier.tree.ArgumentCommandNode
import com.mojang.brigadier.tree.CommandNode
import lirand.api.dsl.command.implementation.tree.Mapper
import lirand.api.dsl.command.types.Type
import org.bukkit.command.CommandSender
import java.lang.reflect.Method
import java.util.function.Predicate

internal class SpigotMapper(private val dispatcher: CommandDispatcher<CommandSender>) : Mapper<CommandSender, Any>() {

	override fun getType(node: ArgumentCommandNode<CommandSender, *>): ArgumentType<*>? {
		val type = node.type
		return if (type is Type<*>) type.map() else type
	}

	override fun mapCommand(node: CommandNode<CommandSender>): Command<Any>? {
		return node.command?.let { reparseCommand(node) }
	}

	override fun mapRequirement(node: CommandNode<CommandSender>): Predicate<Any> {
		val requirement = node.requirement
		return if (requirement == null) TRUE
		else Predicate { listener: Any ->
			requirement.test(listener.bukkitSender)
		}
	}

	override fun mapSuggestions(node: ArgumentCommandNode<CommandSender, *>): SuggestionProvider<Any>? {
		val type = node.type
		val suggestor = node.customSuggestions
		if (type !is Type<*> && suggestor == null) {
			return null
		}
		else if (suggestor == null) {
			return reparseSuggestions(type as Type<*>)
		}
		return reparseSuggestions(suggestor)
	}

	private fun reparseCommand(command: CommandNode<CommandSender>): Command<Any> {
		return Command { context: CommandContext<Any> ->
			val sender = context.source.bukkitSender
			val input = context.input

			val reparsed = dispatcher.parse(input, sender).context.build(input)
			command.command.run(reparsed)
		}
	}

	private fun reparseSuggestions(type: Type<*>): SuggestionProvider<Any> {
		return SuggestionProvider { context: CommandContext<Any>, suggestions: SuggestionsBuilder ->
			val sender = context.source.bukkitSender
			var input = context.input
			input = if (input.length <= 1) "" else input.substring(1)
			val reparsed = dispatcher.parse(input, sender).context.build(context.input)
			type.listSuggestions(reparsed, suggestions)
		}
	}

	private fun reparseSuggestions(suggestor: SuggestionProvider<CommandSender>): SuggestionProvider<Any> {
		return SuggestionProvider { context: CommandContext<Any>, suggestions: SuggestionsBuilder ->
			val sender = context.source.bukkitSender
			var input = context.input
			input = if (input.length <= 1) "" else input.substring(1)
			val reparsed = dispatcher.parse(input, sender).context.build(context.input)
			suggestor.getSuggestions(reparsed, suggestions)
		}
	}


	private val Any.bukkitSender: CommandSender
		get() {
			verifyCommandListenerInstance(this)
			return listenerGetBukkitSenderMethod.invoke(this) as CommandSender
		}



	private companion object {
		lateinit var listenerGetBukkitSenderMethod: Method
			private set

		fun verifyCommandListenerInstance(commandListener: Any) {
			if (::listenerGetBukkitSenderMethod.isInitialized) return

			listenerGetBukkitSenderMethod = commandListener::class.java.getMethod("getBukkitSender")
		}
	}
}