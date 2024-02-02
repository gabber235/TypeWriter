package me.gabber235.typewriter.entry

import com.google.gson.Gson
import com.google.gson.JsonElement
import com.google.gson.JsonObject
import com.google.gson.JsonPrimitive
import lirand.api.extensions.other.set
import me.gabber235.typewriter.entry.EntryMigrations.migrateEntriesForPage
import me.gabber235.typewriter.logger
import me.gabber235.typewriter.plugin
import me.gabber235.typewriter.utils.get
import java.io.File
import java.text.SimpleDateFormat
import java.util.*


data class PossibleMigration(
    val file: File,
    val version: SemanticVersion,
)


fun File.pages(): Array<File> = listFiles { _, name -> name.endsWith(".json") } ?: emptyArray()

fun File.migrateIfNecessary(run: Int = 0) {
    val highestMigratorVersion = EntryMigrations.finaMaximalMigrationVersion() ?: return
    val migratable = pages().mapNotNull { file ->
        if (!file.exists()) {
            return@mapNotNull null
        }

        val content = file.readText()
        val json = Gson().fromJson(content, JsonObject::class.java)
        val pageVersion = json.getAsJsonPrimitive("version")?.asString ?: "0.0.0"
        val semanticVersion = SemanticVersion.fromString(pageVersion)

        if (semanticVersion >= highestMigratorVersion) {
            return@mapNotNull null
        }

        return@mapNotNull PossibleMigration(file, semanticVersion)
    }

    if (migratable.isEmpty()) {
        return
    }

    if (run == 0) {
        logger.info(
            """
			|
			| -------------- MIGRATION --------------
			| Starting migration of ${migratable.size} files.
			| 
			| This may take a while, please wait.
			| 
			| If the migration fails, please report this to the developer. 
			| Your files will be backed up to the folder "plugins/Typewriter/backup".
			| -------------- MIGRATION --------------
		""".trimMargin()
        )

        migratable.map { it.file }.backup()
    }

    val lowestPageVersion = migratable.minBy { it.version }.version
    val lowestMigratorVersion = EntryMigrations.findMinimalNeededMigrationVersion(lowestPageVersion)
        ?: throw IllegalStateException("Could not find a migration for version $lowestPageVersion")

    val migrators = EntryMigrations.findEntryMigrators(lowestMigratorVersion)

    val migrating = migratable.filter { it.version == lowestPageVersion }.map { it.file }

    migrating.forEach { file ->
        val content = file.readText()
        val json = Gson().fromJson(content, JsonObject::class.java)
        json.migrateEntriesForPage(migrators)
        json["version"] = lowestMigratorVersion.toString().json
        file.writeText(Gson().toJson(json))
    }


    /// Recursively call this function until all files are migrated
    migrateIfNecessary(run + 1)
}

fun List<File>.backup() {
    if (isEmpty()) {
        // Nothing to back up
        return
    }
    val date = Date()
    val dateFormat = SimpleDateFormat("yyyy-MM-dd_HH-mm-ss")

    val backupFolder = plugin.dataFolder["backup/${dateFormat.format(date)}"]
    backupFolder.mkdirs()
    forEach {
        val file = it
        val backupFile = File(backupFolder, file.name)
        file.copyTo(backupFile, overwrite = true)
    }
}

internal val String.json get() = JsonPrimitive(this)
internal val List<JsonElement>.json get() = Gson().toJsonTree(this)

internal val String.v get() = SemanticVersion.fromString(this)

data class SemanticVersion(
    val major: Int,
    val minor: Int,
    val patch: Int,
    val type: VersionType = VersionType.RELEASE,
    val buildNumber: Int = 0,
) : Comparable<SemanticVersion> {
    companion object {
        fun fromString(version: String): SemanticVersion {
            val versionParts = version.split("-")
            val versionPart = versionParts[0]
            val parts = versionPart.split(".")
            if (parts.size != 3) {
                throw IllegalArgumentException("Invalid version format: $version")
            }

            val (major, minor, patch) = parts.map { it.toInt() }

            if (versionParts.size == 1) {
                return SemanticVersion(major, minor, patch)
            }

            val type = when (versionParts[1]) {
                "dev" -> VersionType.DEVELOPMENT
                else -> VersionType.RELEASE
            }

            val buildNumber = if (versionParts.size == 3) {
                versionParts[2].toInt()
            } else {
                0
            }

            return SemanticVersion(major, minor, patch, type, buildNumber)
        }
    }

    override operator fun compareTo(other: SemanticVersion): Int {
        if (major != other.major) {
            return major - other.major
        }
        if (minor != other.minor) {
            return minor - other.minor
        }
        if (patch != other.patch) {
            return patch - other.patch
        }

        if (type != other.type) {
            return when (type) {
                VersionType.RELEASE -> 1
                VersionType.DEVELOPMENT -> -1
            }
        }

        return buildNumber - other.buildNumber
    }

    override fun toString(): String {
        if (type == VersionType.RELEASE) {
            return "$major.$minor.$patch"
        }

        return "$major.$minor.$patch-${type.suffix}-$buildNumber"
    }
}

enum class VersionType(val suffix: String) {
    RELEASE(""),
    DEVELOPMENT("dev"),
}