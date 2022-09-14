import com.github.jengelman.gradle.plugins.shadow.tasks.ShadowJar
import org.jetbrains.kotlin.util.capitalizeDecapitalize.capitalizeAsciiOnly

plugins {
	id("java")
	kotlin("jvm") version "1.7.10"
	id("com.github.johnrengelman.shadow") version "5.2.0"
}

group = "me.gabber235"
version = "0.0.1"

repositories {
	maven("https://repo.citizensnpcs.co/")
	maven("https://jitpack.io")
	mavenCentral()
	maven("https://repo.dmulloy2.net/repository/public/")
	maven("https://hub.spigotmc.org/nexus/content/repositories/snapshots/")
	maven("https://oss.sonatype.org/content/groups/public/")
	maven("https://libraries.minecraft.net/")
	maven("https://repo.codemc.io/repository/maven-snapshots/")
	maven("https://repo.opencollab.dev/maven-snapshots/")
}

dependencies {
	implementation(kotlin("stdlib"))
	compileOnly("org.spigotmc:spigot-api:1.19.2-R0.1-SNAPSHOT")

	implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core:1.6.4")
	implementation("com.github.dyam0:LirandAPI:26f60a4baa")
	compileOnly("com.mojang:brigadier:1.0.18")
	implementation("net.kyori:adventure-api:4.11.0")
	implementation("net.kyori:adventure-text-minimessage:4.11.0")
	implementation("net.kyori:adventure-text-serializer-plain:4.11.0")
	implementation("net.kyori:adventure-text-serializer-legacy:4.11.0")
	implementation("net.kyori:adventure-text-serializer-gson:4.11.0")
	implementation("net.kyori:adventure-platform-bukkit:4.1.2")
	compileOnly("net.citizensnpcs:citizensapi:2.0.30-SNAPSHOT")
	compileOnly("com.google.code.gson:gson:2.9.1")
	compileOnly("com.comphenix.protocol:ProtocolLib:5.0.0-SNAPSHOT")
	compileOnly("org.geysermc.floodgate:api:2.2.0-SNAPSHOT")
	
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
	dependsOn("clean", "shadowJar")

	group = "build"
	description = "Builds the jar and moves it to the server folder"

	// Move the jar from the build/libs folder to the server/plugins folder
	doLast {
		val jar = file("build/libs/%s-%s-all.jar".format(project.name, project.version))
		val server = file("server/plugins/%s.jar".format(project.name.capitalizeAsciiOnly()))
		jar.copyTo(server, overwrite = true)
	}
}