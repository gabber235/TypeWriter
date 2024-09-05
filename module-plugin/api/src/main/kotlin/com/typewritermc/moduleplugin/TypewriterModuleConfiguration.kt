package com.typewritermc.moduleplugin

import com.typewritermc.loader.ExtensionFlag
import kotlinx.serialization.Serializable

@Serializable
open class TypewriterModuleConfiguration {
    /**
     * The namespace of the module.
     * This must be tied to your organization.
     */
    var namespace: String = ""

    val flags: MutableList<ExtensionFlag> = mutableListOf()

    var engine: TypewriterEngineConfiguration? = null
    var extension: TypewriterExtensionConfiguration? = null

    /**
     * Adds a flag to the module.
     */
    fun flag(flag: ExtensionFlag) {
        flags.add(flag)
    }

    fun engine(block: TypewriterEngineConfiguration.() -> Unit) {
        engine = TypewriterEngineConfiguration().apply(block)
    }

    fun extension(block: TypewriterExtensionConfiguration.() -> Unit) {
        extension = TypewriterExtensionConfiguration().apply(block)
    }
}

const val MIN_NAMESPACE_LENGTH = 5
const val MAX_NAMESPACE_LENGTH = 20
fun TypewriterModuleConfiguration.validate() {
    if (namespace.isBlank()) {
        throw MissingFieldException("namespace")
    }
    if (!Regex("^([a-z0-9])+\$").matches(namespace)) {
        throw IllegalArgumentException("Namespace must only contain lowercase letters and numbers.")
    }
    if (namespace.length !in MIN_NAMESPACE_LENGTH..MAX_NAMESPACE_LENGTH) {
        throw FieldOutsideRangeException("namespace", MIN_NAMESPACE_LENGTH, MAX_NAMESPACE_LENGTH)
    }
    engine?.validate()
    extension?.validate()
}

@Serializable
data class TypewriterEngineConfiguration(
    /**
     * Version of the engine.
     * It follows the [Semantic Versioning](https://semver.org/) format.
     */
    var version: String = "",
    /**
     * Release channel of the engine.
     * This is used to determine what repository to download the engine from.
     */
    var channel: ReleaseChannel = ReleaseChannel.RELEASE,
)

enum class ReleaseChannel(val url: String? = null) {
    RELEASE("https://maven.typewritermc.com/releases"),
    BETA("https://maven.typewritermc.com/beta"),
    NONE
}

private fun TypewriterEngineConfiguration.validate() {
    if (version.isBlank()) {
        throw MissingFieldException("engine.version")
    }
    if (!Regex("^([0-9])+\\.([0-9])+\\.([0-9])+\$").matches(version)) {
        throw IllegalArgumentException("Version must follow the Semantic Versioning format.")
    }
}

@Serializable
data class TypewriterExtensionConfiguration(
    /**
     * The name of the extension.
     */
    var name: String = "",
    /**
     * A short description of the extension.
     */
    var shortDescription: String = "",
    /**
     * A longer description of the extension.
     */
    var description: String = "",
    var paper: PaperExtensionConfiguration? = null
) {
    fun paper(block: PaperExtensionConfiguration.() -> Unit) {
        paper = PaperExtensionConfiguration().apply(block)
    }

    fun paper() {
        paper = PaperExtensionConfiguration()
    }
}

private const val MIN_NAME_LENGTH = 5
private const val MAX_NAME_LENGTH = 25
private const val MIN_SHORT_DESCRIPTION_LENGTH = 10
private const val MAX_SHORT_DESCRIPTION_LENGTH = 80
private const val MIN_DESCRIPTION_LENGTH = 100
private const val MAX_DESCRIPTION_LENGTH = 2000
private val FORBIDDEN_WORDS = listOf("Adapter", "Extension")

internal fun TypewriterExtensionConfiguration.validate() {
    if (name.isBlank()) {
        throw MissingFieldException("extension.name")
    }
    if (name.length !in MIN_NAME_LENGTH..MAX_NAME_LENGTH) {
        throw FieldOutsideRangeException("extension.name", MIN_NAME_LENGTH, MAX_NAME_LENGTH)
    }
    if (!Regex("^[a-zA-Z0-9]+$").matches(name)) {
        throw IllegalArgumentException("Extension name '$name' must be alphanumeric.")
    }
    for (word in FORBIDDEN_WORDS) {
        if (name.contains(word, ignoreCase = true)) {
            throw IllegalArgumentException("Extension name '$name' cannot contain '$word'")
        }
    }

    if (shortDescription.isBlank()) {
        throw MissingFieldException("extension.shortDescription")
    }
    if (shortDescription.length !in MIN_SHORT_DESCRIPTION_LENGTH..MAX_SHORT_DESCRIPTION_LENGTH) {
        throw FieldOutsideRangeException(
            "extension.shortDescription",
            MIN_SHORT_DESCRIPTION_LENGTH,
            MAX_SHORT_DESCRIPTION_LENGTH
        )
    }

    if (description.isBlank()) {
        throw MissingFieldException("extension.description")
    }
    if (description.length !in MIN_DESCRIPTION_LENGTH..MAX_DESCRIPTION_LENGTH) {
        throw FieldOutsideRangeException("extension.description", MIN_DESCRIPTION_LENGTH, MAX_DESCRIPTION_LENGTH)
    }

    paper?.validate()
}

@Serializable
data class PaperExtensionConfiguration(
    val dependencies: MutableList<String> = mutableListOf(),
) {
    /**
     * Adds a dependency to the paper extension.
     * If the dependency is not found, the extension will not be loaded.
     */
    fun dependency(dependency: String) {
        dependencies.add(dependency)
    }
}

internal fun PaperExtensionConfiguration.validate() {
    val invalidDependencies = dependencies.filter {
        !Regex("^([a-zA-Z0-9])+\$").matches(it)
    }
    if (invalidDependencies.isNotEmpty()) {
        throw IllegalArgumentException("Invalid dependency(s): ${invalidDependencies.joinToString(", ")}")
    }
}

class MissingFieldException(field: String) : Exception("Field '$field' is is not set, yet is required.")
class FieldOutsideRangeException(field: String, min: Int, max: Int) :
    Exception("Field '$field' must be between $min and $max characters.")
