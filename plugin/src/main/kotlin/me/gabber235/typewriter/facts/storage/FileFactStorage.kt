package me.gabber235.typewriter.facts.storage

import com.google.gson.*
import me.gabber235.typewriter.facts.Fact
import me.gabber235.typewriter.facts.FactStorage
import me.gabber235.typewriter.plugin
import me.gabber235.typewriter.utils.get
import org.koin.core.component.KoinComponent
import java.io.File
import java.lang.reflect.Type
import java.time.LocalDateTime
import java.time.ZoneOffset
import java.util.*

private val gson: Gson = GsonBuilder()
    .setPrettyPrinting()
    .registerTypeAdapter(LocalDateTime::class.java, LocalDateTimeSerializer)
    .create()

class FileFactStorage : FactStorage, KoinComponent {
    private lateinit var directory: File

    override fun init() {
        super.init()
        directory = plugin.dataFolder["players"]
        if (!directory.exists()) {
            directory.mkdirs()
        }
    }

    override suspend fun loadFacts(uuid: UUID): Set<Fact> {
        val file = directory["$uuid.json"]
        if (!file.exists()) {
            return emptySet()
        }
        return gson.fromJson(file.readText(), Array<Fact>::class.java).toSet()
    }

    override suspend fun storeFacts(uuid: UUID, facts: Set<Fact>) {
        val file = directory["$uuid.json"]
        file.writeText(gson.toJson(facts))
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
