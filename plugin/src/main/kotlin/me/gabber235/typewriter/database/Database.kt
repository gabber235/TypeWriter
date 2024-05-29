package me.gabber235.typewriter.database

import com.google.gson.*
import me.gabber235.typewriter.entry.Page
import me.gabber235.typewriter.entry.StagingState
import me.gabber235.typewriter.facts.Fact
import me.gabber235.typewriter.plugin
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject
import org.koin.core.qualifier.named
import java.io.InputStream
import java.lang.reflect.Type
import java.time.LocalDateTime
import java.time.ZoneOffset
import java.util.*

/**
 * Represents a database where the plugin stores its data.
 * The database can be of different types, such as MySQL, MariaDB, PostgreSQL, or a file-based database.
 */
abstract class Database : KoinComponent {

    protected val bukkitDataGson: Gson by inject(named("bukkitDataParser"))
    protected val entryParserGson: Gson by inject(named("entryParser"))
    protected val factsDataGson: Gson = GsonBuilder()
        .setPrettyPrinting()
        .registerTypeAdapter(LocalDateTime::class.java, LocalDateTimeSerializer)
        .create()

    /**
     * Initializes the database.
     */
    abstract fun initialize()

    /**
     * Shuts down the database.
     */
    abstract fun shutdown()

    /**
     * Returns the type of the database.
     */
    abstract fun getDatabaseType(): DatabaseType

    /**
     * Load facts asynchronously for the given player [uuid].
     */
    abstract suspend fun loadFacts(uuid: UUID): Set<Fact>

    /**
     * Save the given [facts] for the given player [uuid].
     */
    abstract suspend fun storeFacts(uuid: UUID, facts: Set<Fact>)

    /**
     * Store the given [path] with the given [content].
     */
    abstract fun storeAsset(path: String, content: String)

    /**
     * Fetch the asset with the given [path].
     */
    abstract fun fetchAsset(path: String): Result<String>

    /**
     * Delete the asset with the given [path].
     */
    abstract fun deleteAsset(path: String)

    /**
     * Fetch all asset paths.
     */
    abstract fun fetchAllAssetPaths(): Set<String>

    /**
     * Load pages state from the database and return the staging state.
     */
    abstract fun loadPagesState(pages: MutableMap<String, JsonObject>): StagingState

    /**
     * Load all pages from the database and return them.
     */
    abstract fun loadPages(): List<Page>

    /**
     * Save the staging pages.
     */
    abstract fun saveStagingPages(pages: MutableMap<String, JsonObject>)

    /**
     * Publish the staging pages.
     */
    abstract suspend fun publishStagingPages(pages: MutableMap<String, JsonObject>): Result<String>
}

const val TABLE_FACTS_NAME = "typewriter_facts"
const val TABLE_ASSETS_NAME = "typewriter_assets"
const val TABLE_PAGES_NAME = "typewriter_pages"

fun loadDatabaseSchemaFromFile(databaseType: DatabaseType): List<String> {
    val fileName = when (databaseType) {
        DatabaseType.MYSQL, DatabaseType.MARIADB -> "mysql_schema.sql"
        DatabaseType.POSTGRES -> "postgresql_schema.sql"
        else -> throw IllegalArgumentException("Unsupported database type: $databaseType")
    }
    val fileInputStream: InputStream = plugin.getResource("database/$fileName") ?: throw IllegalStateException("Schema file not found: $fileName")
    return fileInputStream.bufferedReader().use { it.readText() }.split(";").map { it.trim() }.filter { it.isNotBlank() }
}

enum class DatabaseType(val displayName: String, val protocol: String) {
    MYSQL("MySQL", "mysql"),
    MARIADB("MariaDB", "mariadb"),
    POSTGRES("PostgreSQL", "postgresql"),
    FILE("File", "file");
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