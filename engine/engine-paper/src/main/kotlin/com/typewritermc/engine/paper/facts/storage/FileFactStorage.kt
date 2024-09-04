package com.typewritermc.engine.paper.facts.storage

import com.google.gson.*
import com.google.gson.reflect.TypeToken
import com.google.gson.stream.JsonReader
import com.google.gson.stream.JsonWriter
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.withContext
import com.typewritermc.engine.paper.entry.entries.GroupId
import com.typewritermc.engine.paper.facts.FactData
import com.typewritermc.engine.paper.facts.FactId
import com.typewritermc.engine.paper.facts.FactStorage
import com.typewritermc.engine.paper.logger
import com.typewritermc.engine.paper.plugin
import com.typewritermc.engine.paper.utils.get
import org.koin.core.component.KoinComponent
import java.lang.reflect.Type
import java.time.LocalDateTime
import java.time.ZoneOffset
import java.util.*

private val gson: Gson = GsonBuilder()
    .setPrettyPrinting()
    .registerTypeAdapter(LocalDateTime::class.java, LocalDateTimeSerializer)
    .create()

class FileFactStorage : FactStorage, KoinComponent {
    override fun init() {
        super.init()

        runBlocking {
            migrateFacts(this@FileFactStorage)
        }
    }

    override suspend fun loadFacts(): Map<FactId, FactData> {
        val file = plugin.dataFolder["facts.json"]
        if (!file.exists()) {
            return emptyMap()
        }

        val facts = mutableMapOf<FactId, FactData>()

        file.reader().buffered().use { fileReader ->
            JsonReader(fileReader).use { reader ->
                reader.beginObject()
                while (reader.hasNext()) {
                    val entryId = reader.nextName()
                    reader.beginObject()
                    while (reader.hasNext()) {
                        val audienceId = reader.nextName()
                        val data = gson.fromJson<FactData>(reader, FactData::class.java)
                        val id = FactId(entryId, GroupId(audienceId))
                        facts[id] = data
                    }
                    reader.endObject()
                }
                reader.endObject()
            }
        }

        return facts
    }

    override suspend fun storeFacts(facts: Collection<Pair<FactId, FactData>>) {
        val file = plugin.dataFolder["facts.json"]
        if (!file.exists()) {
            withContext(Dispatchers.IO) {
                file.createNewFile()
            }
        }

        file.writer().buffered().use { fileWriter ->
            JsonWriter(fileWriter).use { writer ->
                writer.beginObject()
                for ((id, data) in facts) {
                    writer.name(id.entryId)
                    writer.beginObject()
                    writer.name(id.groupId.id)
                    gson.toJson(data, FactData::class.java, writer)
                    writer.endObject()
                }
                writer.endObject()
            }
        }
    }
}

// Create a (de)serializer for LocalDateTime class
object LocalDateTimeSerializer : JsonSerializer<LocalDateTime>, JsonDeserializer<LocalDateTime> {
    override fun serialize(src: LocalDateTime, typeOfSrc: Type, context: JsonSerializationContext): JsonElement {
        return JsonPrimitive(src.toInstant(ZoneOffset.UTC).epochSecond)
    }

    override fun deserialize(json: JsonElement, typeOfT: Type, context: JsonDeserializationContext): LocalDateTime {
        return LocalDateTime.ofEpochSecond(json.asLong, 0, ZoneOffset.UTC)
    }
}

private suspend fun migrateFacts(storage: FactStorage) {
    val directory = plugin.dataFolder["players"]
    if (!directory.exists()) {
        return
    }

    logger.info("Migrating facts from old storage")
    val facts = directory.listFiles()?.flatMap { file ->
        val uuid = UUID.fromString(file.nameWithoutExtension)
        val facts = gson.fromJson(file.readText(), object : TypeToken<List<OldFact>>() {})

        facts.map { oldFact ->
            val id = FactId(oldFact.id, GroupId(uuid))
            val data = FactData(oldFact.value, oldFact.lastUpdate)

            id to data
        }
    } ?: emptyList()

    storage.storeFacts(facts)

    if (!plugin.dataFolder["deleted_assets/players"].exists()) {
        plugin.dataFolder["deleted_assets/players"].mkdirs()
    }
    directory.listFiles()?.forEach { it.renameTo(plugin.dataFolder["deleted_assets/players/${it.name}"]) }
    directory.delete()
}

private data class OldFact(val id: String, val value: Int, val lastUpdate: LocalDateTime)
