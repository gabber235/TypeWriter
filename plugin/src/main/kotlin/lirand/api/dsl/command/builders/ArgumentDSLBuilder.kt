package lirand.api.dsl.command.builders

import com.mojang.brigadier.arguments.ArgumentType
import com.mojang.brigadier.builder.RequiredArgumentBuilder
import com.mojang.brigadier.suggestion.SuggestionProvider
import com.mojang.brigadier.suggestion.SuggestionsBuilder
import lirand.api.dsl.command.implementation.tree.nodes.BrigadierArgument
import org.bukkit.command.CommandSender
import org.bukkit.plugin.Plugin

open class ArgumentDSLBuilder<T>(
	plugin: Plugin,
	val name: String,
	val type: ArgumentType<T>
) : ContinuableNodeDSLBuilder<RequiredArgumentBuilder<CommandSender, ArgumentDSLBuilder<T>>>(plugin) {

	var suggestionsProvider: SuggestionProvider<CommandSender>? = null
		private set


	fun suggests(provider: BrigadierCommandContext<CommandSender>.(builder: SuggestionsBuilder) -> Unit) {
		suggestionsProvider = SuggestionProvider { context, builder ->
			BrigadierCommandContext<CommandSender>(context).provider(builder)
			builder.buildFuture()
		}
	}

	override fun build(): BrigadierArgument<CommandSender, T> {
		val argument = BrigadierArgument(
			name, type, completeExecutor, completeRequirement,
			suggestionsProvider, redirect, redirectModifier, isFork
		)

		for (child in arguments) {
			argument.addChild(child)
		}

		return argument
	}
}