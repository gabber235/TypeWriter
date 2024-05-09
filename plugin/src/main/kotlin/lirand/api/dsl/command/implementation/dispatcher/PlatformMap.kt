package lirand.api.dsl.command.implementation.dispatcher

import com.mojang.brigadier.tree.LiteralCommandNode
import org.bukkit.command.CommandSender

/**
 * A `DispatcherMap` wraps and registers `LiteralCommandNode`s to the
 * platform.
 */
interface PlatformMap {
	/**
	 * Registers the given command to the platform.
	 *
	 * @param node the command
	 * @return a [DispatcherCommand] that represents the given command, or
	 * `null` if the command could not be registered
	 */
	fun register(node: LiteralCommandNode<CommandSender>): DispatcherCommand?

	/**
	 * Removes the given command from the platform.
	 *
	 * @param name the name of the command
	 * @return a [DispatcherCommand] that represents the removed command,
	 * or `null` if the command was not registered
	 */
	fun unregister(name: String): DispatcherCommand?
}