import com.github.jengelman.gradle.plugins.shadow.tasks.ShadowJar

plugins {
	kotlin("jvm") version "1.7.20"
	id("com.github.johnrengelman.shadow") version "5.2.0"
}

group = "com.caleb"
version = file("../../version.txt").readText().trim()

repositories {
	// Required
	maven("https://jitpack.io")
	mavenCentral()
	maven("https://hub.spigotmc.org/nexus/content/repositories/snapshots/")
	maven("https://oss.sonatype.org/content/groups/public/")
	maven("https://libraries.minecraft.net/")
	maven { url = uri("https://repo.papermc.io/repository/maven-public/") }
	maven("https://repo.codemc.io/repository/maven-snapshots/")
	maven("https://repo.opencollab.dev/maven-snapshots/")
	maven("https://jitpack.io/")

	// Adapter Specific

}

dependencies {
	compileOnly(kotlin("stdlib"))
	compileOnly("io.papermc.paper:paper-api:1.19.3-R0.1-SNAPSHOT")

	compileOnly("me.gabber235:typewriter:$version")

	// Already included in the TypeWriter plugin
	compileOnly("org.jetbrains.kotlinx:kotlinx-coroutines-core:1.6.4")
	compileOnly("com.github.dyam0:LirandAPI:26f60a4baa")
	compileOnly("net.kyori:adventure-api:4.12.0")
	compileOnly("net.kyori:adventure-text-minimessage:4.12.0")
	compileOnly("com.mojang:brigadier:1.0.18")
	compileOnly("com.github.MilkBowl:VaultAPI:1.7")

	// External dependencies
	//compileOnly("com.bgsoftware:SuperiorSkyblockAPI:1.11.1")

	testImplementation(kotlin("test"))

}

tasks.test {
	useJUnitPlatform()
}

val targetJavaVersion = 17
java {
	val javaVersion = JavaVersion.toVersion(targetJavaVersion)
	sourceCompatibility = javaVersion
	targetCompatibility = javaVersion
}

task<ShadowJar>("buildAndMove") {
	dependsOn("shadowJar")

	group = "build"
	description = "Builds the jar and moves it to the server folder"

	// Move the jar from the build/libs folder to the server/plugins folder
	doLast {
		val jar = file("build/libs/%s-%s-all.jar".format(project.name, project.version))
		val server =
			file("../../plugin/server/plugins/Typewriter/adapters/%s.jar".format(project.name))
		jar.copyTo(server, overwrite = true)
	}
}

task<ShadowJar>("buildRelease") {
	dependsOn("shadowJar")
	group = "build"
	description = "Builds the jar and renames it"

	doLast {
		// Rename the jar to remove the version and -all
		val jar = file("build/libs/%s-%s-all.jar".format(project.name, project.version))
		jar.renameTo(file("build/libs/%s.jar".format(project.name)))
	}
}