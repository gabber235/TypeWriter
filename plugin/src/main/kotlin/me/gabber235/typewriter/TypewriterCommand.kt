package me.gabber235.typewriter

import com.mojang.brigadier.arguments.StringArgumentType.string
import lirand.api.dsl.command.builders.command
import lirand.api.dsl.command.types.PlayerType
import me.gabber235.typewriter.entry.EntryDatabase
import me.gabber235.typewriter.npc.TypeWriterTrait
import me.gabber235.typewriter.utils.msg
import net.citizensnpcs.api.CitizensAPI
import net.citizensnpcs.api.npc.NPC
import org.bukkit.command.CommandSender
import org.bukkit.plugin.Plugin

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