package lirand.api.dsl.command.builders

import com.github.shynixn.mccoroutine.bukkit.minecraftDispatcher
import com.mojang.brigadier.Command
import com.mojang.brigadier.builder.ArgumentBuilder
import com.mojang.brigadier.tree.CommandNode
import com.mojang.brigadier.tree.RootCommandNode
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.launch
import lirand.api.dsl.command.builders.fails.CommandFailException
import lirand.api.extensions.server.allPermissions
import org.bukkit.command.CommandSender
import org.bukkit.command.ConsoleCommandSender
import org.bukkit.entity.Player
import org.bukkit.plugin.Plugin
import java.util.function.Predicate
import com.mojang.brigadier.Command as BrigadierCommand

private typealias CommandExecutor<S> = BrigadierCommandContext<S>.(scope: CoroutineScope) -> Unit

@NodeBuilderDSLMarker
abstract class NodeDSLBuilder<B : ArgumentBuilder<CommandSender, B>>(
    val plugin: Plugin
) {

    protected val rootNode = RootCommandNode<CommandSender>()

    open val arguments: Collection<CommandNode<CommandSender>>
        get() = rootNode.children


    protected val scope = CoroutineScope(
        plugin.minecraftDispatcher + SupervisorJob() +
                CoroutineExceptionHandler { _, exception ->
                    if (exception is CommandFailException) {
                        exception.failMessage?.let {
                            exception.source.sendMessage(it)
                        }
                    } else {
                        exception.printStackTrace()
                    }
                }
    )

    open var completeExecutor: Command<CommandSender>? = null
        protected set

    open var defaultExecutor: CommandExecutor<CommandSender>? = null
        protected set


    open var playerExecutor: CommandExecutor<Player>? = null
        protected set
    open var consoleExecutor: CommandExecutor<ConsoleCommandSender>? = null
        protected set


    open var completeRequirement: Predicate<CommandSender> =
        Predicate { sender -> requirements.all { it.test(sender) } }
        protected set

    protected val _requirements = mutableListOf<Predicate<CommandSender>>()
    open val requirements: List<Predicate<CommandSender>>
        get() = _requirements


    open fun executes(block: CommandExecutor<CommandSender>) {
        if (completeExecutor == null) {
            setupExecutor()
        }
        defaultExecutor = block
    }

    open fun executesPlayer(block: CommandExecutor<Player>) {
        if (completeExecutor == null) {
            setupExecutor()
        }
        playerExecutor = block
    }

    open fun executesConsole(block: CommandExecutor<ConsoleCommandSender>) {
        if (completeExecutor == null) {
            setupExecutor()
        }
        consoleExecutor = block
    }


    open fun requires(predicate: (sender: CommandSender) -> Boolean) {
        _requirements.add(predicate)
    }

    open fun requiresPermissions(permission: String, vararg permissions: String) {
        _requirements.add { sender -> sender.allPermissions(permission, *permissions) }
    }


    abstract fun build(): CommandNode<CommandSender>


    private fun setupExecutor() {
        completeExecutor = BrigadierCommand { context ->

            val brigadierContext = BrigadierCommandContext(context)

            scope.launch {
                defaultExecutor?.invoke(brigadierContext, scope)

                if (context.source is Player) {
                    playerExecutor?.invoke(
                        brigadierContext as BrigadierCommandContext<Player>,
                        this
                    )
                }

                if (context.source is ConsoleCommandSender) {
                    consoleExecutor?.invoke(
                        brigadierContext as BrigadierCommandContext<ConsoleCommandSender>,
                        this
                    )
                }
            }

            Command.SINGLE_SUCCESS
        }
    }
}