package lirand.api.dsl.command.builders.chains.optional

import com.mojang.brigadier.StringReader
import com.mojang.brigadier.arguments.ArgumentType
import com.mojang.brigadier.context.CommandContext
import com.mojang.brigadier.context.StringRange
import com.mojang.brigadier.suggestion.Suggestions
import com.mojang.brigadier.suggestion.SuggestionsBuilder
import com.mojang.brigadier.tree.ArgumentCommandNode
import lirand.api.dsl.command.types.PrefixedType
import lirand.api.dsl.command.types.Type
import lirand.api.dsl.command.types.exceptions.ChatCommandExceptionType
import lirand.api.dsl.command.types.extensions.until
import net.kyori.adventure.text.Component
import java.util.concurrent.CompletableFuture

class CompoundPrefixedType(
    val options: MutableList<PrefixedType<*>>
) : Type<Any> {

    private val unknownPrefixExceptionType = ChatCommandExceptionType {
        Component.translatable("argument.id.unknown", Component.text(it[0].toString()))
    }

    override fun parse(reader: StringReader): Any {
        val prefix = reader.until(':').lowercase()
        if (reader.canRead()) reader.skip()
        else throw unknownPrefixExceptionType.createWithContext(reader, prefix)

        return options.find { it.prefix.lowercase() == prefix }?.type?.parse(reader)
            ?: throw unknownPrefixExceptionType.createWithContext(reader, prefix)
    }

    override fun <S> listSuggestions(
        context: CommandContext<S>,
        builder: SuggestionsBuilder
    ): CompletableFuture<Suggestions> {
        val alreadyUsedOptions = context.nodes
            .filter { (it.node as? ArgumentCommandNode<*, *>)?.type is CompoundPrefixedType }
            .map { StringRange(it.range.start + 1, it.range.end + 1).get(context.input) }

        val unusedOptions = options.filter { option -> alreadyUsedOptions.none { it.startsWith(option.prefixForm) } }

        unusedOptions.find { builder.remaining.startsWith(it.prefixForm) }?.let {
            return it.type.listSuggestions(
                context,
                SuggestionsBuilder(builder.input, builder.start + it.prefixForm.length)
            )
        }

        unusedOptions.filter { it.prefixForm.startsWith(builder.remaining) }
            .forEach {
                builder.suggest(it.prefixForm)
            }

        return builder.buildFuture()
    }


    override fun getExamples(): List<String> = options.map { examples[0] }


    override fun map(): ArgumentType<*> {
        return PrefixedType.argumentTag
    }
}