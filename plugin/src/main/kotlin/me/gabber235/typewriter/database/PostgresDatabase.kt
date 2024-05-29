package me.gabber235.typewriter.database

import com.google.gson.JsonObject
import com.zaxxer.hikari.HikariConfig
import com.zaxxer.hikari.HikariDataSource
import me.gabber235.typewriter.entry.Page
import me.gabber235.typewriter.entry.StagingState
import me.gabber235.typewriter.facts.Fact
import me.gabber235.typewriter.logger
import me.gabber235.typewriter.utils.config
import org.koin.core.component.KoinComponent
import java.sql.SQLException
import java.util.*
import java.util.logging.Level

class PostgresDatabase : Database(), KoinComponent {

    private lateinit var dataSource: HikariDataSource
    private val databaseUrl: String by config(
        "storage.sql.url",
        "jdbc:postgresql://<ip>:<port>/<database>",
        comment = "The JDBC URL to use. Remember to replace <ip>, <port>, and <database> with your actual values. MAKE SURE TO CREATE THE DATABASE FIRST!"
    )
    private val databaseUsername: String by config(
        "storage.sql.username",
        "root",
        comment = "The username to use."
    )
    private val databasePassword: String by config(
        "storage.sql.password",
        "password",
        comment = "The password to use."
    )

    override fun initialize() {
        val config = HikariConfig().apply {
            jdbcUrl = databaseUrl
            username = databaseUsername
            password = databasePassword
            maximumPoolSize = 10
            minimumIdle = 10
            maxLifetime = 1800000
            keepaliveTime = 0
            connectionTimeout = 5000
            driverClassName = "org.postgresql.Driver"
        }
        dataSource = HikariDataSource(config)

        try {
            dataSource.connection.use { connection ->
                try {
                    val statements = loadDatabaseSchemaFromFile(DatabaseType.POSTGRES)
                    statements.forEach { statement ->
                        connection.createStatement().execute(statement)
                    }
                } catch (e: SQLException) {
                    logger.log(Level.SEVERE, "Failed to create tables. Make sure you're running a PostgreSQL database and "
                        + "that the user '$databaseUsername' has the necessary permissions.", e)
                }
            }
        } catch (e: Exception) {
            logger.log(Level.SEVERE, "Failed to connect to the database. Please check your configuration.", e)
        }
    }

    override fun shutdown() {
        if (!dataSource.isClosed) {
            dataSource.close()
        }
    }

    override fun getDatabaseType(): DatabaseType {
        return DatabaseType.POSTGRES
    }

    override suspend fun loadFacts(uuid: UUID): Set<Fact> {
        val query = "SELECT facts FROM $TABLE_FACTS_NAME WHERE player_id = ?"
        return dataSource.connection.use { connection ->
            connection.prepareStatement(query).use { statement ->
                statement.setObject(1, uuid)
                statement.executeQuery().use { resultSet ->
                    if (resultSet.next()) {
                        val factsJson = resultSet.getString("facts")
                        val factsArray: Array<Fact> = factsDataGson.fromJson(factsJson, Array<Fact>::class.java)
                        factsArray.toSet()
                    } else {
                        emptySet()
                    }
                }
            }
        }
    }

    override suspend fun storeFacts(uuid: UUID, facts: Set<Fact>) {
        val query = """
            INSERT INTO $TABLE_FACTS_NAME (player_id, facts) VALUES (?, ?::json)
            ON CONFLICT (player_id) DO UPDATE SET facts = EXCLUDED.facts
        """

        val factsJson = factsDataGson.toJson(facts)

        dataSource.connection.use { connection ->
            try {
                connection.prepareStatement(query).use { statement ->
                    statement.setObject(1, uuid)
                    statement.setString(2, factsJson)
                    statement.executeUpdate()
                }
            } catch (e: SQLException) {
                logger.log(Level.SEVERE, "Failed to store facts for player $uuid: ${e.message}", e)
            }
        }
    }

    override fun storeAsset(path: String, content: String) {
        val query = """
            INSERT INTO $TABLE_ASSETS_NAME (path, content) VALUES (?, ?)
            ON CONFLICT (path) DO UPDATE SET content = EXCLUDED.content, is_deleted = FALSE
        """
        dataSource.connection.use { connection ->
            try {
                connection.prepareStatement(query).use { statement ->
                    statement.setString(1, path)
                    statement.setString(2, content)
                    statement.executeUpdate()
                }
            } catch (e: SQLException) {
                logger.log(Level.SEVERE, "Failed to store asset $path: ${e.message}", e)
            }
        }
    }

    override fun fetchAsset(path: String): Result<String> {
        val query = "SELECT content FROM $TABLE_ASSETS_NAME WHERE path = ? AND is_deleted = FALSE"
        return try {
            dataSource.connection.use { connection ->
                connection.prepareStatement(query).use { statement ->
                    statement.setString(1, path)
                    statement.executeQuery().use { resultSet ->
                        if (resultSet.next()) {
                            Result.success(resultSet.getString("content"))
                        } else {
                            Result.failure(IllegalArgumentException("Asset $path not found."))
                        }
                    }
                }
            }
        } catch (e: SQLException) {
            logger.log(Level.SEVERE, "Failed to fetch asset $path: ${e.message}", e)
            Result.failure(e)
        }
    }

    override fun deleteAsset(path: String) {
        val query = "UPDATE $TABLE_ASSETS_NAME SET is_deleted = TRUE WHERE path = ?"
        try {
            dataSource.connection.use { connection ->
                connection.prepareStatement(query).use { statement ->
                    statement.setString(1, path)
                    statement.executeUpdate()
                }
            }
        } catch (e: SQLException) {
            logger.log(Level.SEVERE, "Failed to delete asset $path: ${e.message}", e)
        }
    }

    override fun fetchAllAssetPaths(): Set<String> {
        val query = "SELECT path FROM $TABLE_ASSETS_NAME WHERE is_deleted = FALSE"
        return try {
            dataSource.connection.use { connection ->
                connection.prepareStatement(query).use { statement ->
                    statement.executeQuery().use { resultSet ->
                        mutableSetOf<String>().apply {
                            while (resultSet.next()) {
                                add(resultSet.getString("path"))
                            }
                        }
                    }
                }
            }
        } catch (e: SQLException) {
            logger.log(Level.SEVERE, "Failed to fetch all asset paths: ${e.message}", e)
            emptySet()
        }
    }

    override fun loadStagingPages(pages: MutableMap<String, JsonObject>): StagingState {
        val query = "SELECT path, content, is_staging FROM $TABLE_PAGES_NAME"
        return try {
            var anyStaging = false;

            dataSource.connection.use { connection ->
                connection.prepareStatement(query).use { statement ->
                    statement.executeQuery().use { resultSet ->
                        while (resultSet.next()) {
                            val path = resultSet.getString("path")
                            val content = resultSet.getString("content")
                            pages[path] = bukkitDataGson.fromJson(content, JsonObject::class.java)
                            if (resultSet.getBoolean("is_staging")) {
                                anyStaging = true;
                            }
                        }
                    }
                }
            }

            if (anyStaging) {
                StagingState.STAGING
            } else {
                StagingState.PUBLISHED
            }
        } catch (e: SQLException) {
            logger.log(Level.SEVERE, "Failed to load staging pages: ${e.message}", e)
            StagingState.STAGING
        }
    }

    override fun loadPages(): List<Page> {
        val query = "SELECT path, content FROM $TABLE_PAGES_NAME WHERE is_staging = FALSE"
        return try {
            dataSource.connection.use { connection ->
                connection.prepareStatement(query).use { statement ->
                    statement.executeQuery().use { resultSet ->
                        mutableListOf<Page>().apply {
                            while (resultSet.next()) {
                                val content = resultSet.getString("content")
                                val page = entryParserGson.fromJson(content, Page::class.java)
                                add(page)
                            }
                        }
                    }
                }
            }
        } catch (e: SQLException) {
            logger.log(Level.SEVERE, "Failed to load pages: ${e.message}", e)
            emptyList()
        }
    }

    override fun saveStagingPages(pages: MutableMap<String, JsonObject>) {
        val insertQuery = """
            INSERT INTO $TABLE_PAGES_NAME (path, content, is_staging) VALUES (?, ?::json, TRUE)
            ON CONFLICT (path) DO UPDATE SET content = EXCLUDED.content, is_staging = TRUE
        """
        dataSource.connection.use { connection ->
            try {
                connection.prepareStatement(insertQuery).use { statement ->
                    for ((path, page) in pages) {
                        statement.setString(1, path)
                        statement.setString(2, page.toString())
                        statement.addBatch()
                    }
                    statement.executeBatch()
                }
            } catch (e: SQLException) {
                logger.log(Level.SEVERE, "Failed to save staging pages: ${e.message}", e)
            }
        }
    }

    override suspend fun publishStagingPages(pages: MutableMap<String, JsonObject>): Result<String> {
        val insertQuery = """
            INSERT INTO $TABLE_PAGES_NAME (path, content, is_staging) VALUES (?, ?::json, FALSE)
            ON CONFLICT (path) DO UPDATE SET content = EXCLUDED.content, is_staging = FALSE
        """
        val deleteQuery = "DELETE FROM $TABLE_PAGES_NAME WHERE path = ? AND is_staging = TRUE"
        return try {
            dataSource.connection.use { connection ->
                connection.prepareStatement(insertQuery).use { insertStatement ->
                    for ((path, page) in pages) {
                        insertStatement.setString(1, path)
                        insertStatement.setString(2, page.toString())
                        insertStatement.addBatch()
                    }
                    insertStatement.executeBatch()
                }

                connection.prepareStatement(deleteQuery).use { deleteStatement ->
                    for (path in pages.keys) {
                        deleteStatement.setString(1, path)
                        deleteStatement.addBatch()
                    }
                    deleteStatement.executeBatch()
                }
            }

            Result.success("Successfully published the staging state")
        } catch (e: SQLException) {
            logger.log(Level.SEVERE, "Failed to publish staging pages: ${e.message}", e)
            Result.failure(e)
        }
    }

}