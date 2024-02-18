import com.github.jengelman.gradle.plugins.shadow.tasks.ShadowJar
import org.jetbrains.kotlin.util.capitalizeDecapitalize.capitalizeAsciiOnly

plugins {
    id("java")
    kotlin("jvm") version "1.9.22"
    `maven-publish`
    id("com.github.johnrengelman.shadow") version "8.1.1"
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
    // LirandAPI
    maven("https://jitpack.io")
    // PacketEvents
    maven("https://repo.codemc.io/repository/maven-releases/")
    // Anvil GUI (Sub dependency of LirandAPI)
    maven("https://repo.codemc.io/repository/maven-snapshots/")
    // PlaceholderAPI
    maven("https://repo.extendedclip.com/content/repositories/placeholderapi/")
    // Floodgate
    maven("https://repo.opencollab.dev/maven-snapshots/")
    // PaperMC
    maven { url = uri("https://repo.papermc.io/repository/maven-public/") }
}

val centralDependencies = listOf(
    "org.jetbrains.kotlin:kotlin-stdlib:1.9.22",
    "org.jetbrains.kotlin:kotlin-reflect:1.9.22",
    "org.jetbrains.kotlinx:kotlinx-coroutines-core:1.7.1",
    "com.corundumstudio.socketio:netty-socketio:1.7.19", // Keep this on a lower version as the newer version breaks the ping
)

dependencies {
    for (dependency in centralDependencies) {
        compileOnly(dependency)
    }
    compileOnly("io.papermc.paper:paper-api:1.20.4-R0.1-SNAPSHOT")

    implementation("com.github.dyam0:LirandAPI:96cc59d4fb")
    implementation("com.github.Tofaa2.EntityLib:spigot:2.0.1-SNAPSHOT")

    // Doesn't want to load properly using the spigot api.
    implementation("io.ktor:ktor-server-core-jvm:2.3.6")
    implementation("io.ktor:ktor-server-netty-jvm:2.3.6")
    implementation("io.insert-koin:koin-core:3.4.0")
    implementation("org.jetbrains.kotlinx:kotlinx-serialization-core:1.6.0")
    implementation("org.bstats:bstats-bukkit:3.0.2")

    compileOnly("com.github.shynixn.mccoroutine:mccoroutine-bukkit-api:2.11.0")
    compileOnly("com.github.shynixn.mccoroutine:mccoroutine-bukkit-core:2.11.0")
    compileOnly("net.kyori:adventure-api:4.15.0")
    compileOnly("net.kyori:adventure-text-minimessage:4.15.0")
    compileOnly("net.kyori:adventure-text-serializer-plain:4.15.0")
    compileOnly("net.kyori:adventure-text-serializer-legacy:4.15.0")
    compileOnly("net.kyori:adventure-text-serializer-gson:4.15.0")
    compileOnly("com.mojang:brigadier:1.0.18")
    compileOnly("me.clip:placeholderapi:2.11.3")
    compileOnly("com.google.code.gson:gson:2.10.1")
    compileOnly("com.github.retrooper.packetevents:spigot:2.2.1")
    compileOnly("org.geysermc.floodgate:api:2.2.0-SNAPSHOT")

    testImplementation("org.junit.jupiter:junit-jupiter:5.9.0")
}

val targetJavaVersion = 17
java {
    val javaVersion = JavaVersion.toVersion(targetJavaVersion)
    sourceCompatibility = javaVersion
    targetCompatibility = javaVersion
    toolchain.languageVersion.set(JavaLanguageVersion.of(targetJavaVersion))
    withSourcesJar()
}

tasks.withType<org.jetbrains.kotlin.gradle.tasks.KotlinCompile>().configureEach {
    kotlinOptions {
        jvmTarget = "$targetJavaVersion"
    }
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
    minimize {
        exclude(dependency("org.jetbrains.kotlin:kotlin-stdlib"))
        exclude(dependency("org.jetbrains.kotlin:kotlin-reflect"))
        exclude(dependency("com.github.shynixn.mccoroutine:mccoroutine-bukkit-core"))
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
        }
    }
}