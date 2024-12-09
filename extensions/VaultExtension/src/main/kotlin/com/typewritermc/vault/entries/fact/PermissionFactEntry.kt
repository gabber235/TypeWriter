package com.typewritermc.vault.entries.fact

import com.typewritermc.vault.VaultInitializer
import com.typewritermc.core.books.pages.Colors
import com.typewritermc.core.extension.annotations.Entry
import com.typewritermc.core.extension.annotations.Help
import com.typewritermc.core.entries.Ref
import com.typewritermc.core.entries.emptyRef
import com.typewritermc.engine.paper.entry.entries.GroupEntry
import com.typewritermc.engine.paper.entry.entries.ReadableFactEntry
import com.typewritermc.engine.paper.facts.FactData
import net.milkbowl.vault.permission.Permission
import org.bukkit.entity.Player
import org.koin.java.KoinJavaComponent

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
        val permissionHandler: Permission = KoinJavaComponent.get<VaultInitializer>(VaultInitializer::class.java).permissions ?: return FactData(0)
        val value = if (permissionHandler.playerHas(player, permission)) 1 else 0
        return FactData(value)
    }
}