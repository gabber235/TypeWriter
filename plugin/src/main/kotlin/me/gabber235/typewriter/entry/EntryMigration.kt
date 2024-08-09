package me.gabber235.typewriter.entry

import com.google.gson.Gson
import com.google.gson.JsonObject
import com.google.gson.JsonSyntaxException
import lirand.api.extensions.other.set
import me.gabber235.typewriter.adapters.AdapterLoader
import org.koin.core.qualifier.named
import org.koin.java.KoinJavaComponent.get
import java.lang.reflect.Method
import java.lang.reflect.Modifier
import kotlin.reflect.KClass
import kotlin.reflect.full.findAnnotations

/**
 * Annotate a function to let Typewriter know that it should be called when an entry needs to be migrated.
 *
 * The function must have the signature `(JsonObject, EntryMigratorContext) -> JsonObject`.
 *
 * Example:
 * ```kotlin
 * @EntryMigration(InventoryItemCountFact::class, "0.1.0")
 * private fun migrate010(json: JsonObject, context: EntryMigratorContext): JsonObject {
 *    val data = JsonObject()
 *    data.copyAllBut(json, "test")
 *
 *    val test = json.getAndParse<Optional<String>>("test", context.gson).optional
 *    // ...
 *    return data
 * }
 * ```
 *
 * IMPORTANT: The function must be static
 *
 * Additionally, you can annotate the function with [NeedsMigrationIfContainsAny], [NeedsMigrationIfContainsAll],
 * or [NeedsMigrationIfNotParsable] to prevent the migration of entries that are already parsable by the current version
 * of the entry.
 */
@Target(AnnotationTarget.FUNCTION)
annotation class EntryMigration(val klass: KClass<out Entry>, val version: String)

class EntryMigratorContext(val gson: Gson)

data class EntryMigrator(
    val klass: KClass<out Entry>,
    val version: SemanticVersion,
    val needsMigration: NeedsMigration,
    val migrator: (JsonObject, EntryMigratorContext) -> JsonObject,
) {
    fun needsMigration(json: JsonObject, context: EntryMigratorContext): Boolean {
        return needsMigration.needsMigration(this, json, context)
    }
}

fun JsonObject.copyAllBut(from: JsonObject, vararg properties: String) {
    from.entrySet().filter { it.key !in properties }.forEach { (key, value) ->
        this[key] = value.deepCopy()
    }
}

inline fun <reified T> JsonObject.getAndParse(name: String, gson: Gson): T? {
    val element = this[name] ?: return null
    return gson.fromJson(element, object : com.google.gson.reflect.TypeToken<T>() {}.type)
}

object EntryMigrations {
    fun finaMaximalMigrationVersion(): SemanticVersion? {
        val migrators = get<AdapterLoader>(AdapterLoader::class.java).getEntryMigrators()
        return migrators.maxOfOrNull { it.version }
    }

    fun findMinimalNeededMigrationVersion(currentVersion: SemanticVersion): SemanticVersion? {
        val migrators = get<AdapterLoader>(AdapterLoader::class.java).getEntryMigrators()
        return migrators.map { it.version }.filter { it > currentVersion }.minOrNull()
    }

    fun findEntryMigrators(targetVersion: SemanticVersion): List<Pair<String, EntryMigrator>> {
        val migrators = get<AdapterLoader>(AdapterLoader::class.java).getEntryMigrators()
        return migrators.filter { it.version == targetVersion }.map { entryName(it.klass) to it }
    }

    private fun entryName(klass: KClass<out Entry>): String {
        val entry = klass.findAnnotations(me.gabber235.typewriter.adapters.Entry::class).firstOrNull()
            ?: throw IllegalStateException("Entry ${klass.simpleName} does not have an @Entry annotation.")
        return entry.name
    }

    fun JsonObject.migrateEntriesForPage(migrators: List<Pair<String, EntryMigrator>>) {
        val gson = get<Gson>(Gson::class.java, named("entryParser"))
        val entries = getAsJsonArray("entries") ?: return
        val newEntries = entries.map { entry ->
            val entryObject = entry.asJsonObject
            val type = entryObject["type"]?.asString ?: return@map entry
            val entryMigrators = migrators.filter { it.first == type }.map { it.second }
            if (entryMigrators.isEmpty()) {
                return@map entry
            }

            val context = EntryMigratorContext(gson)

            entryMigrators.filter { it.needsMigration(entryObject, context) }.fold(entryObject) { acc, migrator ->
                migrator.migrator(acc, context)
            }
        }

        this["entries"] = newEntries.json
    }

    private fun constructEntryMigrator(method: Method): EntryMigrator {
        val annotation = method.getAnnotation(EntryMigration::class.java)

        if (method.parameterCount != 2) {
            throw EntryMigratorException(method, "has ${method.parameterCount} parameters.")
        }
        if (method.parameterTypes[0] != JsonObject::class.java) {
            throw EntryMigratorException(method, "the first parameter is not JsonObject.")
        }
        if (method.parameterTypes[1] != EntryMigratorContext::class.java) {
            throw EntryMigratorException(method, "the second parameter is not EntryMigratorContext.")
        }
        if (method.returnType != JsonObject::class.java) {
            throw EntryMigratorException(method, "the return type is not JsonObject.")
        }
        val needsMigration = NeedsMigration.fromMethod(method)

        return EntryMigrator(annotation.klass, annotation.version.v, needsMigration) { json, context ->
            method.isAccessible = true
            method.invoke(null, json, context) as JsonObject
        }
    }

    fun constructEntryMigrators(classes: List<Class<*>>): List<EntryMigrator> {
        return classes.asSequence()
            .flatMap { it.methods.asSequence() }
            .filter { Modifier.isStatic(it.modifiers) }
            .filter { it.isAnnotationPresent(EntryMigration::class.java) }
            .map(::constructEntryMigrator)
            .toList()

    }
}

/**
 * Indicates that an entry needs to be migrated if any of the specified properties contain any value.
 *
 * Use this to prevent the migration of entries that are already parsable by the current version of the entry.
 *
 * @param properties The list of properties to check for values. If any of these properties contain a value,
 *      the annotated function needs to be migrated.
 */
@Target(AnnotationTarget.FUNCTION)
annotation class NeedsMigrationIfContainsAny(val properties: Array<String>)

/**
 * Indicates that an entry needs to be migrated if all the specified properties contain any value.
 *
 * Use this to prevent the migration of entries that are already parsable by the current version of the entry.
 *
 * @param properties The list of properties to check for values. If all of these properties contain a value,
 */
@Target(AnnotationTarget.FUNCTION)
annotation class NeedsMigrationIfContainsAll(val properties: Array<String>)

/**
 * Indicates that an entry needs to be migrated if the object is not parsable by the current version of the entry.
 *
 * Use this to prevent the migration of entries that are already parsable by the current version of the entry.
 */
@Target(AnnotationTarget.FUNCTION)
annotation class NeedsMigrationIfNotParsable

sealed interface NeedsMigration {
    fun needsMigration(migrator: EntryMigrator, json: JsonObject, context: EntryMigratorContext): Boolean

    data object Always : NeedsMigration {
        override fun needsMigration(migrator: EntryMigrator, json: JsonObject, context: EntryMigratorContext): Boolean =
            true
    }

    class ContainsAny(private val properties: Array<String>) : NeedsMigration {
        override fun needsMigration(migrator: EntryMigrator, json: JsonObject, context: EntryMigratorContext): Boolean {
            return properties.any { json.has(it) }
        }
    }

    class ContainsAll(private val properties: Array<String>) : NeedsMigration {
        override fun needsMigration(migrator: EntryMigrator, json: JsonObject, context: EntryMigratorContext): Boolean {
            return properties.all { json.has(it) }
        }
    }

    data object NotParsable : NeedsMigration {
        override fun needsMigration(migrator: EntryMigrator, json: JsonObject, context: EntryMigratorContext): Boolean {
            return try {
                context.gson.fromJson(json, migrator.klass.java)
                false
            } catch (e: JsonSyntaxException) {
                true
            }
        }
    }

    companion object {
        fun fromMethod(method: Method): NeedsMigration {
            val any = method.getAnnotation(NeedsMigrationIfContainsAny::class.java)
            val all = method.getAnnotation(NeedsMigrationIfContainsAll::class.java)
            val notParsable = method.getAnnotation(NeedsMigrationIfNotParsable::class.java)

            return when {
                any != null -> ContainsAny(any.properties)
                all != null -> ContainsAll(all.properties)
                notParsable != null -> NotParsable
                else -> Always
            }
        }
    }
}

class EntryMigratorException(method: Method, comment: String) :
    Exception("Entry migration method ${method.name} should have signature (JsonObject, EntryMigratorContext) -> JsonObject, but $comment.")
