package me.gabber235.typewriter

import com.mojang.brigadier.StringReader
import com.mojang.brigadier.arguments.IntegerArgumentType.integer
import com.mojang.brigadier.arguments.StringArgumentType.string
import com.mojang.brigadier.context.CommandContext
import com.mojang.brigadier.suggestion.Suggestions
import com.mojang.brigadier.suggestion.SuggestionsBuilder
import lirand.api.dsl.command.builders.command
import lirand.api.dsl.command.types.PlayerType
import lirand.api.dsl.command.types.WordType
import lirand.api.dsl.command.types.exceptions.ChatCommandExceptionType
import lirand.api.dsl.command.types.extensions.readUnquoted
import me.gabber235.typewriter.entry.EntryDatabase
import me.gabber235.typewriter.extensions.npc.TypeWriterTrait
import me.gabber235.typewriter.facts.*
import me.gabber235.typewriter.interaction.InteractionHandler
import me.gabber235.typewriter.interaction.chatHistory
import me.gabber235.typewriter.utils.msg
import me.gabber235.typewriter.utils.sendMini
import net.citizensnpcs.api.CitizensAPI
import net.citizensnpcs.api.npc.NPC
import org.bukkit.command.CommandSender
import org.bukkit.entity.Player
import org.bukkit.plugin.Plugin
import java.time.format.DateTimeFormatter
import java.util.concurrent.CompletableFuture

fun Plugin.typeWriterCommand() = command("typewriter") {
	requiresPermissions("typewriter.use")
	alias("tw")

	literal("reload") {
		executes {
			source.msg("Reloading configuration...")
			EntryDatabase.loadEntries()
			source.msg("Configuration reloaded!")
		}
	}

	literal("facts") {
		fun Player.listFacts(source: CommandSender) {
			val facts = facts
			if (facts.isEmpty()) {
				source.msg("$name has no facts.")
			} else {
				source.sendMini("\n\n")
				val formatter = DateTimeFormatter.ofPattern("HH:mm:ss dd/MM/yyyy")
				source.msg("$name has the following facts:\n")
				facts.map { it to EntryDatabase.getFact(it.id) }.forEach { (fact, entry) ->
					if (entry == null) return@forEach
					source.sendMini(
						"<hover:show_text:'${
							entry.comment.replace(
								Regex(" +"),
								" "
							)
						}\n\n<gray><i>Click to modify'><click:suggest_command:'/tw facts $name set ${entry.name} ${fact.value}'><gray> - </gray><blue>${entry.formattedName}:</blue> ${fact.value} <gray><i>(${
							formatter.format(
								fact.lastUpdate
							)
						})</i></gray>"
					)
				}
			}
		}


		argument("player", PlayerType.Instance) { player ->
			executes {
				player.get().listFacts(source)
			}

			literal("set") {
				argument("fact", FactType.Instance) { fact ->
					argument("value", integer(0)) { value ->
						executes {
							FactDatabase.modify(player.get().uniqueId) {
								set(fact.get().id, value.get())
							}
							source.msg("Set <blue>${fact.get().formattedName}</blue> to ${value.get()} for ${player.get().name}")
						}
					}
				}
			}

			literal("reset") {
				executes {
					val p = player.get()
					FactDatabase.modify(p.uniqueId) {
						p.facts.forEach { (id, _) ->
							set(id, 0)
						}
					}
					source.msg("All facts for ${p.name} have been reset.")
				}
			}
		}

		executesPlayer {
			source.listFacts(source)
		}
		literal("set") {
			argument("fact", FactType.Instance) { fact ->
				argument("value", integer(0)) { value ->
					executesPlayer {
						FactDatabase.modify(source.uniqueId) {
							set(fact.get().id, value.get())
						}
						source.msg("Fact <blue>${fact.get().formattedName}</blue> set to ${value.get()}.")
					}
				}
			}
		}

		literal("reset") {
			executesPlayer {
				FactDatabase.modify(source.uniqueId) {
					source.facts.forEach { (id, _) ->
						set(id, 0)
					}
				}
				source.msg("All your facts have been reset.")
			}
		}
	}

	literal("event") {
		literal("run") {
			argument("event", EventType) { event ->
				executes {
					source.msg("Running event <yellow>${event.get()}</yellow>...")
					InteractionHandler.startInteractionAndTrigger(source as Player, listOf(event.get()))
				}
			}
		}
	}

	literal("clearChat") {
		executesPlayer {
			source.chatHistory.let {
				it.clear()
				it.resendMessages(source)
			}
		}
	}

	if (server.pluginManager.isPluginEnabled("Citizens")) {
		literal("npc") {
			literal("identifier") {
				argument("name", string()) { name ->
					fun NPC?.setIdentifier(source: CommandSender, name: String) {
						if (this == null) {
							source.msg("No NPC selected!")
							return
						}

						val trait = this.getOrAddTrait(TypeWriterTrait::class.java)
						trait.identifier = name

						source.msg("Identifier set to $name")
					}

					executesPlayer {
						val npc = CitizensAPI.getDefaultNPCSelector().getSelected(source)
						npc.setIdentifier(source, name.get())
					}


					argument("target", PlayerType.Instance) { player ->
						executes {
							val npc = CitizensAPI.getDefaultNPCSelector().getSelected(player.get())
							npc.setIdentifier(source, name.get())
						}
					}
				}
			}
		}
	}
}

open class FactType(
	open val notFoundExceptionType: ChatCommandExceptionType = PlayerType.notFoundExceptionType
) : WordType<FactEntry> {
	companion object Instance : FactType()

	override fun parse(reader: StringReader): FactEntry {
		val name = reader.readUnquoted()
		return EntryDatabase.findFactByName(name) ?: throw notFoundExceptionType.create(name)
	}


	override fun <S> listSuggestions(
		context: CommandContext<S>,
		builder: SuggestionsBuilder
	): CompletableFuture<Suggestions> {

		EntryDatabase.facts.filter { it.name.startsWith(builder.remaining, true) }.forEach {
			builder.suggest(it.name)
		}

		return builder.buildFuture()
	}

	override fun getExamples(): Collection<String> = listOf("test.fact", "key.some_fact")

}

open class EventType() : WordType<String> {
	companion object Instance : EventType()

	override fun parse(reader: StringReader): String = reader.readUnquoted()


	override fun <S> listSuggestions(
		context: CommandContext<S>,
		builder: SuggestionsBuilder
	): CompletableFuture<Suggestions> {

		EntryDatabase.getAllTriggers().filter { it.startsWith(builder.remaining, true) }.forEach {
			builder.suggest(it)
		}

		return builder.buildFuture()
	}

	override fun getExamples(): Collection<String> = listOf("test.fact", "key.some_event")

}