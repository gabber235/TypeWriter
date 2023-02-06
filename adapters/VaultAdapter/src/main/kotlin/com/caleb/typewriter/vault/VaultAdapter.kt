package com.caleb.typewriter.vault

import lirand.api.extensions.server.server
import me.gabber235.typewriter.Typewriter.Companion.plugin
import me.gabber235.typewriter.adapters.Adapter
import me.gabber235.typewriter.adapters.TypewriteAdapter
import net.milkbowl.vault.chat.Chat
import net.milkbowl.vault.economy.Economy
import net.milkbowl.vault.permission.Permission
import org.bukkit.Bukkit.getServer
import org.bukkit.plugin.RegisteredServiceProvider


@Adapter("vault", "For Vault", "0.0.1")
object VaultAdapter : TypewriteAdapter() {

	var economy: Economy? = null
	private set

	var permissions: Permission? = null
	private set

	var chat: Chat? = null
	private set


	override fun initialize() {
		if (!server.pluginManager.isPluginEnabled("Vault")) {
			plugin.logger.warning("Vault plugin not found, try installing it or disabling the Vault adapter")
		}

		loadVault()

		println("VaultAdapter initialized")
	}

	private fun loadVault() {
		println("${setupEconomy()} economy")
		println("${setupPermissions()} permissions")
		println("${setupChat()} chat")
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
		val rsp: RegisteredServiceProvider<Permission> = getServer().servicesManager.getRegistration(
			Permission::class.java
		) as RegisteredServiceProvider<Permission>
		permissions = rsp.provider
		return true
	}

	private fun setupChat(): Boolean {
		val rsp: RegisteredServiceProvider<Chat> = getServer().servicesManager.getRegistration(
			Chat::class.java
		) as RegisteredServiceProvider<Chat>
		chat = rsp.provider
		return true
	}


}