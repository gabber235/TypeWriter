package me.gabber235.typewriter.ui

import com.corundumstudio.socketio.AckRequest
import com.corundumstudio.socketio.SocketIOClient
import com.google.gson.*
import me.gabber235.typewriter.Typewriter.Companion.plugin
import me.gabber235.typewriter.entry.EntryDatabase
import me.gabber235.typewriter.utils.get

object ClientSynchronizer {
	private val pages: MutableMap<String, JsonObject> = mutableMapOf()
	private lateinit var adapters: JsonElement

	private lateinit var gson: Gson

	fun initialize() {
		gson = EntryDatabase.gson()
		// Read the pages from the file
		val dir = plugin.dataFolder["pages"]
		dir.listFiles { file -> file.name.endsWith(".json") }?.forEach { file ->
			val page = file.readText()
			pages[file.nameWithoutExtension] = gson.fromJson(page, JsonObject::class.java)
		}

		// Read the adapters from the file
		adapters = gson.fromJson(plugin.dataFolder["adapters.json"].readText(), JsonElement::class.java)
	}

	fun handleFetchRequest(client: SocketIOClient, data: String, ack: AckRequest) {
		if (data == "pages") {
			val array = JsonArray()
			pages.forEach { (name, page) ->
				val obj = JsonObject()
				obj.addProperty("name", name)
				obj.add("page", page)
				array.add(obj)
			}
			ack.sendAckData(array.toString())
		} else if (data == "adapters") {
			ack.sendAckData(adapters.toString())
		}

		ack.sendAckData("No data found")
	}

	fun dispose() {

	}
}