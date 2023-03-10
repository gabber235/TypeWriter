package me.gabber235.typewriter.ui

import com.corundumstudio.socketio.*
import lirand.api.extensions.server.server
import me.gabber235.typewriter.Typewriter.Companion.plugin
import me.gabber235.typewriter.utils.config
import me.gabber235.typewriter.utils.logErrorIfNull
import java.time.Instant
import java.util.*
import kotlin.collections.set


object CommunicationHandler {
	private val hostName: String by config("hostname", "localhost")
	private val panelPort: Int by config("panel.port", 8080)
	private val BASE_URL get() = "http://${hostName}:${panelPort}/#"
	private val enabled: Boolean by config("enabled", false)
	private val port: Int by config("websocket.port", 9092)
	private val auth: String by config("websocket.auth", "session") // Possible values: none, session

	private val sessionTokens: MutableMap<UUID, SessionData> = mutableMapOf()

	var server: SocketIOServer? = null
		private set

	fun initialize() {
		if (!enabled) return
		plugin.logger.warning("Websocket server is enabled. This is not recommended for production servers.")
		ClientSynchronizer.initialize()
		PanelHost.initialize()
		val config = Configuration().apply {
			hostname = "0.0.0.0"
			port = this@CommunicationHandler.port
			setAuthorizationListener(this@CommunicationHandler::authenticate)
		}

		server = SocketIOServer(config)

		server?.addEventListener("fetch", String::class.java, ClientSynchronizer::handleFetchRequest)
		server?.addEventListener("createPage", String::class.java, ClientSynchronizer::handleCreatePage)
		server?.addEventListener("renamePage", String::class.java, ClientSynchronizer::handleRenamePage)
		server?.addEventListener("deletePage", String::class.java, ClientSynchronizer::handleDeletePage)
		server?.addEventListener("createEntry", String::class.java, ClientSynchronizer::handleCreateEntry)
		server?.addEventListener("updateEntry", String::class.java, ClientSynchronizer::handleEntryUpdate)
		server?.addEventListener(
			"updateCompleteEntry",
			String::class.java,
			ClientSynchronizer::handleCompleteEntryUpdate
		)
		server?.addEventListener("reorderEntry", String::class.java, ClientSynchronizer::handleReorderEntry)
		server?.addEventListener("deleteEntry", String::class.java, ClientSynchronizer::handleDeleteEntry)

		server?.addEventListener("publish", String::class.java, ClientSynchronizer::handlePublish)

		server?.addEventListener("updateWriter", String::class.java, ClientSynchronizer::handleUpdateWriter)

		server?.addConnectListener { socket ->
			plugin.logger.info("Client connected: ${socket.remoteAddress}")
			socket.sendEvent("stagingState", ClientSynchronizer.stagingState.name.lowercase())

			val token = getSessionToken(socket.handshakeData)
			if (token != null) {
				sessionTokens.computeIfPresent(token) { _, session ->
					session.copy(lastConnectionId = socket.sessionId)
				}
			}

			val iconUrl = sessionTokens[token]?.playerId?.let {
				"https://crafatar.com/avatars/$it?size=32&overlay"
			}

			Writer.addWriter(socket.sessionId.toString(), iconUrl)
			server.broadcastWriters()
		}

		server?.addDisconnectListener {
			plugin.logger.info("Client disconnected: ${it.remoteAddress}")
			server?.broadcastOperations?.sendEvent("disconnectWriter", it, it.sessionId.toString())

			Writer.removeWriter(it.sessionId.toString())
			server.broadcastWriters()
		}

		server?.start()
	}

	private fun authenticate(data: HandshakeData): Boolean {
		if (auth == "none") return true
		if (auth == "session") {
			val token = getSessionToken(data)
				.logErrorIfNull("${data.address} tried to connect to the socket without token!") ?: return false
			val session = sessionTokens[token] ?: return false
			return session.isValid
		}
		return false
	}

	private fun getSessionToken(data: HandshakeData): UUID? {
		if (auth == "none") return null
		if (auth == "session") {
			val token = data.getSingleUrlParam("token") ?: return null
			return UUID.fromString(token)
		}
		return null
	}

	private fun generateSessionToken(playerId: UUID?): UUID {
		// If there already is a token for this player, use that one.
		val existingToken = sessionTokens.filter { it.value.playerId == playerId }.keys.firstOrNull()
		if (existingToken != null) return existingToken

		// Otherwise, create a new token.
		val token = UUID.randomUUID()
		sessionTokens[token] = SessionData.create(playerId)
		return token
	}

	fun generateUrl(playerId: UUID?): String {
		if (auth == "none") return "$BASE_URL/connect?host=$hostName&port=$port"
		if (auth == "session") {
			val token = generateSessionToken(playerId)
			return "$BASE_URL/connect?host=$hostName&port=$port&token=$token"
		}
		return ""
	}

	fun shutdown() {
		if (!enabled) return
		server?.stop()
		PanelHost.dispose()
		ClientSynchronizer.dispose()
		Writer.dispose()
	}
}

data class SessionData(
	val playerId: UUID?,
	val created: Instant,
	val lastConnectionId: UUID? = null,
) {
	companion object {
		fun create(playerId: UUID?) = SessionData(playerId, Instant.now())
	}

	/**
	 * Check if the session is still valid.
	 *
	 * A session is valid if the player is still online.
	 *
	 * @return true if the session is still valid, false otherwise.
	 */
	val isValid: Boolean
		get() = playerId?.let { server.getPlayer(it) != null } ?: true
}