import com.github.jengelman.gradle.plugins.shadow.tasks.ShadowJar
import org.jetbrains.kotlin.util.capitalizeDecapitalize.capitalizeAsciiOnly

plugins {
	id("java")
	kotlin("jvm") version "1.7.20"
	id("com.github.johnrengelman.shadow") version "5.2.0"
}

group = "me.gabber235"
version = "0.0.1"

repositories {
	mavenCentral()
	maven("https://jitpack.io")
	maven("https://repo.dmulloy2.net/repository/public/")
	maven("https://hub.spigotmc.org/nexus/content/repositories/snapshots/")
	maven("https://oss.sonatype.org/content/groups/public/")
	maven("https://libraries.minecraft.net/")
	maven("https://repo.codemc.io/repository/maven-snapshots/")
	maven("https://repo.extendedclip.com/content/repositories/placeholderapi/")
	maven("https://repo.opencollab.dev/maven-snapshots/")
	maven { url = uri("https://repo.papermc.io/repository/maven-public/") }
}

dependencies {
	implementation(kotlin("stdlib"))
	implementation("org.jetbrains.kotlin:kotlin-reflect:1.7.20")
	compileOnly("io.papermc.paper:paper-api:1.19.3-R0.1-SNAPSHOT")

	implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core:1.6.4")
	implementation("com.github.dyam0:LirandAPI:26f60a4baa")
	compileOnly("net.kyori:adventure-api:4.12.0")
	compileOnly("net.kyori:adventure-text-minimessage:4.12.0")
	compileOnly("net.kyori:adventure-text-serializer-plain:4.12.0")
	compileOnly("net.kyori:adventure-text-serializer-legacy:4.12.0")
	compileOnly("net.kyori:adventure-text-serializer-gson:4.12.0")
	implementation("net.kyori:adventure-platform-bukkit:4.1.2")
	compileOnly("com.mojang:brigadier:1.0.18")
	compileOnly("me.clip:placeholderapi:2.11.2")
	compileOnly("com.google.code.gson:gson:2.10")
	compileOnly("com.comphenix.protocol:ProtocolLib:5.0.0-SNAPSHOT")
	compileOnly("org.geysermc.floodgate:api:2.2.0-SNAPSHOT")

	// Client communication
	implementation("com.corundumstudio.socketio:netty-socketio:1.7.19")

	testImplementation("org.junit.jupiter:junit-jupiter:5.9.0")
}

val targetJavaVersion = 17
java {
	val javaVersion = JavaVersion.toVersion(targetJavaVersion)
	sourceCompatibility = javaVersion
	targetCompatibility = javaVersion
}

tasks.test {
	useJUnitPlatform()
}


tasks.processResources {
	filteringCharset = "UTF-8"
	filesMatching("plugin.yml") {
		expand("version" to version)
	}
}

task<ShadowJar>("buildAndMove") {
	dependsOn("shadowJar")

	group = "build"
	description = "Builds the jar and moves it to the server folder"

	// Move the jar from the build/libs folder to the server/plugins folder
	doLast {
		val jar = file("build/libs/%s-%s-all.jar".format(project.name, project.version))
		val server = file("server/plugins/%s.jar".format(project.name.capitalizeAsciiOnly()))
		jar.copyTo(server, overwrite = true)
	}
}