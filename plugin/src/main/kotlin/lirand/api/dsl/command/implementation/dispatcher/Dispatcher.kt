package lirand.api.dsl.command.implementation.dispatcher

import com.mojang.brigadier.CommandDispatcher
import com.mojang.brigadier.tree.CommandNode
import com.mojang.brigadier.tree.LiteralCommandNode
import lirand.api.dsl.command.builders.LiteralDSLBuilder
import lirand.api.dsl.command.implementation.tree.TreeWalker
import lirand.api.dsl.command.implementation.tree.nodes.BrigadierLiteral
import lirand.api.dsl.command.implementation.tree.nodes.BrigadierRoot
import lirand.api.extensions.server.registerEvents
import lirand.api.extensions.server.server
import org.bukkit.command.CommandSender
import org.bukkit.command.SimpleCommandMap
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.server.ServerLoadEvent
import org.bukkit.plugin.Plugin

/**
 * A [CommandDispatcher] subclass that facilities the registration of commands
 * between a Spigot plugin and the server.
 *
 * This dispatcher will **not** automatically synchronize newly registered commands
 * except when the server is started or reloaded to minimize unnecessary synchronizations.
 * All otherwise registered commands must be manually synchronized via [update].
 *
 * **Implementation details:**
 * This dispatcher holds a reference to the internal dispatcher of the server. The
 * internal dispatcher is flushed after all plugins are enabled when the server
 * is either started or reloaded. In both cases, this dispatcher and the internal
 * dispatcher are synchronized automatically.
 */
open class Dispatcher protected constructor(private val root: BrigadierRoot) :
    CommandDispatcher<CommandSender>(root), Listener {

    internal val walker: TreeWalker<CommandSender, Any> = TreeWalker(
        SpigotMapper(this)
    )

    open fun register(nodes: List<CommandNode<CommandSender>>) {
        for (node in nodes) {
            getRoot().addChild(node)
        }
    }

    /**
     * Registers the command by the given node.
     *
     * @param node that must be registered
     */
    open fun register(node: LiteralCommandNode<CommandSender>) {
        root.addChild(node)
    }

    /**
     * Registers the command built using the given [builder].
     *
     * @param builder the builder used to build the command to be registered
     * @return the built node that was registered
     */
    open fun register(builder: LiteralDSLBuilder): BrigadierLiteral<CommandSender> {
        val literal = builder.build()
        register(literal)
        return literal
    }

    /**
     * Unregister the command by name.
     * @param name the name of the command to unregister
     * @return the command that was unregistered
     */
    open fun unregister(name: String): CommandNode<CommandSender>? {
        return root.removeChild(name)
    }

    /**
     * Synchronizes this dispatcher with the server and clients.
     */
    open fun update() {
        walker.prune(commandDispatcher.root, root.children)
        for (player in server.onlinePlayers) {
            player.updateCommands()
        }
    }

    override fun getRoot(): BrigadierRoot {
        return root
    }

    @EventHandler
    private fun onServerReload(event: ServerLoadEvent) {
        commandDispatcher = commandsGetDispatcherMethod.invoke(
            serverGetCommandDispatcherMethod.invoke(dedicatedServer)
        ) as CommandDispatcher<Any>

        if (event.type == ServerLoadEvent.LoadType.STARTUP) {
            walker.prune(commandDispatcher.root, root.children)
        } else {
            update()
        }
    }

    companion object {

        private lateinit var commandDispatcher: CommandDispatcher<Any>

        private val dedicatedServer = server::class.java.getMethod("getServer").invoke(server)
        private val serverGetCommandMapMethod = server::class.java.getMethod("getCommandMap")
        private val serverGetCommandDispatcherMethod = dedicatedServer::class.java.methods
            .find { it.returnType.simpleName == "CommandDispatcher" && it.parameterCount == 0 }
            ?: dedicatedServer::class.java.methods
                .find { it.returnType.simpleName == "Commands" && it.parameterCount == 0 }
            ?: throw IllegalStateException("Could not find the method getCommandDispatcher in the server class")

        private val commandsGetDispatcherMethod = serverGetCommandDispatcherMethod.returnType.methods
            .find { it.returnType == CommandDispatcher::class.java && it.parameterCount == 0 }!!


        private val _instances = mutableMapOf<Plugin, Dispatcher>()
        val instances: Map<Plugin, Dispatcher> get() = _instances

        fun of(plugin: Plugin): Dispatcher {
            if (plugin in instances) return instances[plugin]!!

            if (!Companion::commandDispatcher.isInitialized) {
                val commands = serverGetCommandDispatcherMethod.invoke(dedicatedServer)
                commandDispatcher = commandsGetDispatcherMethod.invoke(commands) as CommandDispatcher<Any>
            }

            val prefix = plugin.name.lowercase()
            val map = SpigotMap(
                prefix, plugin,
                serverGetCommandMapMethod.invoke(server) as SimpleCommandMap
            )
            val root = BrigadierRoot(prefix, map)
            val dispatcher = Dispatcher(root)
            map.dispatcher = dispatcher

            _instances[plugin] = dispatcher

            plugin.registerEvents(dispatcher)

            return dispatcher
        }
    }
}