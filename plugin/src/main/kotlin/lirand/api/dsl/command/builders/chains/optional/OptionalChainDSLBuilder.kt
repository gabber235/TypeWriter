package lirand.api.dsl.command.builders.chains.optional

import com.mojang.brigadier.arguments.ArgumentType
import com.mojang.brigadier.builder.ArgumentBuilder
import com.mojang.brigadier.builder.RequiredArgumentBuilder
import com.mojang.brigadier.tree.CommandNode
import lirand.api.dsl.command.builders.ContinuableNodeDSLBuilder
import lirand.api.dsl.command.builders.NodeDSLBuilder
import lirand.api.dsl.command.builders.OptionalArgumentDefinition
import lirand.api.dsl.command.implementation.tree.nodes.BrigadierArgument
import lirand.api.dsl.command.types.PrefixedType
import lirand.api.dsl.command.types.exceptions.ChatCommandExceptionType
import net.kyori.adventure.text.Component
import net.md_5.bungee.api.chat.TranslatableComponent
import org.bukkit.command.CommandSender
import org.bukkit.plugin.Plugin

inline fun <B : ArgumentBuilder<CommandSender, B>> ContinuableNodeDSLBuilder<B>.optionalArguments(
	name: String,
	crossinline builder: OptionalChainDSLBuilder.() -> Unit
): CommandNode<CommandSender> {
	return OptionalChainDSLBuilder(
		plugin, name,
		CompoundPrefixedType(mutableListOf())
	).apply(builder).build().also {
		addChild(it)
	}
}


class OptionalChainDSLBuilder(
	plugin: Plugin,
	val name: String,
	val type: CompoundPrefixedType
) : NodeDSLBuilder<RequiredArgumentBuilder<CommandSender, OptionalChainDSLBuilder>>(plugin) {

	var defaultPrefixExceptionType = ChatCommandExceptionType {
		Component.translatable("argument.id.unknown", Component.text(it[0].toString()))
	}


	fun <T> option(prefixedType: PrefixedType<T>): OptionalArgumentDefinition<T> {
		this.type.options.add(prefixedType)

		return OptionalArgumentDefinition(name, prefixedType)
	}

	fun <T> option(
		prefixedType: Pair<String, ArgumentType<T>>,
		unknownPrefixExceptionType: ChatCommandExceptionType = defaultPrefixExceptionType
	): OptionalArgumentDefinition<T> {
		return option(PrefixedType(prefixedType.first, prefixedType.second, unknownPrefixExceptionType))
	}


	override fun build(): CommandNode<CommandSender> {
		val firstNode = constructNode("$name-1")

		var currentNode = firstNode
		for (i in 1 until type.options.size) {
			val nextNode = constructNode("$name-${i + 1}")
			currentNode.addChild(nextNode)
			currentNode = nextNode
		}

		return firstNode
	}



	private fun constructNode(name: String) = BrigadierArgument(
		name, type, completeExecutor, completeRequirement, null
	)

}