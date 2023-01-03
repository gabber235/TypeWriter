package me.gabber235.typewriter.ui

import com.corundumstudio.socketio.SocketIOServer
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

data class Writer(
	val id: String = "",
	val pageId: String? = null,
	val entryId: String? = null,
	val field: String? = null,
) {
	companion object {
		private var writers = listOf<Writer>()
		private var lastSynced = 0L


		private fun addWriter(writer: Writer) {
			writers = writers + writer
		}

		fun addWriter(id: String) {
			addWriter(Writer(id))
		}

		private fun updateWriter(writer: Writer) {
			writers = writers.map { if (it.id == writer.id) writer else it }
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
				CommunicationHandler.server?.allClients?.map { it.sessionId.toString() }
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

		fun dispose() {
			writers = listOf()
		}

		fun serialize(): String {
			return Gson().toJson(writers)
		}
	}
}


fun SocketIOServer?.broadcastWriters() {
	this?.broadcastOperations?.sendEvent("updateWriters", Writer.serialize())
}
