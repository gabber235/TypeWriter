package me.gabber235.typewriter.entry.roadnetwork

import com.google.common.cache.CacheBuilder
import com.google.common.cache.CacheLoader
import com.google.gson.Gson
import kotlinx.coroutines.CompletableDeferred
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import me.gabber235.typewriter.entry.Query
import me.gabber235.typewriter.entry.Ref
import me.gabber235.typewriter.entry.entries.RoadNetwork
import me.gabber235.typewriter.entry.entries.RoadNetworkEntry
import me.gabber235.typewriter.logger
import me.gabber235.typewriter.plugin
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

    private var job: Job? = null

    private val editors = CacheBuilder.newBuilder()
        .expireAfterAccess(2, TimeUnit.MINUTES)
        .removalListener<Ref<out RoadNetworkEntry>, RoadNetworkEditor> { notification ->
            DISPATCHERS_ASYNC.launch {
                notification.value?.dispose()
            }
        }
        .build(CacheLoader.from(::createEditor))

    fun initialize() {
        job = DISPATCHERS_ASYNC.launch {
            while (plugin.isEnabled) {
                delay(500)
                editors.asMap().values.forEach { it.refresh() }
            }
        }
    }

    private fun loadRoadNetwork(id: String): CompletableDeferred<RoadNetwork> {
        val deferred = CompletableDeferred<RoadNetwork>()
        DISPATCHERS_ASYNC.launch {
            val network = try {
                Query.findById<RoadNetworkEntry>(id)?.loadRoadNetwork(gson)
            } catch (e: Exception) {
                logger.severe("Failed to load road network with id $id")
                null
            } ?: RoadNetwork()

            deferred.complete(network)
        }
        return deferred
    }

    suspend fun getNetwork(ref: Ref<out RoadNetworkEntry>): RoadNetwork {
        return networks.get(ref.id).await()
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    fun getNetworkOrNull(ref: Ref<out RoadNetworkEntry>): RoadNetwork? {
        val deferred = networks.get(ref.id)
        return if (deferred.isCompleted) deferred.getCompleted() else null
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

    private fun createEditor(ref: Ref<out RoadNetworkEntry>): RoadNetworkEditor {
        return RoadNetworkEditor(ref).also { it.load() }
    }

    fun getEditorNetwork(ref: Ref<out RoadNetworkEntry>): RoadNetworkEditor = editors.get(ref)

    suspend fun shutdown() {
        job?.cancel()
        networks.invalidateAll()
        editors.asMap().values.forEach { it.dispose() }
        editors.invalidateAll()
    }
}