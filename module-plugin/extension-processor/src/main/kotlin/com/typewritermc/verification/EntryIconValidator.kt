package com.typewritermc.verification

import com.google.devtools.ksp.KspExperimental
import com.google.devtools.ksp.getAnnotationsByType
import com.google.devtools.ksp.processing.*
import com.google.devtools.ksp.symbol.KSAnnotated
import com.google.devtools.ksp.symbol.KSClassDeclaration
import com.typewritermc.core.extension.annotations.Entry
import com.typewritermc.processors.fullName
import io.ktor.client.*
import io.ktor.client.engine.cio.*
import io.ktor.client.plugins.*
import io.ktor.client.request.*
import io.ktor.client.statement.*
import io.ktor.http.*
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.runBlocking
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import java.io.File
import java.util.concurrent.ConcurrentHashMap
import java.util.concurrent.atomic.AtomicInteger

class EntryIconValidator(
    val logger: KSPLogger,
    val buildDir: File,
) : SymbolProcessor {
    @OptIn(KspExperimental::class)
    override fun process(resolver: Resolver): List<KSAnnotated> {
        val entries = resolver.getSymbolsWithAnnotation(Entry::class.qualifiedName!!)

        val invalidEntries = validateIcons(logger, buildDir) {
            runBlocking {
                entries
                    .map { it to it.getAnnotationsByType(Entry::class).first() }
                    .map { (entry, annotation) ->
                        async {
                            val isValid = annotation.icon.isValidIcon()
                            EntryIconInfo(entry, annotation, isValid)
                        }
                    }
                    .toList()
                    .awaitAll()
                    .filter { !it.isValid }
                    .map {
                        if (it.entry !is KSClassDeclaration) it.annotation.icon
                        else "${it.entry.fullName}: ${it.annotation.icon}"
                    }
            }
        }

        if (invalidEntries.isEmpty()) return emptyList()
        throw InvalidIconException(invalidEntries)
    }
}

fun <T> validateIcons(logger: KSPLogger, buildDir: File, consumer: IconValidator.() -> T): T {
    val validator = IconValidator(logger, buildDir)
    validator.init()
    return try {
        consumer(validator)
    } finally {
        validator.dispose()
    }
}

class IconValidator(
    private val logger: KSPLogger,
    private val buildDir: File,
) {
    companion object {
        private val client: HttpClient = HttpClient(CIO) {
            install(HttpTimeout)
        }

        private val consumers = AtomicInteger(0)
        private val maxCacheTime = 1000 * 60 * 60 * 24 * 7
        private var cachedIcons: MutableMap<String, Long> = ConcurrentHashMap()
    }

    fun init() {
        if (consumers.incrementAndGet() != 1) return
        val cacheFile = File(buildDir, "cache/icons.json")
        cachedIcons = if (cacheFile.exists()) {
            val cache = cacheFile.readText()
            Json.decodeFromString(cache)
        } else mutableMapOf()

        cachedIcons.entries.retainAll { System.currentTimeMillis() - it.value < maxCacheTime }
    }

    suspend fun String.isValidIcon(): Boolean {
        logger.info("Validating $this ${if (cachedIcons.containsKey(this)) "(cached)" else ""}")
        if (cachedIcons.containsKey(this)) return true
        val isValid = this.lookupIconValidity()
        if (isValid) {
            cachedIcons[this] = System.currentTimeMillis()
        }
        return isValid
    }

    private suspend fun String.lookupIconValidity(): Boolean {
        if (isBlank()) return false
        if (!contains(':')) return false
        val set = split(':')
        if (set.size != 2) return false
        val (collection, icon) = set
        val response = try {
            client.get {
                url("https://api.iconify.design/$collection/$icon.svg")
                timeout { socketTimeoutMillis = 1000 }
            }
        } catch (e: Exception) {
            // If we can't connect to the server, we assume the icon is valid
            // So that people can compile without an internet connection
            logger.info("Failed to connect to the Iconify API, assuming the icon is valid: ${e.message}")
            return true
        }
        val contentType = response.contentType() ?: return false
        if (contentType.match(ContentType.Application.Json)) return false

        response.discardRemaining()
        return true
    }

    fun dispose() {
        if (consumers.decrementAndGet() != 0) return
        client.close()
        val cacheFile = File(buildDir, "cache/icons.json")
        cacheFile.parentFile.mkdirs()
        cacheFile.writeText(Json.encodeToString(cachedIcons))
    }
}

data class EntryIconInfo(
    val entry: KSAnnotated,
    val annotation: Entry,
    val isValid: Boolean,
)


class InvalidIconException(names: List<String>) : Exception(
    """
    |Icon must be a valid icon from https://iconify.design/.
    |The following icons are invalid: 
    | - ${names.joinToString("\n - ")}
    | 
""".trimMargin()
)

class EntryIconValidatorProvider : SymbolProcessorProvider {
    override fun create(environment: SymbolProcessorEnvironment): SymbolProcessor {
        val buildDirPath = environment.options["buildDir"] as String
        val buildDir = File(buildDirPath)
        return EntryIconValidator(environment.logger, buildDir)
    }
}