package me.gabber235.typewriter.ui

import com.corundumstudio.socketio.Configuration
import com.corundumstudio.socketio.SocketIOServer
import me.gabber235.typewriter.utils.config


object CommunicationHandler {

	private val hostName: String by config("hostname", "localhost")
	private val port: Int by config("port", 9092)

	var server: SocketIOServer? = null
		private set

	fun initialize() {
		ClientSynchronizer.initialize()
		val config = Configuration().apply {
			hostname = hostName
			port = this@CommunicationHandler.port
		}

		server = SocketIOServer(config)

		server?.addEventListener("fetch", String::class.java, ClientSynchronizer::handleFetchRequest)
		server?.addEventListener("createPage", String::class.java, ClientSynchronizer::handleCreatePage)
		server?.addEventListener("deletePage", String::class.java, ClientSynchronizer::handleDeletePage)
		server?.addEventListener("createEntry", String::class.java, ClientSynchronizer::handleCreateEntry)
		server?.addEventListener("updateEntry", String::class.java, ClientSynchronizer::handleEntryUpdate)
		server?.addEventListener("deleteEntry", String::class.java, ClientSynchronizer::handleDeleteEntry)

		server?.addEventListener("publish", String::class.java, ClientSynchronizer::handlePublish)

		server?.addEventListener("updateWriter", String::class.java, ClientSynchronizer::handleUpdateWriter)

		server?.addConnectListener {
			it.sendEvent("stagingState", ClientSynchronizer.stagingState.name.lowercase())

			Writer.addWriter(it.sessionId.toString())
			server.broadcastWriters()
		}

		server?.addDisconnectListener {
			server?.broadcastOperations?.sendEvent("disconnectWriter", it, it.sessionId.toString())

			Writer.removeWriter(it.sessionId.toString())
			server.broadcastWriters()
		}

		server?.start()
	}

	fun shutdown() {
		server?.stop()
		ClientSynchronizer.dispose()
	}
}