package me.gabber235.typewriter.citizens

import com.mojang.brigadier.arguments.StringArgumentType
import lirand.api.dsl.command.builders.command
import lirand.api.dsl.command.types.PlayerType
import me.gabber235.typewriter.utils.msg
import net.citizensnpcs.api.CitizensAPI
import net.citizensnpcs.api.npc.NPC
import org.bukkit.command.CommandSender
import org.bukkit.plugin.Plugin

fun Plugin.npcCommand() = command("typewriternpc") {
	requiresPermissions("typewriter.npc")

	alias("twnpc")

	argument("name", StringArgumentType.string()) { name ->
		fun NPC?.setIdentifier(source: CommandSender, name: String) {
			if (this == null) {
				source.msg("No NPC selected!")
				return
			}

			val trait = this.getOrAddTrait(TypewriterTrait::class.java)
			trait.identifier = name

			source.msg("Identifier set to $name")
		}

		executesPlayer {
			val npc = CitizensAPI.getDefaultNPCSelector().getSelected(source)
			npc.setIdentifier(source, name.get())
		}


		argument("target", PlayerType) { player ->
			executes {
				val npc = CitizensAPI.getDefaultNPCSelector().getSelected(player.get())
				npc.setIdentifier(source, name.get())
			}
		}
	}
}