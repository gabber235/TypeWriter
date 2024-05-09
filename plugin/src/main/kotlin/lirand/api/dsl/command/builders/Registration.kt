package lirand.api.dsl.command.builders

import lirand.api.dsl.command.implementation.dispatcher.Dispatcher
import lirand.api.dsl.command.implementation.tree.nodes.BrigadierLiteral
import org.bukkit.command.CommandSender
import org.bukkit.plugin.Plugin

@DslMarker
@Retention(AnnotationRetention.BINARY)
annotation class NodeBuilderDSLMarker


inline fun Plugin.command(
	name: String,
	register: Boolean = true,
	crossinline builder: LiteralDSLBuilder.() -> Unit
): BrigadierLiteral<CommandSender> {
	val node = LiteralDSLBuilder(this, name)
		.apply(builder).build()

	if (register) Dispatcher.of(this).register(node)

	return node
}