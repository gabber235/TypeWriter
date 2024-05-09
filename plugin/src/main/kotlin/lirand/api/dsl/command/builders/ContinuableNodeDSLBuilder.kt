package lirand.api.dsl.command.builders

import com.mojang.brigadier.RedirectModifier
import com.mojang.brigadier.arguments.ArgumentType
import com.mojang.brigadier.builder.ArgumentBuilder
import com.mojang.brigadier.tree.CommandNode
import lirand.api.dsl.command.implementation.tree.nodes.BrigadierArgument
import lirand.api.dsl.command.implementation.tree.nodes.BrigadierLiteral
import org.bukkit.command.CommandSender
import org.bukkit.plugin.Plugin

@NodeBuilderDSLMarker
abstract class ContinuableNodeDSLBuilder<B : ArgumentBuilder<CommandSender, B>>(
	plugin: Plugin
): NodeDSLBuilder<B>(plugin) {

	open var redirect: CommandNode<CommandSender>? = null
		protected set

	open var redirectModifier: RedirectModifier<CommandSender>? = null
		protected set

	open var isFork: Boolean = false
		protected set



	inline fun literal(
		name: String,
		crossinline builder: LiteralDSLBuilder.() -> Unit
	): BrigadierLiteral<CommandSender> {
		val childNode = LiteralDSLBuilder(plugin, name)
			.apply(builder).build()

		addChild(childNode)

		return childNode
	}

	inline fun <T> argument(
		name: String,
		type: ArgumentType<T>,
		crossinline builder: ArgumentDSLBuilder<T>.(argument: ArgumentDefinition<T>) -> Unit
	): BrigadierArgument<CommandSender, T> {
		val childNode = ArgumentDSLBuilder(plugin, name, type).apply {
			builder(ArgumentDefinition(name, type))
		}.build()

		addChild(childNode)

		return childNode
	}



	open fun redirect(target: CommandNode<CommandSender>) {
		_requirements.add(target.requirement)
		forward(target.redirect, target.redirectModifier, target.isFork)
		completeExecutor = target.command

		for (child in target.children) {
			addChild(child)
		}
	}

	open fun fork(
		target: CommandNode<CommandSender>,
		modifier: RedirectModifier<CommandSender>? = null
	) {
		forward(target, modifier, true)
	}

	open fun forward(
		target: CommandNode<CommandSender>,
		modifier: RedirectModifier<CommandSender>? = null,
		isFork: Boolean
	) {
		check(rootNode.children.isEmpty()) { "Cannot forward a node with children." }
		redirect = target
		redirectModifier = modifier
		this.isFork = isFork
	}



	@PublishedApi
	internal open fun addChild(node: CommandNode<CommandSender>) {
		check(redirect == null) { "Cannot add children to a redirected node." }
		rootNode.addChild(node)
	}

}