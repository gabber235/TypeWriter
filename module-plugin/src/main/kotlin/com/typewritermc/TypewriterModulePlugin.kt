package com.typewritermc

import com.google.devtools.ksp.gradle.KspExtension
import com.typewritermc.moduleplugin.TypewriterModuleConfiguration
import com.typewritermc.moduleplugin.validate
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import org.gradle.api.GradleException
import org.gradle.api.Plugin
import org.gradle.api.Project
import java.util.*

class TypewriterModulePlugin : Plugin<Project> {
    override fun apply(project: Project) {
        project.pluginManager.apply("com.google.devtools.ksp")

        project.extensions
        val extension = project.extensions.create("typewriter", TypewriterModuleConfiguration::class.java)

        project.registerKsp(extension)
        project.afterEvaluate {
            project.checkExtension(extension)

            project.registerRepositories(extension)
        }
    }

    /**
     * Checks if the extension is valid.
     */
    private fun Project.checkExtension(extension: TypewriterModuleConfiguration) {
        if (extension.namespace.isBlank() && extension.flags.isEmpty() && extension.engine == null && extension.extension == null) {
            logger.warn("No extension configuration found in module $name. The typewriter plugin will not work.")
            return
        }
        try {
            extension.validate()
        } catch (e: Exception) {
            throw GradleException("Invalid module configuration for $name", e)
        }
    }

    /**
     * Registers repositories that are required for typewriter to work.
     */
    private fun Project.registerRepositories(extension: TypewriterModuleConfiguration) {
        // Add Maven Central repository
        repositories.mavenCentral()

        extension.engine?.let { engine ->
            // Add Typewriter repository
            engine.channel.url?.let { url ->
                repositories.maven {
                    it.setUrl(url)
                }
            }
            // Add Typewriter dependency
            dependencies.add("compileOnly", "com.typewritermc:engine-core:${engine.version}")
        }


        // Add PacketEvents repository
        repositories.maven {
            it.setUrl("https://repo.codemc.io/repository/maven-snapshots/")
        }
        // Add EntityLib repository
        repositories.maven {
            it.setUrl("https://maven.evokegames.gg/snapshots")
        }

        if (extension.extension?.paper != null) {
            extension.engine?.let { engine ->
                // Add Typewriter Paper dependency
                dependencies.add("compileOnly", "com.typewritermc:engine-paper:${engine.version}")
                // Add Paper repository
                repositories.maven {
                    it.setUrl("https://repo.papermc.io/repository/maven-public/")
                }
                // Add Geyser repository
                repositories.maven {
                    it.setUrl("https://repo.opencollab.dev/maven-snapshots/")
                }
            }
        }
    }

    private fun Project.registerKsp(configuration: TypewriterModuleConfiguration) {
        val pluginVersion = getPluginVersion()
        dependencies.add("ksp", "com.typewritermc.module-plugin:extension-processor:$pluginVersion")

        afterEvaluate {
            extensions.configure(KspExtension::class.java) {
                it.arg(
                    "configuration",
                    Base64.getEncoder().encodeToString(Json.encodeToString(configuration).toByteArray())
                )
                it.arg("pluginVersion", pluginVersion)
                it.arg("version", version.toString())
                it.arg("buildDir", buildDir.absolutePath)
            }
        }
    }

    private fun getPluginVersion(): String {
        val props = Properties().apply {
            this@TypewriterModulePlugin::class.java.classLoader.getResourceAsStream("typewriter-module-plugin.properties")
                ?.use { stream ->
                    load(stream)
                } ?: throw IllegalStateException("Could not load properties")
        }
        val version = props.getProperty("version")
        return version ?: "0.0.0"
    }
}