package com.caleb.typewriter.vault.entries.action

import com.caleb.typewriter.vault.VaultAdapter
import me.gabber235.typewriter.adapters.Colors
import me.gabber235.typewriter.adapters.Entry
import me.gabber235.typewriter.entry.Criteria
import me.gabber235.typewriter.entry.Modifier
import me.gabber235.typewriter.entry.entries.ActionEntry
import me.gabber235.typewriter.utils.Icons
import net.milkbowl.vault.chat.Chat
import org.bukkit.entity.Player

@Entry("vault_set_prefix", "[Vault] Set Prefix", Colors.RED, Icons.USER_TAG)
data class SetPrefixActionEntry(
	override val id: String = "",
	override val name: String = "",
	override val criteria: List<Criteria> = emptyList(),
	override val modifiers: List<Modifier> = emptyList(),
	override val triggers: List<String> = emptyList(),
	private val prefix: String = "",
) : ActionEntry {
	override fun execute(player: Player) {
		super.execute(player)

		val chat: Chat = VaultAdapter.chat ?: return

		chat.setPlayerPrefix(player, prefix)
	}
}