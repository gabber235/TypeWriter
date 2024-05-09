package lirand.api.dsl.command.types

import com.mojang.brigadier.Message
import com.mojang.brigadier.StringReader
import com.mojang.brigadier.context.CommandContext
import com.mojang.brigadier.suggestion.Suggestions
import com.mojang.brigadier.suggestion.SuggestionsBuilder
import lirand.api.dsl.command.types.exceptions.ChatCommandExceptionType
import lirand.api.dsl.command.types.exceptions.ChatCommandSyntaxException
import lirand.api.dsl.command.types.extensions.readUnquoted
import net.kyori.adventure.text.Component
import net.md_5.bungee.api.chat.TranslatableComponent
import org.bukkit.enchantments.Enchantment
import org.bukkit.entity.Player
import java.util.concurrent.CompletableFuture

/**
 * An [Enchantment] type.
 */
open class EnchantmentType(
	open val allowedEnchantments: (sender: Player?) -> Map<out Enchantment, Message?> = Instance.allowedEnchantments,
	open val notFoundExceptionType: ChatCommandExceptionType = Instance.notFoundExceptionType
) : WordType<Enchantment> {

	/**
	 * Returns an [Enchantment] from the result of the [allowedEnchantments]
	 * which name matches the string returned by the given [reader].
	 *
	 * @param reader the reader
	 * @return an [Enchantment] with the given name
	 * @throws ChatCommandSyntaxException if an [Enchantment] with the given name does not exist
	 */
	override fun parse(reader: StringReader): Enchantment {
		val name = reader.readUnquoted().lowercase()

		return allEnchantments[name]?.takeIf { it in allowedEnchantments(null) }
			?: throw notFoundExceptionType.createWithContext(reader, name)
	}

	/**
	 * Returns the [Enchantment] names from the result of the [allowedEnchantments]
	 * that start with the remaining input of the given [builder].
	 *
	 * @param S the type of the source
	 * @param context the context
	 * @param builder the builder
	 * @return the [Enchantment] names that start with the remaining input
	 */
	override fun <S> listSuggestions(
		context: CommandContext<S>,
		builder: SuggestionsBuilder
	): CompletableFuture<Suggestions> {
		val sender = context.source as? Player? ?: return builder.buildFuture()

		allowedEnchantments(sender).mapKeys { (enchantment, _) -> enchantment.key.key }
			.filterKeys { it.startsWith(builder.remaining, true) }
			.forEach { (enchantmentName, tooltip) ->
				if (tooltip != null)
					builder.suggest(enchantmentName, tooltip)
				else
					builder.suggest(enchantmentName)
			}

		return builder.buildFuture()
	}

	override fun getExamples(): List<String> = listOf("arrow_damage", "channeling")

	companion object Instance : EnchantmentType(
		allowedEnchantments = { Enchantment.values().associateWith { null } },
		notFoundExceptionType = ChatCommandExceptionType {
			Component.translatable("enchantment.unknown", Component.text(it[0].toString()))
		}
	) {
		val allEnchantments = Enchantment.values()
			.associateBy { it.key.key }
	}
}