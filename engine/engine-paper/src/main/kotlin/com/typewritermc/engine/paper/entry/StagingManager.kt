package com.typewritermc.engine.paper.entry

import com.google.gson.*
import com.typewritermc.core.entries.Entry
import com.typewritermc.core.entries.Ref
import com.typewritermc.core.entries.pageId
import com.typewritermc.core.utils.failure
import com.typewritermc.core.utils.ok
import com.typewritermc.core.utils.onFail
import com.typewritermc.engine.paper.entry.StagingState.*
import com.typewritermc.engine.paper.events.StagingChangeEvent
import com.typewritermc.engine.paper.logger
import com.typewritermc.engine.paper.plugin
import com.typewritermc.engine.paper.ui.ClientSynchronizer
import com.typewritermc.engine.paper.utils.ThreadType.DISPATCHERS_ASYNC
import com.typewritermc.engine.paper.utils.Timeout
import com.typewritermc.engine.paper.utils.get
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject
import org.koin.core.qualifier.named
import org.koin.java.KoinJavaComponent
import java.io.File
import java.lang.reflect.Type
import java.text.SimpleDateFormat
import java.util.*
import java.util.concurrent.ConcurrentHashMap
import kotlin.collections.component1
import kotlin.collections.component2
import kotlin.collections.set
import kotlin.time.Duration.Companion.seconds

interface StagingManager {
    val stagingState: StagingState

    fun loadState()
    fun unload()
    fun fetchPages(): Map<String, JsonObject>
    fun createPage(data: JsonObject): Result<String>
    fun renamePage(pageId: String, newName: String): Result<String>
    fun changePageValue(pageId: String, path: String, value: JsonElement): Result<String>
    fun deletePage(pageId: String): Result<String>
    fun moveEntry(entryId: String, fromPageId: String, toPageId: String): Result<String>
    fun createEntry(pageId: String, data: JsonObject): Result<String>
    fun updateEntryField(pageId: String, entryId: String, path: String, value: JsonElement): Result<String>
    fun updateEntry(pageId: String, data: JsonObject): Result<String>
    fun reorderEntry(pageId: String, entryId: String, newIndex: Int): Result<String>
    fun deleteEntry(pageId: String, entryId: String): Result<String>

    fun findEntryPage(entryId: String): Result<String>
    suspend fun publish(): Result<String>
    fun shutdown()
}

class StagingManagerImpl : StagingManager, KoinComponent {
    private val gson: Gson by inject(named("bukkitDataParser"))

    private val pages = ConcurrentHashMap<String, JsonObject>()

    private val autoSaver =
        Timeout(
            3.seconds,
            ::saveStaging,
            immediateRunnable = {
                // When called to save, we immediately want to update the staging state
                if (stagingState != PUBLISHING) stagingState = STAGING
            })

    private val stagingDir
        get() = plugin.dataFolder["staging"]

    private val publishedDir
        get() = plugin.dataFolder["pages"]

    override var stagingState = PUBLISHED
        private set(value) {
            field = value
            StagingChangeEvent(value).callEvent()
        }

    override fun loadState() {
        stagingState = if (stagingDir.exists()) STAGING else PUBLISHED

        // Read the pages from the file
        val dir = if (stagingState == STAGING) stagingDir else publishedDir
        pages.clear()
        pages.putAll(fetchPages(dir))
    }

    private fun fetchPages(dir: File): Map<String, JsonObject> {
        val pages = mutableMapOf<String, JsonObject>()
        dir.listFiles()?.filter { it.extension == "json" }?.forEach { file ->
            val page = file.readText()
            val pageId = file.nameWithoutExtension
            val pageJson = gson.fromJson(page, JsonObject::class.java)
            // Always force the id to be the same as the file name
            pageJson.addProperty("id", pageId)
            pages[pageId] = pageJson
        }
        return pages
    }

    override fun fetchPages(): Map<String, JsonObject> {
        return pages
    }

    override fun unload() {
        autoSaver.force()
    }

    override fun createPage(data: JsonObject): Result<String> {
        if (!data.has("id")) return failure("Id is required")
        if (!data.has("name")) return failure("Name is required")
        val idJson = data["id"]
        if (!idJson.isJsonPrimitive || !idJson.asJsonPrimitive.isString) return failure("Id must be a string")
        val id = idJson.asString

        if (pages.containsKey(id)) return failure("Page with that id already exists")

        // Add the version of this page to track migrations
        data.addProperty("version", plugin.pluginMeta.version)

        pages[id] = data
        autoSaver()
        return ok("Successfully created page with name $id")
    }

    override fun renamePage(pageId: String, newName: String): Result<String> {
        val page = getPage(pageId) onFail { return it }

        page.addProperty("name", newName)

        autoSaver()
        return ok("Successfully renamed page from $pageId to $newName")
    }

    override fun changePageValue(pageId: String, path: String, value: JsonElement): Result<String> {
        val page = getPage(pageId) onFail { return it }

        page.changePathValue(path, value)

        autoSaver()
        return ok("Successfully updated field")
    }

    override fun deletePage(pageId: String): Result<String> {
        pages.remove(pageId) ?: return failure("Page does not exist")

        autoSaver()
        return ok("Successfully deleted page with name $pageId")
    }

    override fun moveEntry(entryId: String, fromPageId: String, toPageId: String): Result<String> {
        val from = pages[fromPageId] ?: return failure("Page '$fromPageId' does not exist")
        val to = pages[toPageId] ?: return failure("Page '$toPageId' does not exist")

        val entry = from["entries"].asJsonArray.find { it.asJsonObject["id"].asString == entryId }
            ?: return failure("Entry does not exist in page '$fromPageId'")

        from["entries"].asJsonArray.remove(entry)
        to["entries"].asJsonArray.add(entry)

        autoSaver()
        return ok("Successfully moved entry")
    }

    override fun createEntry(pageId: String, data: JsonObject): Result<String> {
        val page = getPage(pageId) onFail { return it }
        val entries = page["entries"] as? JsonArray ?: JsonArray()

        entries.add(data)
        page.add("entries", entries)

        autoSaver()
        return ok("Successfully created entry with id ${data["id"]}")
    }

    override fun updateEntryField(
        pageId: String,
        entryId: String,
        path: String,
        value: JsonElement
    ): Result<String> {
        // Update the page
        val page = getPage(pageId) onFail { return it }
        val entries = page["entries"].asJsonArray
        val entry = entries.find { it.asJsonObject["id"].asString == entryId } ?: return failure("Entry does not exist")

        // Update the entry
        entry.changePathValue(path, value)

        autoSaver()
        return ok("Successfully updated field")
    }

    override fun updateEntry(pageId: String, data: JsonObject): Result<String> {
        val page = getPage(pageId) onFail { return it }
        val entries = page["entries"] as? JsonArray ?: return failure("Page does not have any entries")
        val entryId = data["id"]?.asString ?: return failure("Entry does not have an id")

        entries.removeAll { entry -> entry.asJsonObject["id"]?.asString == entryId }
        entries.add(data)

        autoSaver()
        return ok("Successfully updated entry with id ${data["id"]}")
    }

    override fun reorderEntry(pageId: String, entryId: String, newIndex: Int): Result<String> {
        val page = getPage(pageId) onFail { return it }
        val entries = page["entries"].asJsonArray
        val oldIndex = entries.indexOfFirst { it.asJsonObject["id"].asString == entryId }

        if (oldIndex == -1) return failure("Entry does not exist")
        if (oldIndex == newIndex) return ok("Entry is already at the correct index")

        val correctIndex = if (oldIndex < newIndex) newIndex - 1 else newIndex

        val entryAtNewIndex = entries[correctIndex]
        entries[correctIndex] = entries[oldIndex]
        entries[oldIndex] = entryAtNewIndex

        autoSaver()
        return ok("Successfully reordered entry")
    }

    override fun deleteEntry(pageId: String, entryId: String): Result<String> {
        val page = getPage(pageId) onFail { return it }
        val entries = page["entries"].asJsonArray
        val entry = entries.find { it.asJsonObject["id"].asString == entryId } ?: return failure("Entry does not exist")

        entries.remove(entry)

        autoSaver()
        return ok("Successfully deleted entry with id $entryId")
    }

    override fun findEntryPage(entryId: String): Result<String> {
        val page = pages.values.find { page ->
            page["entries"].asJsonArray.any { entry -> entry.asJsonObject["id"].asString == entryId }
        } ?: return failure("Entry does not exist")

        return ok(page["name"].asString)
    }

    private fun getPage(id: String): Result<JsonObject> {
        val page = pages[id] ?: return failure("Page '$id' does not exist")
        return ok(page)
    }

    private fun saveStaging() {
        // If we are already publishing, we don't want to save the staging
        if (stagingState == PUBLISHING) return
        val dir = stagingDir

        pages.forEach { (id, page) ->
            val file = dir["$id.json"]
            if (!file.exists()) {
                file.parentFile.mkdirs()
                file.createNewFile()
            }
            file.writeText(page.toString())
        }

        dir.listFiles()?.filter { it.nameWithoutExtension !in pages.keys }?.forEach { it.delete() }

        stagingState = STAGING
    }

    // Save the page to the file
    override suspend fun publish(): Result<String> {
        if (stagingState != STAGING) return failure("Can only publish when in staging")
        autoSaver.cancel()
        return DISPATCHERS_ASYNC.switchContext {
            stagingState = PUBLISHING

            if (!publishedDir.exists()) publishedDir.mkdirs()

            try {
                pages.forEach { (name, page) ->
                    val file = publishedDir["$name.json"]
                    file.createNewFile()
                    file.writeText(page.toString())
                }

                val stagingPages = pages.keys
                val publishedFiles = publishedDir.listFiles()?.toList() ?: emptyList()

                val deletedPages = publishedFiles.filter { it.nameWithoutExtension !in stagingPages }
                if (deletedPages.isNotEmpty()) {
                    logger.info(
                        "Deleting ${deletedPages.size} pages, as they are no longer in staging. (${
                            deletedPages.joinToString(
                                ", "
                            ) { it.nameWithoutExtension }
                        })"
                    )
                    deletedPages.backup()
                    deletedPages.forEach { it.delete() }
                }

                // Delete the staging folder
                stagingDir.deleteRecursively()
                plugin.reload()
                stagingState = PUBLISHED
                ok("Successfully published the staging state")
            } catch (e: Exception) {
                e.printStackTrace()
                stagingState = STAGING
                failure("Failed to publish the staging state")
            }
        }
    }

    override fun shutdown() {
        if (stagingState == STAGING) saveStaging()
    }
}

fun JsonElement.changePathValue(path: String, value: JsonElement) {
    val pathParts = path.split(".")
    var current: JsonElement = this
    pathParts.forEachIndexed { index, key ->
        if (index == pathParts.size - 1) {
            if (current.isJsonObject) {
                current.asJsonObject.add(key, value)
            } else if (current.isJsonArray) {
                val i = Integer.parseInt(key)
                while (current.asJsonArray.size() <= i) {
                    current.asJsonArray.add(JsonNull.INSTANCE)
                }
                current.asJsonArray[i] = value
            }
        } else if (current.isJsonObject) {
            if (!current.asJsonObject.has(key)) {
                current.asJsonObject.add(key, JsonObject())
            }
            current = current.asJsonObject[key]
        } else if (current.isJsonArray) {
            val i = Integer.parseInt(key)
            while (current.asJsonArray.size() <= i) {
                current.asJsonArray.add(JsonObject())
            }
            current = current.asJsonArray[i]
        }
    }
}

enum class StagingState {
    PUBLISHING,
    STAGING,
    PUBLISHED
}

inline fun <reified T : Any> Ref<out Entry>.fieldValue(path: String, value: T) = fieldValue(path, value, T::class.java)
fun Ref<out Entry>.fieldValue(path: String, value: Any, type: Type) {
    val stagingManager = KoinJavaComponent.get<StagingManager>(StagingManager::class.java)
    val gson = KoinJavaComponent.get<Gson>(Gson::class.java, named("dataSerializer"))

    val pageId = pageId
    if (pageId == null) {
        logger.warning("No pageId found for $this. Did you forgot to publish?")
        return
    }

    val json = gson.toJsonTree(value, type)
    val result = stagingManager.updateEntryField(pageId, id, path, json)
    if (result.isFailure) {
        logger.warning("Failed to update field: ${result.exceptionOrNull()}")
        return
    }

    val clientSynchronizer = KoinJavaComponent.get<ClientSynchronizer>(ClientSynchronizer::class.java)
    clientSynchronizer.sendEntryFieldUpdate(pageId, id, path, json)
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