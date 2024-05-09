package lirand.api.dsl.command.types.extensions

import com.mojang.brigadier.Message
import com.mojang.brigadier.suggestion.SuggestionsBuilder
import lirand.api.extensions.server.nmsNumberVersion
import lirand.api.extensions.server.nmsVersion


fun SuggestionsBuilder.suggest(vararg suggestions: Pair<String, Message>): SuggestionsBuilder {
	for ((text, tooltip) in suggestions) {
		suggest(text, tooltip)
	}
	return this
}


fun SuggestionsBuilder.suggestTranslatable(text: String, tooltip: Message): SuggestionsBuilder {
	val resultTooltip = tooltip.let {
		if (it is TranslatableMessage) it.toChatMessage()
		else it
	}

	return suggest(text, resultTooltip)
}

fun SuggestionsBuilder.suggestTranslatable(vararg suggestions: Pair<String, Message>): SuggestionsBuilder {
	for ((text, tooltip) in suggestions) {
		suggestTranslatable(text, tooltip)
	}
	return this
}

class TranslatableMessage(
	val translate: String,
	vararg val with: Any
) : Message {

	fun toChatMessage(): Message {
		return chatMessageConstructor.newInstance(translate, with) as Message
	}

	override fun getString(): String = translate


	companion object {
		private val chatMessageClass = run {
			val nmsPackage = if (nmsNumberVersion < 17)
				"net.minecraft.server.v$nmsVersion"
			else
				"net.minecraft.network.chat"

			Class.forName("$nmsPackage.ChatMessage")
		}

		private val chatMessageConstructor = run {
			chatMessageClass.getConstructor(String::class.java, Array<Any>::class.java)
		}
	}
}