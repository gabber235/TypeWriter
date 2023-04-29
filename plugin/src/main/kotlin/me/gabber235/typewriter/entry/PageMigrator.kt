package me.gabber235.typewriter.entry

import com.google.gson.*
import lirand.api.extensions.other.set
import me.gabber235.typewriter.Typewriter.Companion.plugin
import me.gabber235.typewriter.adapters.AdapterLoader
import me.gabber235.typewriter.utils.get
import me.gabber235.typewriter.utils.logErrorIfNull
import java.io.File
import java.text.SimpleDateFormat
import java.util.*
import kotlin.reflect.full.isSuperclassOf

fun File.pages(): Array<File> = listFiles { _, name -> name.endsWith(".json") } ?: emptyArray()

fun File.migrateIfNecessary(run: Int = 0) {
	val highestMigratorVersion = migrators.keys.maxOrNull() ?: return
	val migratable = pages().mapNotNull { file ->
		if (!file.exists()) {
			return@mapNotNull null
		}

		val content = file.readText()
		val json = Gson().fromJson(content, JsonObject::class.java)
		val version = json.getAsJsonPrimitive("version")?.asString ?: "0.0.0"
		val semanticVersion = SemanticVersion.fromString(version)

		if (semanticVersion >= highestMigratorVersion) {
			return@mapNotNull null
		}

		return@mapNotNull file to semanticVersion
	}

	if (migratable.isEmpty()) {
		return
	}

	if (run == 0) {
		plugin.logger.info(
			"""
			|
			| -------------- MIGRATION --------------
			| Starting migration of ${migratable.size} files.
			| 
			| This may take a while, please wait.
			| 
			| If the migration fails, please report this to the developer. 
			| Your files will be backed up to the folder "backup".
			| -------------- MIGRATION --------------
		""".trimMargin()
		)

		migratable.map { it.first }.backup()
	}

	val lowestVersion = migratable.minBy { it.second }.second

	val migrating = migratable.filter { it.second == lowestVersion }

	val migrator = migrators.filter { it.key > lowestVersion }.minByOrNull { it.key }?.value
		?: throw IllegalStateException("No migrator found for version $lowestVersion")

	migrating.forEach { (file, _) ->
		file.migrator()
	}

	/// Recursively call this function until all files are migrated
	migrateIfNecessary(run + 1)
}

fun List<File>.backup() {
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

private val migrators by lazy {
	mutableMapOf<SemanticVersion, File.() -> Unit>(
		"0.3.0".v to File::migrate0_3_0,
	)
}

private fun File.migrate0_3_0() {
	val content = readText()
	val json = Gson().fromJson(content, JsonObject::class.java)
	val name = json.getAsJsonPrimitive("name")?.asString ?: "unnamed_${UUID.randomUUID().toString().substring(0, 5)}"

	json["type"] = "sequence".json
	json["version"] = "0.3.0".json

	val entries = json.getAsJsonArray("entries")

	val staticEntries = entries.mapNotNull { entry ->
		val entryJson = entry.asJsonObject
		val needsExtracting = entryJson.needsStaticExtractingForV0_3_0()
		if (needsExtracting) entryJson
		else null
	}

	if (staticEntries.isEmpty()) {
		writeText(json.toString())
		return
	}


	// Extract static entries to their own page
	val newStaticPage = JsonObject().apply {
		addProperty("name", "${name}_static")
		addProperty("type", "static")
		addProperty("version", "0.3.0")
		add("entries", Gson().toJsonTree(staticEntries))
	}

	// Remove static entries from the original page
	staticEntries.forEach { entries.remove(it) }

	writeText(json.toString())

	// Write the newly created static page
	val staticPageFile = parentFile["${name}_static.json"]
	staticPageFile.writeText(newStaticPage.toString())
}

private fun JsonObject.needsStaticExtractingForV0_3_0(): Boolean {
	val type = getAsJsonPrimitive("type")?.asString ?: return false

	val blueprint = AdapterLoader.getEntryBlueprint(type).logErrorIfNull(
		"""
		| ------ ERROR IN MIGRATION ------
		|Failed to get blueprint for type $type while running the migration. 
		|
		|Make sure you install all the needed adapters to properly migrate your pages!
		| ---------------------------------
	""".trimMargin()
	) ?: return false

	return StaticEntry::class.isSuperclassOf(blueprint.clazz.kotlin)
}

private val String.json get() = JsonPrimitive(this)

private val String.v get() = SemanticVersion.fromString(this)

data class SemanticVersion(
	val major: Int,
	val minor: Int,
	val patch: Int
) : Comparable<SemanticVersion> {
	companion object {
		fun fromString(version: String): SemanticVersion {
			val parts = version.split(".")
			return SemanticVersion(parts[0].toInt(), parts[1].toInt(), parts[2].toInt())
		}
	}

	override operator fun compareTo(other: SemanticVersion): Int {
		if (major != other.major) {
			return major - other.major
		}
		if (minor != other.minor) {
			return minor - other.minor
		}
		return patch - other.patch
	}
}