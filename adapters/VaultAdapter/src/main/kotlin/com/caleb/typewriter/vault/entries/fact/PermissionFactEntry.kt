package com.caleb.typewriter.vault.entries.fact

import com.caleb.typewriter.vault.VaultAdapter
import me.gabber235.typewriter.adapters.Colors
import me.gabber235.typewriter.adapters.Entry
import me.gabber235.typewriter.adapters.modifiers.Help
import me.gabber235.typewriter.entry.Ref
import me.gabber235.typewriter.entry.emptyRef
import me.gabber235.typewriter.entry.entries.GroupEntry
import me.gabber235.typewriter.entry.entries.ReadableFactEntry
import me.gabber235.typewriter.facts.FactData
import net.milkbowl.vault.permission.Permission
import org.bukkit.entity.Player

@Entry("permission_fact", "If the player has a permission", Colors.PURPLE, "fa6-solid:user-shield")
/**
 * A [fact](/docs/creating-stories/facts) that checks if the player has a certain permission.
 *
 * ## How could this be used?
 *
 * This fact could be used to check if the player has a certain permission, for example to check if the player is an admin.
 */
class PermissionFactEntry(
    override val id: String = "",
    override val name: String = "",
    override val comment: String = "",
    override val group: Ref<GroupEntry> = emptyRef(),
    @Help("The permission to check for")
    val permission: String = "",
) : ReadableFactEntry {
    override fun readSinglePlayer(player: Player): FactData {
        val permissionHandler: Permission = VaultAdapter.permissions ?: return FactData(0)
        val value = if (permissionHandler.playerHas(player, permission)) 1 else 0
        return FactData(value)
    }
}