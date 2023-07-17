package me.gabber235.typewriter.citizens

import App
import me.gabber235.typewriter.adapters.Adapter
import me.gabber235.typewriter.adapters.TypewriteAdapter
import me.gabber235.typewriter.logger
import me.gabber235.typewriter.plugin
import net.citizensnpcs.api.CitizensAPI
import net.citizensnpcs.api.npc.MemoryNPCDataStore
import net.citizensnpcs.api.npc.NPC
import net.citizensnpcs.api.npc.NPCDataStore
import net.citizensnpcs.api.npc.NPCRegistry
import net.citizensnpcs.api.trait.TraitInfo

@Adapter("Citizens", "For the Citizens plugin", App.VERSION)
/**
 * The Citizens adapter allows you to create custom interactions with NPCs.
 */
object CitizensAdapter : TypewriteAdapter() {
    private var tmpRegistry: NPCRegistry? = null
    val temporaryRegistry: NPCRegistry
        get() = tmpRegistry ?: CitizensAPI.createAnonymousNPCRegistry(MemoryNPCDataStore()).also { tmpRegistry = it }

    override fun initialize() {
        if (!plugin.server.pluginManager.isPluginEnabled("Citizens")) {
            logger.warning("Citizens plugin not found, try installing it or disabling the Citizens adapter")
            return
        }

        CitizensAPI.getTraitFactory().registerTrait(TraitInfo.create(TypewriterTrait::class.java))
        tmpRegistry = CitizensAPI.createAnonymousNPCRegistry(MemoryNPCDataStore(23500))
    }

    override fun shutdown() {
        CitizensAPI.getTraitFactory().deregisterTrait(TraitInfo.create(TypewriterTrait::class.java))
        tmpRegistry?.deregisterAll()
        tmpRegistry = null
    }
}


class MemoryNPCDataStore(private var lastID: Int = 0) : NPCDataStore {
    override fun clearData(npc: NPC) {}
    override fun createUniqueNPCId(registry: NPCRegistry): Int {
        return lastID++
    }

    override fun loadInto(registry: NPCRegistry) {}
    override fun saveToDisk() {}
    override fun saveToDiskImmediate() {}
    override fun store(npc: NPC) {}
    override fun storeAll(registry: NPCRegistry) {}
    override fun reloadFromSource() {}
}