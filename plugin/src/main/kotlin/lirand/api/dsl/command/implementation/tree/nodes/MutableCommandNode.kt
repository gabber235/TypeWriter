package lirand.api.dsl.command.implementation.tree.nodes

import com.mojang.brigadier.Command
import com.mojang.brigadier.tree.CommandNode
import java.lang.reflect.Field
import java.util.function.Consumer

/**
 * A mutable node that contains self-modification operations.
 *
 * @param T the type of the source
 */
interface MutableCommandNode<T> {
    /**
     * Adds the given child to this `Node` and its aliases.
     *
     * **Implementation requirement:**
     * The [child] must also be added to its aliases.
     *
     * @param child the child
     */
    fun addChild(child: CommandNode<T>)

    /**
     * Removes the given child from this node and its aliases.
     *
     * **Implementation requirement:**
     * The child must also be removed from its aliases.
     *
     * @param child the child
     * @return the child that was removed if present; otherwise `null`
     */
    fun removeChild(child: String): CommandNode<T>?

    /**
     * Returns the [Command] to be executed.
     *
     * @return the command to be executed
     */
    fun getCommand(): Command<T>?

    /**
     * Sets the [command] as the command to be executed by this node
     * and its aliases.
     *
     * **Implementation requirement:**
     * The [command] must also be set for its aliases.
     *
     * @param command the command to be executed
     */
    fun setCommand(command: Command<T>?)

    /**
     * Sets the destination of this node and its aliases when redirected.
     *
     * **Implementation requirement:**
     * The destination must also be set for its aliases.
     *
     * @param redirect the node to which this node is redirected
     */
    fun setRedirect(redirect: CommandNode<T>?)

    /**
     * Returns the destination of this `node`, or `null` if this
     * `node` is not redirected.
     *
     * @return the destination of this `node`, or `null` if this node is not
     * redirected
     */
    fun getRedirect(): CommandNode<T>?
}


internal fun <N, T> N.addAliasedChild(
    child: CommandNode<T>,
    addition: Consumer<CommandNode<T>>
) where N : CommandNode<T>, N : MutableCommandNode<T> {
    val current = getChild(child.name)
    if (current != null) {
        removeChild(current.name)
        for (grandchild in current.children) {
            child.addChild(grandchild)
        }
    }
    if (current is AliasableCommandNode<*>) {
        for (alias in (current as AliasableCommandNode<T>).aliases) {
            removeChild(alias.name)
        }
    }
    addition.accept(child)
    if (child is AliasableCommandNode<*>) {
        val aliases = (child as AliasableCommandNode<T>).aliases
        for (alias in aliases) {
            addition.accept(alias)
        }
        if (current != null) {
            for (alias in aliases) {
                for (grandchild in current.children) {
                    alias.addChild(grandchild)
                }
            }
        }
    }
}

internal fun <T> CommandNode<T>.removeAliasedChild(childName: String): CommandNode<T>? {
    val children = nodeChildrenField.get(this) as MutableMap<String?, CommandNode<T>>
    val literals = nodeLiteralsField.get(this) as MutableMap<String?, *>
    val arguments = nodeArgumentsField.get(this) as MutableMap<String?, *>

    val removed = children.remove(childName) ?: return null
    literals.remove(childName)
    arguments.remove(childName)

    if (removed is AliasableCommandNode<*>) {
        for (alias in removed.aliases) {
            val name = alias.name
            children.remove(name)
            literals.remove(name)
            arguments.remove(name)
        }
    }

    return removed
}

internal fun <T> CommandNode<T>.setMutableCommand(execution: Command<T>?) {
    nodeCommandField.set(this, execution)
}


private val commandNodeClass = CommandNode::class.java

private val nodeCommandField: Field = commandNodeClass.getDeclaredField("command").apply {
    isAccessible = true
}
private val nodeChildrenField: Field = commandNodeClass.getDeclaredField("children").apply {
    isAccessible = true
}
private val nodeLiteralsField: Field = commandNodeClass.getDeclaredField("literals").apply {
    isAccessible = true
}
private val nodeArgumentsField: Field = commandNodeClass.getDeclaredField("arguments").apply {
    isAccessible = true
}