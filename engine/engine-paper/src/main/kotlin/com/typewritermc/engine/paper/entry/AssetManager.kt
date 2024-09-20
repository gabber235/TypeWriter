package com.typewritermc.engine.paper.entry

import com.typewritermc.engine.paper.entry.entries.AssetEntry
import com.typewritermc.engine.paper.plugin
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject

interface AssetStorage {
    suspend fun storeAsset(path: String, content: String)
    suspend fun containsAsset(path: String): Boolean
    suspend fun fetchAsset(path: String): Result<String>
    suspend fun deleteAsset(path: String)

    suspend fun fetchAllAssetPaths(): Set<String>
}

class AssetManager : KoinComponent {
    private val storage: AssetStorage by inject()

    fun initialize() {
    }

    suspend fun storeAsset(entry: AssetEntry, content: String) {
        storage.storeAsset(entry.path, content)
    }

    suspend fun containsAsset(entry: AssetEntry): Boolean {
        return storage.containsAsset(entry.path)
    }

    suspend fun fetchAsset(entry: AssetEntry): String? {
        val result = storage.fetchAsset(entry.path)
        if (result.isFailure) {
            plugin.logger.severe("Failed to fetch asset ${entry.path}: ${result.exceptionOrNull()?.message}")
            return null
        }
        return result.getOrNull()
    }

    fun shutdown() {
    }
}

class LocalAssetStorage : AssetStorage {
    override suspend fun storeAsset(path: String, content: String) {
        val file = plugin.dataFolder.resolve("assets/$path")
        file.parentFile.mkdirs()
        file.writeText(content)
    }

    override suspend fun containsAsset(path: String): Boolean {
        return plugin.dataFolder.resolve("assets/$path").exists()
    }

    override suspend fun fetchAsset(path: String): Result<String> {
        val file = plugin.dataFolder.resolve("assets/$path")
        if (!file.exists()) {
            return Result.failure(IllegalArgumentException("Asset $path not found."))
        }
        return Result.success(file.readText())
    }

    override suspend fun deleteAsset(path: String) {
        val asset = plugin.dataFolder.resolve("assets/$path")
        val deletedAsset = plugin.dataFolder.resolve("deleted_assets/$path")
        deletedAsset.parentFile.mkdirs()
        asset.renameTo(deletedAsset)
    }

    override suspend fun fetchAllAssetPaths(): Set<String> {
        return plugin.dataFolder.resolve("assets").walk().filter { it.isFile }
            .map { it.relativeTo(plugin.dataFolder.resolve("assets")).path }.toSet()
    }
}
