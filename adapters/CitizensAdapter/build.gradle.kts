import org.jetbrains.kotlin.gradle.tasks.KotlinCompile
import org.jetbrains.kotlin.util.capitalizeDecapitalize.capitalizeAsciiOnly

plugins {
	kotlin("jvm") version "1.7.20"
	id("com.github.johnrengelman.shadow") version "5.2.0"
}

group = "me.gabber235"
version = "0.0.1"

repositories {
	// Required
	maven("https://jitpack.io")
	mavenCentral()
	maven("https://hub.spigotmc.org/nexus/content/repositories/snapshots/")
	maven("https://oss.sonatype.org/content/groups/public/")
	maven("https://libraries.minecraft.net/")
	maven("https://repo.codemc.io/repository/maven-snapshots/")
	maven("https://repo.opencollab.dev/maven-snapshots/")

	// Adapter Specific
	maven("https://repo.citizensnpcs.co/")
}

dependencies {
	compileOnly("org.spigotmc:spigot-api:1.19.2-R0.1-SNAPSHOT")

	compileOnly("me.gabber235:typewriter:1.0.0")

	// Already included in the TypeWriter plugin
	compileOnly("org.jetbrains.kotlinx:kotlinx-coroutines-core:1.6.4")
	compileOnly("com.github.dyam0:LirandAPI:26f60a4baa")
	compileOnly("net.kyori:adventure-api:4.12.0")
	compileOnly("net.kyori:adventure-text-minimessage:4.12.0")
	compileOnly("com.mojang:brigadier:1.0.18")

	// External dependencies
	compileOnly("net.citizensnpcs:citizens-main:2.0.30-SNAPSHOT") {
		exclude(group = "*", module = "*")
	}

	testImplementation(kotlin("test"))
}

tasks.test {
	useJUnitPlatform()
}

tasks.withType<KotlinCompile> {
	kotlinOptions.jvmTarget = "1.8"
}

task<com.github.jengelman.gradle.plugins.shadow.tasks.ShadowJar>("buildAndMove") {
	dependsOn("clean", "shadowJar")

	group = "build"
	description = "Builds the jar and moves it to the server folder"

	// Move the jar from the build/libs folder to the server/plugins folder
	doLast {
		val jar = file("build/libs/%s-%s-all.jar".format(project.name, project.version))
		val server =
			file("../../plugin/server/plugins/Typewriter/adapters/%s.jar".format(project.name.capitalizeAsciiOnly()))
		jar.copyTo(server, overwrite = true)
	}
}