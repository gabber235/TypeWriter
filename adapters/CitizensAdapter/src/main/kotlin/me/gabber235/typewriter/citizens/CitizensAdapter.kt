package me.gabber235.typewriter.citizens

import App
import me.gabber235.typewriter.Typewriter.Companion.plugin
import me.gabber235.typewriter.adapters.Adapter
import me.gabber235.typewriter.adapters.TypewriteAdapter
import net.citizensnpcs.api.CitizensAPI
import net.citizensnpcs.api.npc.MemoryNPCDataStore
import net.citizensnpcs.api.npc.NPCRegistry
import net.citizensnpcs.api.trait.TraitInfo

@Adapter("Citizens", "For the Citizens plugin", App.VERSION)
object CitizensAdapter : TypewriteAdapter() {
    private var tmpRegistry: NPCRegistry? = null
    val temporaryRegistry: NPCRegistry
        get() = tmpRegistry ?: CitizensAPI.createAnonymousNPCRegistry(MemoryNPCDataStore()).also { tmpRegistry = it }

    override fun initialize() {
        if (!plugin.server.pluginManager.isPluginEnabled("Citizens")) {
            plugin.logger.warning("Citizens plugin not found, try installing it or disabling the Citizens adapter")
            return
        }

        CitizensAPI.getTraitFactory().registerTrait(TraitInfo.create(TypewriterTrait::class.java))
        tmpRegistry = CitizensAPI.createAnonymousNPCRegistry(MemoryNPCDataStore())
    }

    override fun shutdown() {
        CitizensAPI.getTraitFactory().deregisterTrait(TraitInfo.create(TypewriterTrait::class.java))
        tmpRegistry?.deregisterAll()
        tmpRegistry = null
    }
}