package me.gabber235.typewriter.entry.roadnetwork

import com.google.common.cache.CacheBuilder
import com.google.common.cache.CacheLoader
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import kotlinx.coroutines.CompletableDeferred
import me.gabber235.typewriter.entry.Query
import me.gabber235.typewriter.entry.Ref
import me.gabber235.typewriter.entry.entries.RoadNetwork
import me.gabber235.typewriter.entry.entries.RoadNetworkEntry
import me.gabber235.typewriter.entry.entries.data
import me.gabber235.typewriter.logger
import me.gabber235.typewriter.utils.ThreadType.DISPATCHERS_ASYNC
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject
import org.koin.core.qualifier.named
import java.util.concurrent.TimeUnit

class RoadNetworkManager : KoinComponent {
    private val gson by inject<Gson>(named("roadNetworkParser"))
    private val networks = CacheBuilder.newBuilder()
        .expireAfterAccess(10, TimeUnit.MINUTES)
        .build(CacheLoader.from(::loadRoadNetwork))

    fun initialize() {
    }

    private fun loadRoadNetwork(id: String): CompletableDeferred<RoadNetwork> {
        val deferred = CompletableDeferred<RoadNetwork>()
        DISPATCHERS_ASYNC.launch {
            val network = try {
                Query.findById<RoadNetworkEntry>(id)?.loadRoadNetwork(gson)
            } catch (e: Exception) {
                logger.severe("Failed to load road network with id $id")
                null
            } ?: RoadNetwork(emptyList(), emptyList(), emptyList())

            deferred.complete(network)
        }
        return deferred
    }

    suspend fun getNetwork(ref: Ref<out RoadNetworkEntry>): RoadNetwork {
        return networks.get(ref.id).await()
    }

    internal suspend fun saveRoadNetwork(ref: Ref<out RoadNetworkEntry>, network: RoadNetwork) {
        val entry = ref.get()
        if (entry == null) {
            logger.severe("Failed to save road network with id ${ref.id}")
            return
        }
        entry.saveRoadNetwork(gson, network)
        networks.put(ref.id, CompletableDeferred(network))
    }

    fun shutdown() {
        networks.invalidateAll()
    }
}