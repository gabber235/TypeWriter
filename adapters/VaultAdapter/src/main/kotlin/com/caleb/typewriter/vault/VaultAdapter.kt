package com.caleb.typewriter.vault

import App
import lirand.api.extensions.server.server
import me.gabber235.typewriter.adapters.Adapter
import me.gabber235.typewriter.adapters.TypewriterAdapter
import me.gabber235.typewriter.logger
import net.milkbowl.vault.chat.Chat
import net.milkbowl.vault.economy.Economy
import net.milkbowl.vault.permission.Permission
import org.bukkit.Bukkit.getServer
import org.bukkit.plugin.RegisteredServiceProvider

@Adapter("Vault", "For Vault", App.VERSION)
/**
 * The Vault Adapter is an adapter for the Vault plugin. It allows you to use Vault's economy system in your dialogue.
 */
object VaultAdapter : TypewriterAdapter() {
    var economy: Economy? = null
        private set

    var permissions: Permission? = null
        private set

    var chat: Chat? = null
        private set


    override fun initialize() {
        if (!server.pluginManager.isPluginEnabled("Vault")) {
            logger.warning("Vault plugin not found, try installing it or disabling the Vault adapter")
            return
        }

        loadVault()
    }

    private fun loadVault() {
        setupEconomy()
        setupPermissions()
        setupChat()
    }

    private fun setupEconomy(): Boolean {
        if (getServer().pluginManager.getPlugin("Vault") == null) {
            return false
        }
        val rsp = getServer().servicesManager.getRegistration(
            Economy::class.java
        ) ?: return false
        economy = rsp.provider
        return true
    }

    private fun setupPermissions(): Boolean {
        val rsp = getServer().servicesManager.getRegistration(
            Permission::class.java
        ) as? RegisteredServiceProvider<Permission> ?: return false
        permissions = rsp.provider
        return true
    }

    private fun setupChat(): Boolean {
        val rsp = getServer().servicesManager.getRegistration(
            Chat::class.java
        ) as? RegisteredServiceProvider<Chat> ?: return false
        chat = rsp.provider
        return true
    }
}