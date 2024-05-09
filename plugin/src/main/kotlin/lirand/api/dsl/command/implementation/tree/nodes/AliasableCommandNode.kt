package lirand.api.dsl.command.implementation.tree.nodes

import com.mojang.brigadier.tree.LiteralCommandNode

/**
 * A node that has aliases and may itself be an alias.
 *
 * @param T the type of the source
 */
interface AliasableCommandNode<T> {

	/**
	 * Returns the aliases of this node.
	 *
	 * @return the aliases of this node
	 */
	val aliases: MutableList<LiteralCommandNode<T>>


	/**
	 * Returns whether this node is an alias.
	 *
	 * @return {@code true} if this node is an alias; otherwise {@code false}
	 */
	val isAlias: Boolean

}