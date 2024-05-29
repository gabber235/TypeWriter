package me.gabber235.typewriter.entry

import com.google.gson.Gson
import com.google.gson.stream.JsonReader
import me.gabber235.typewriter.database.Database
import me.gabber235.typewriter.entry.entries.AssetEntry
import me.gabber235.typewriter.plugin
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject
import org.koin.core.qualifier.named
import java.io.StringReader

class AssetManager : KoinComponent {
    private val database: Database by inject()
    private val stagingManager: StagingManager by inject()
    private val gson: Gson by inject(named("entryParser"))

    fun initialize() {
    }

    private fun removeUnusedAssets() {
        val usedPaths = usedPaths()
        if (usedPaths.isFailure) {
            plugin.logger.severe("Failed to remove unused assets: ${usedPaths.exceptionOrNull()?.message}")
            return
        }

        val unusedPaths = database.fetchAllAssetPaths().subtract((usedPaths.getOrNull() ?: emptySet()).toSet())
        unusedPaths.forEach {
            database.deleteAsset(it)
        }
    }

    /**
     * Find all the paths that either have a reference in the database or are in the staging area.
     */
    private fun usedPaths(): Result<Set<String>> {
        val stagingPages = stagingManager.fetchPages().map { (id, data) ->
            id to JsonReader(StringReader(data.toString()))
        }.map { (id, reader) ->
            reader.parsePage(id, gson)
        }

        if (stagingPages.any { it.isFailure }) {
            return Result.failure(stagingPages.first { it.isFailure }.exceptionOrNull()!!)
        }

        val stagingPaths = stagingPages.flatMap {
            it.getOrThrow().entries
        }.filterIsInstance<AssetEntry>().map {
            it.path
        }.toSet()

        val productionPaths = Query.find<AssetEntry>().map {
            it.path
        }.toSet()

        return Result.success(stagingPaths.union(productionPaths))
    }

    fun storeAsset(entry: AssetEntry, content: String) {
        database.storeAsset(entry.path, content)
    }

    fun fetchAsset(entry: AssetEntry): String? {
        val result = database.fetchAsset(entry.path)
        if (result.isFailure) {
            plugin.logger.severe("Failed to fetch asset ${entry.path}")
            return null
        }
        return result.getOrNull()
    }

    fun shutdown() {
        removeUnusedAssets()
    }
}
