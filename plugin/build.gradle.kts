import com.github.jengelman.gradle.plugins.shadow.tasks.ShadowJar
import io.papermc.hangarpublishplugin.model.Platforms
import org.jetbrains.kotlin.util.capitalizeDecapitalize.capitalizeAsciiOnly
import java.io.ByteArrayOutputStream

plugins {
    id("java")
    kotlin("jvm") version "2.0.0"
    `maven-publish`
    id("io.github.goooler.shadow") version "8.1.7"
    id("java-library")
    id("io.papermc.hangar-publish-plugin") version "0.1.2"
}

fun Project.findPropertyOr(property: String, default: String): String {
    val prop = findProperty(property)?.toString() ?: ""
    if (prop.isBlank()) return default
    if (prop == "unspecified") return default
    return prop
}

group = project.findPropertyOr("group", "com.github.gabber235")
val versionFile = if (file("version.txt").exists()) file("version.txt") else file("../version.txt")
version = project.findPropertyOr("version", versionFile.readText().trim())

repositories {
    mavenCentral()
    // Floodgate
    maven("https://repo.opencollab.dev/maven-snapshots/")
    // PacketEvents, CommandAPI
    maven("https://repo.codemc.io/repository/maven-releases/")
    // PlaceholderAPI
    maven("https://repo.extendedclip.com/content/repositories/placeholderapi/")
    // PaperMC
    maven("https://repo.papermc.io/repository/maven-public/")
    // EntityLib
    maven("https://jitpack.io")
}

val centralDependencies = listOf(
    "org.jetbrains.kotlin:kotlin-stdlib:2.0.0",
    "org.jetbrains.kotlin:kotlin-reflect:2.0.0",
    "org.jetbrains.kotlinx:kotlinx-coroutines-core:1.9.0-RC",
    "com.corundumstudio.socketio:netty-socketio:1.7.19", // Keep this on a lower version as the newer version breaks the ping
)

dependencies {
    for (dependency in centralDependencies) {
        compileOnlyApi(dependency)
    }
    compileOnlyApi("io.papermc.paper:paper-api:1.21-R0.1-SNAPSHOT")

    api("com.github.Tofaa2.EntityLib:spigot:2.4.9-SNAPSHOT")
    api("com.github.shynixn.mccoroutine:mccoroutine-bukkit-api:2.17.0")
    api("com.github.shynixn.mccoroutine:mccoroutine-bukkit-core:2.17.0")
    api("dev.jorel:commandapi-bukkit-shade:9.5.2")
    api("dev.jorel:commandapi-bukkit-kotlin:9.5.2")

    // Doesn't want to load properly using the spigot api.
    implementation("io.ktor:ktor-server-core-jvm:2.3.12")
    implementation("io.ktor:ktor-server-netty-jvm:2.3.12")
    api("io.insert-koin:koin-core:3.5.6")
    implementation("org.jetbrains.kotlinx:kotlinx-serialization-core:1.7.0")
    implementation("org.bstats:bstats-bukkit:3.0.2")
    implementation("com.extollit.gaming:hydrazine-path-engine:1.8.1")

    val adventureVersion = "4.17.0"
    compileOnlyApi("net.kyori:adventure-api:$adventureVersion")
    compileOnlyApi("net.kyori:adventure-text-minimessage:$adventureVersion")
    compileOnlyApi("net.kyori:adventure-text-serializer-plain:$adventureVersion")
    compileOnlyApi("net.kyori:adventure-text-serializer-legacy:$adventureVersion")
    compileOnlyApi("net.kyori:adventure-text-serializer-gson:$adventureVersion")
    compileOnly("me.clip:placeholderapi:2.11.6")
    compileOnly("com.google.code.gson:gson:2.11.0")
    compileOnlyApi("com.github.retrooper:packetevents-spigot:2.4.0")
    compileOnlyApi("org.geysermc.floodgate:api:2.2.0-SNAPSHOT")

    testImplementation("org.junit.jupiter:junit-jupiter:5.9.0")
}

val targetJavaVersion = 21
// TODO: Sync versions back up to 21. This is a temporary fix to allow 1.20.4 servers to still use java 17.
val languageJavaVersion = 17
java {
    val javaVersion = JavaVersion.toVersion(targetJavaVersion)
    val languageVersion = JavaVersion.toVersion(languageJavaVersion)
    sourceCompatibility = javaVersion
    targetCompatibility = languageVersion
    toolchain.languageVersion.set(JavaLanguageVersion.of(targetJavaVersion))
    withSourcesJar()

    disableAutoTargetJvm()
}

kotlin {
    jvmToolchain(languageJavaVersion)
}

tasks.test {
    useJUnitPlatform()
}

tasks.processResources {
    filteringCharset = "UTF-8"
    filesMatching("plugin.yml") {
        expand("version" to version, "libraries" to centralDependencies)
    }
}

tasks.withType<ShadowJar> {
    relocate("org.bstats", "${project.group}.${project.name}.extensions.bstats")
    relocate("dev.jorel.commandapi", "${project.group}.${project.name}.extensions.commandapi") {
        include("dev.jorel.commandapi.**")
    }
    minimize {
        exclude(dependency("org.jetbrains.kotlin:kotlin-stdlib"))
        exclude(dependency("org.jetbrains.kotlin:kotlin-reflect"))
        exclude(dependency("com.github.shynixn.mccoroutine:mccoroutine-bukkit-core"))
        exclude(dependency("com.github.Tofaa2.EntityLib:spigot"))
        exclude(dependency("web::"))
    }
}

task<ShadowJar>("buildAndMove") {
    dependsOn("shadowJar")

    group = "build"
    description = "Builds the jar and moves it to the server folder"
    outputs.upToDateWhen { false }

    // Move the jar from the build/libs folder to the server/plugins folder
    doLast {
        val jar = file("build/libs/%s-%s-all.jar".format(project.name, project.version))
        val server = file("server/plugins/%s.jar".format(project.name.capitalizeAsciiOnly()))
        jar.copyTo(server, overwrite = true)
    }
}

task("copyFlutterWebFiles") {
    group = "build"
    description = "Copies the flutter web files to the resources folder"

    doLast {
        val flutterWeb = file("../app/build/web")
        val flutterWebDest = file("src/main/resources/web")
        flutterWeb.copyRecursively(flutterWebDest, overwrite = true)
    }
}

task("buildRelease") {
    dependsOn("copyFlutterWebFiles", "shadowJar")
    group = "build"
    description = "Builds the jar including the flutter web panel"

    outputs.upToDateWhen { false }

    doLast {
        // Remove the flutter web folder
        val flutterWebDest = file("src/main/resources/web")
        flutterWebDest.deleteRecursively()

        // Rename the jar to remove the version and -all
        val jar = file("build/libs/%s-%s-all.jar".format(project.name, project.version))
        if (!jar.exists()) {
            throw IllegalStateException("Jar does not exist: ${jar.absolutePath}")
        }
        jar.renameTo(file("build/libs/%s.jar".format(project.name)))
        file("build/libs/%s-%s.jar".format(project.name, project.version)).delete()
    }
}

publishing {
    publications {
        create<MavenPublication>("main") {
            group = project.group
            version = project.version.toString()
            artifactId = project.name

            from(components["kotlin"])
            artifact(tasks["sourcesJar"])
            artifact(tasks["shadowJar"])
        }
    }
}

fun executeGitCommand(vararg command: String): String {
    val byteOut = ByteArrayOutputStream()
    exec {
        commandLine = listOf("git", *command)
        standardOutput = byteOut
    }
    return byteOut.toString(Charsets.UTF_8.name()).trim()
}

fun latestCommitMessage(): String {
    return executeGitCommand("log", "-1", "--pretty=%B")
}

hangarPublish {
    publications.register("plugin") {
        version.set(project.version.toString())
        if (project.version.toString().contains("beta")) {
            channel.set("Beta")
        } else {
            channel.set("Release")
        }

        id.set("Typewriter")
        changelog.set(latestCommitMessage())
        apiKey.set(System.getenv("HANGAR_API_TOKEN"))

        platforms {
            register(Platforms.PAPER) {
                url.set("https://modrinth.com/plugin/typewriter/version/${project.version}")

                val versions: List<String> = (property("paperVersion") as String)
                    .split(",")
                    .map { it.trim() }
                platformVersions.set(versions)

                dependencies {
                    url("PacketEvents", "https://modrinth.com/plugin/packetevents/versions?l=paper&l=purpur") {
                        required.set(true)
                    }
                }
            }
        }
    }
}