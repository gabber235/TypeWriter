package me.gabber235.typewriter.ui

import com.corundumstudio.socketio.SocketIOServer
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject

data class Writer(
    val id: String = "",
    val iconUrl: String? = null,
    val pageId: String? = null,
    val entryId: String? = null,
    val field: String? = null,
) {
    companion object
}

interface Writers {
    fun addWriter(id: String, iconUrl: String? = null)
    fun removeWriter(id: String)
    fun updateWriter(id: String, data: String)

    fun dispose()

    fun serialize(): String
}

class WritersImpl : Writers, KoinComponent {
    private val communicationHandler: CommunicationHandler by inject()
    private var writers = listOf<Writer>()
    private var lastSynced = 0L


    private fun addWriter(writer: Writer) {
        writers = writers + writer
    }

    override fun addWriter(id: String, iconUrl: String?) {
        addWriter(Writer(id, iconUrl))
    }

    private fun updateWriter(writer: Writer) {
        writers = writers.map { if (it.id == writer.id) writer else it }
        syncWriters()
    }

    override fun removeWriter(id: String) {
        writers = writers.filter { it.id != id }
        syncWriters()
    }

    override fun updateWriter(id: String, data: String) {
        val map: Map<String, Any> = Gson().fromJson(data, object : TypeToken<Map<String, Any>>() {}.type)

        val writer = Writer(
            id = id,
            pageId = map["pageId"] as String?,
            entryId = map["entryId"] as String?,
            field = map["field"] as String?,
        )

        updateWriter(writer)
    }

    /**
     * This is purly for backup. It will sync the writers every 30 seconds.
     * This is to make sure that the writers are synced even if the client
     * disconnects.
     */
    private fun syncWriters() {
        val now = System.currentTimeMillis()
        if (now - lastSynced < 30_000) return
        lastSynced = now

        val clientIds =
            communicationHandler.server?.allClients?.map { it.sessionId.toString() }
                ?: return
        // If a writer is not connected, remove it
        writers = writers.filter { clientIds.contains(it.id) }
        // If a client is not a writer, add it
        clientIds.forEach { id ->
            if (writers.none { it.id == id }) {
                addWriter(id)
            }
        }
    }

    override fun dispose() {
        writers = listOf()
    }

    override fun serialize(): String {
        return Gson().toJson(writers)
    }
}


fun SocketIOServer?.broadcastWriters(writers: Writers) {
    this?.broadcastOperations?.sendEvent("updateWriters", writers.serialize())
}
