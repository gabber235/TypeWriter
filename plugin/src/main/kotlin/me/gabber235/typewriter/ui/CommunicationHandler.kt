package me.gabber235.typewriter.ui

import com.corundumstudio.socketio.*
import com.github.shynixn.mccoroutine.launch
import lirand.api.extensions.server.server
import me.gabber235.typewriter.Typewriter.Companion.plugin
import me.gabber235.typewriter.utils.config
import org.bukkit.event.inventory.InventoryCloseEvent
import java.time.Instant
import java.util.*


object CommunicationHandler {
	private const val BASE_URL = "https://typewriter-mc.web.app"
	private val hostName: String by config("hostname", "localhost")
	private val port: Int by config("port", 9092)
	private val auth: String by config("auth", "session") // Possible values: none, session

	// UUID session tokens with a timestamp of when they were created.
	// This is used to expire tokens after 5 minutes or when they are used.
	private val sessionTokens: MutableMap<UUID, SessionData> = mutableMapOf()

	var server: SocketIOServer? = null
		private set

	fun initialize() {
		ClientSynchronizer.initialize()
		val config = Configuration().apply {
			hostname = hostName
			port = this@CommunicationHandler.port
			setAuthorizationListener(this@CommunicationHandler::authenticate)
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

	private fun authenticate(data: HandshakeData): Boolean {
		if (auth == "none") return true
		if (auth == "session") {
			val token = data.getSingleUrlParam("token") ?: return false
			val uuid = UUID.fromString(token)
			val session = sessionTokens.remove(uuid) ?: return false
			session.closePopup()
			return session.isValid
		}
		return false
	}

	private fun generateSessionToken(playerId: UUID?): UUID {
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
		server?.stop()
		ClientSynchronizer.dispose()
		Writer.dispose()
	}
}

data class SessionData(
	val playerId: UUID?,
	val created: Instant,
) {
	companion object {
		fun create(playerId: UUID?) = SessionData(playerId, Instant.now())
	}

	/**
	 * Check if the session is still valid.
	 * A session is valid for 5 minutes after it was created.
	 * And if the player is still online.
	 *
	 * @return true if the session is still valid, false otherwise.
	 */
	val isValid: Boolean
		get() = created.plusSeconds(300)
			.isAfter(Instant.now()) && playerId?.let { server?.getPlayer(it) != null } ?: true

	/**
	 * For players, we open a book with the link.
	 * Only after they connect to the panel the session is invalidated.
	 * Hence, we want to close the book so the player can not click the link again.
	 *
	 * We assume that the player has the book still open after clicking the link and connecting as this happens in a split second.
	 */
	fun closePopup() {
		val uuid = playerId ?: return
		val player = plugin.server.getPlayer(uuid) ?: return
		plugin.launch {
			player.closeInventory(InventoryCloseEvent.Reason.PLUGIN)
		}
	}
}