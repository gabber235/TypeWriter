package me.gabber235.typewriter.ui

import com.corundumstudio.socketio.Configuration
import com.corundumstudio.socketio.SocketIOServer
import me.gabber235.typewriter.Typewriter.Companion.plugin
import me.gabber235.typewriter.entry.EntryDatabase
import me.gabber235.typewriter.entry.Page
import me.gabber235.typewriter.utils.config
import me.gabber235.typewriter.utils.get


private data class PageUpdate(val page: String, val content: String)

object CommunicationHandler {

	private val hostName: String by config("hostname", "localhost")
	private val port: Int by config("port", 9092)

	private var server: SocketIOServer? = null

	fun initialize() {
		ClientSynchronizer.initialize()
		val config = Configuration().apply {
			hostname = hostName
			port = this@CommunicationHandler.port
		}

		server = SocketIOServer(config)
		server?.addEventListener("pageUpdate", PageUpdate::class.java) { _, data, ack ->
			updatePage(data)
			if (ack.isAckRequested) {
				ack.sendAckData("Page updated")
			}
		}

		server?.addEventListener("fetch", String::class.java, ClientSynchronizer::handleFetchRequest)

		server?.addConnectListener {
			plugin.logger.info("Client connected: ${it.sessionId}")
		}

		server?.addDisconnectListener {
			plugin.logger.info("Client disconnected: ${it.sessionId}")
		}

		server?.start()
	}

	private fun updatePage(update: PageUpdate) {
		val pageFile = plugin.dataFolder["pages/${update.page}.json"]

		// To prevent a security issue, we only allow files in the pages folder
		if (!pageFile.canonicalPath.startsWith(plugin.dataFolder["pages"].canonicalPath)) {
			return
		}

		val gson = EntryDatabase.gson()

		// Parse the content as JSON to prevent invalid JSON
		val page = try {
			gson.fromJson(update.content, Page::class.java)
		} catch (e: Exception) {
			// TODO: Send error to client
			return
		}

		pageFile.writeText(gson.toJson(page))
		EntryDatabase.loadEntries()
	}

	fun shutdown() {
		server?.stop()
		ClientSynchronizer.dispose()
	}
}