package me.gabber235.typewriter.ui

import com.corundumstudio.socketio.AckRequest
import com.corundumstudio.socketio.SocketIOClient
import com.github.shynixn.mccoroutine.bukkit.launch
import com.google.gson.Gson
import com.google.gson.JsonArray
import com.google.gson.JsonElement
import com.google.gson.JsonObject
import lirand.api.extensions.server.server
import me.gabber235.typewriter.adapters.AdapterLoader
import me.gabber235.typewriter.capture.RecorderRequestContext
import me.gabber235.typewriter.capture.Recorders
import me.gabber235.typewriter.entry.StagingManager
import me.gabber235.typewriter.logger
import me.gabber235.typewriter.plugin
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject
import org.koin.core.qualifier.named

interface ClientSynchronizer {
    fun handleFetchRequest(client: SocketIOClient, data: String, ack: AckRequest)

    fun handleCreatePage(client: SocketIOClient, data: String, ack: AckRequest)

    fun handleRenamePage(client: SocketIOClient, data: String, ackRequest: AckRequest)
    fun handleChangePageValue(client: SocketIOClient, data: String, ackRequest: AckRequest)

    fun handleDeletePage(client: SocketIOClient, name: String, ack: AckRequest)

    fun handleCreateEntry(client: SocketIOClient, data: String, ack: AckRequest)

    fun handleEntryFieldUpdate(client: SocketIOClient, data: String, ack: AckRequest)

    fun handleEntryUpdate(client: SocketIOClient, data: String, ack: AckRequest)
    fun handleReorderEntry(client: SocketIOClient, data: String, ack: AckRequest)
    fun handleDeleteEntry(client: SocketIOClient, data: String, ack: AckRequest)
    fun handlePublish(client: SocketIOClient, data: String, ack: AckRequest)
    fun handleUpdateWriter(client: SocketIOClient, data: String, ack: AckRequest)

    fun handleCaptureRequest(client: SocketIOClient, data: String, ack: AckRequest)

    fun sendEntryFieldUpdate(pageId: String, entryId: String, fieldPath: String, data: JsonElement)
}

class ClientSynchronizerImpl : ClientSynchronizer, KoinComponent {
    private val stagingManager: StagingManager by inject()
    private val communicationHandler: CommunicationHandler by inject()
    private val writers: Writers by inject()
    private val adapterLoader: AdapterLoader by inject()
    private val adapters by adapterLoader::adaptersJson
    private val gson: Gson by inject(named("bukkitDataParser"))
    private val recorders: Recorders by inject()

    override fun handleFetchRequest(client: SocketIOClient, data: String, ack: AckRequest) {
        if (data == "pages") {
            val array = JsonArray()
            stagingManager.fetchPages().forEach { (_, page) ->
                array.add(page)
            }
            ack.sendAckData(array.toString())
        } else if (data == "adapters") {
            ack.sendAckData(adapters.toString())
        }

        ack.sendAckData("No data found")
    }

    override fun handleCreatePage(client: SocketIOClient, data: String, ack: AckRequest) {
        val json = gson.fromJson(data, JsonObject::class.java)
        val result = stagingManager.createPage(json)

        ack.sendResult(result) {
            communicationHandler.server?.broadcastOperations?.sendEvent("createPage", client, data)
        }
    }

    override fun handleRenamePage(client: SocketIOClient, data: String, ackRequest: AckRequest) {
        val json = gson.fromJson(data, PageRename::class.java)
        val result = stagingManager.renamePage(json.old, json.new)

        ackRequest.sendResult(result) {
            communicationHandler.server?.broadcastOperations?.sendEvent("renamePage", client, data)
        }
    }

    override fun handleChangePageValue(client: SocketIOClient, data: String, ackRequest: AckRequest) {
        val json = gson.fromJson(data, PageValueUpdate::class.java)
        val result = stagingManager.changePageValue(json.pageId, json.path, json.value)

        ackRequest.sendResult(result) {
            communicationHandler.server?.broadcastOperations?.sendEvent("changePageValue", client, data)
        }
    }

    override fun handleDeletePage(client: SocketIOClient, name: String, ack: AckRequest) {
        val result = stagingManager.deletePage(name)
        ack.sendResult(result) {
            communicationHandler.server?.broadcastOperations?.sendEvent("deletePage", client, name)
        }
    }

    override fun handleCreateEntry(client: SocketIOClient, data: String, ack: AckRequest) {
        val json = gson.fromJson(data, EntryCreate::class.java)
        val result = stagingManager.createEntry(json.pageId, json.entry)
        ack.sendResult(result) {
            communicationHandler.server?.broadcastOperations?.sendEvent("createEntry", client, data)
        }
    }

    override fun handleEntryFieldUpdate(client: SocketIOClient, data: String, ack: AckRequest) {
        val update = gson.fromJson(data, EntryUpdate::class.java)
        val result = stagingManager.updateEntryField(update.pageId, update.entryId, update.path, update.value)
        ack.sendResult(result) {
            communicationHandler.server?.broadcastOperations?.sendEvent("updateEntry", client, data)
        }
    }

    override fun handleEntryUpdate(client: SocketIOClient, data: String, ack: AckRequest) {
        val update = gson.fromJson(data, CompleteEntryUpdate::class.java)
        val result = stagingManager.updateEntry(update.pageId, update.entry)
        ack.sendResult(result) {
            communicationHandler.server?.broadcastOperations?.sendEvent("updateCompleteEntry", client, data)
        }
    }

    override fun handleReorderEntry(client: SocketIOClient, data: String, ack: AckRequest) {
        val update = gson.fromJson(data, ReorderEntry::class.java)
        val result = stagingManager.reorderEntry(update.pageId, update.entryId, update.newIndex)
        ack.sendResult(result) {
            communicationHandler.server?.broadcastOperations?.sendEvent("reorderEntry", client, data)
        }
    }


    override fun handleDeleteEntry(client: SocketIOClient, data: String, ack: AckRequest) {
        val json = gson.fromJson(data, EntryDelete::class.java)
        val result = stagingManager.deleteEntry(json.pageId, json.entryId)
        ack.sendResult(result) {
            communicationHandler.server?.broadcastOperations?.sendEvent("deleteEntry", client, data)
        }
    }


    override fun handlePublish(client: SocketIOClient, data: String, ack: AckRequest) {
        plugin.launch {
            val result = stagingManager.publish()
            ack.sendResult(result)
        }
    }

    override fun handleUpdateWriter(client: SocketIOClient, data: String, ack: AckRequest) {
        writers.updateWriter(client.sessionId.toString(), data)
        communicationHandler.server.broadcastWriters(writers)
        ack.sendResult(Result.success("Writer updated"))
    }

    override fun handleCaptureRequest(client: SocketIOClient, data: String, ack: AckRequest) {
        val context = gson.fromJson(data, RecorderRequestContext::class.java)

        var player = communicationHandler.getPlayer(client)

        if (player == null) {
            // If we have authentication enabled, we don't want to fallback as it could be a security issue.
            if (communicationHandler.authenticationEnabled) {
                ack.sendResult(Result.failure(Exception("Could not determine player")))
                return
            }

            // If we have no authentication, we can assume that it's a local server,
            // and we can fall back to the first player.

            val onlinePlayers = server.onlinePlayers
            if (onlinePlayers.isEmpty()) {
                ack.sendResult(Result.failure(Exception("No players online to record")))
                return
            }

            if (onlinePlayers.size > 1) {
                ack.sendResult(Result.failure(Exception("Could not determine player to record")))
                return
            }

            player = onlinePlayers.first()
            logger.warning("Could not determine player from session, using ${player.name}")
        }

        val result = recorders.requestRecording(player!!, context)
        ack.sendResult(result.toResult())
    }

    override fun sendEntryFieldUpdate(pageId: String, entryId: String, fieldPath: String, data: JsonElement) {
        val update = EntryUpdate(pageId, entryId, fieldPath, data)
        val updateData = gson.toJson(update)
        communicationHandler.server?.broadcastOperations?.sendEvent("updateEntry", updateData)
    }
}

fun AckRequest.sendResult(result: Result<String>) {
    val json = JsonObject()
    json.addProperty("success", result.isSuccess)
    json.addProperty("message", result.getOrElse { it.message })

    sendAckData(json.toString())

    if (result.isFailure) {
        logger.severe(result.exceptionOrNull()?.message)
    }
}

inline fun AckRequest.sendResult(result: Result<String>, onSuccess: () -> Unit) {
    sendResult(result)
    if (result.isSuccess) {
        onSuccess()
    }
}

private data class PageRename(val old: String, val new: String)

private data class PageValueUpdate(
    val pageId: String,
    val path: String,
    val value: JsonElement
)

private data class EntryCreate(
    val pageId: String,
    val entry: JsonObject,
)

private data class EntryUpdate(
    val pageId: String,
    val entryId: String,
    val path: String,
    val value: JsonElement
)

private data class CompleteEntryUpdate(
    val pageId: String,
    val entry: JsonObject,
)

private data class ReorderEntry(
    val pageId: String,
    val entryId: String,
    val newIndex: Int,
)

private data class EntryDelete(
    val pageId: String,
    val entryId: String,
)
