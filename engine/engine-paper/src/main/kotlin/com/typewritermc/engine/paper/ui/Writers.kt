package com.typewritermc.engine.paper.ui

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

    fun merge(writer: Writer): Writer {
        return Writer(
            id = writer.id,
            iconUrl = writer.iconUrl ?: iconUrl,
            pageId = writer.pageId,
            entryId = writer.entryId,
            field = writer.field,
        )
    }
}

class Writers : KoinComponent {
    private val communicationHandler: CommunicationHandler by inject()
    private var writers = listOf<Writer>()
    private var lastSynced = 0L


    private fun addWriter(writer: Writer) {
        writers = writers + writer
    }

    fun addWriter(id: String, iconUrl: String?) {
        addWriter(Writer(id, iconUrl))
    }

    private fun updateWriter(writer: Writer) {
        writers = writers.map { if (it.id == writer.id) it.merge(writer) else it }
        syncWriters()
    }

    fun removeWriter(id: String) {
        writers = writers.filter { it.id != id }
        syncWriters()
    }

    fun updateWriter(id: String, data: String) {
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
                val iconUrl = communicationHandler.getIconUrl(id)
                addWriter(id, iconUrl)
            }
        }
    }

    fun dispose() {
        writers = listOf()
    }

    fun serialize(): String {
        return Gson().toJson(writers)
    }
}


fun SocketIOServer?.broadcastWriters(writers: Writers) {
    this?.broadcastOperations?.sendEvent("updateWriters", writers.serialize())
}
