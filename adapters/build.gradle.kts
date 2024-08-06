import com.github.jengelman.gradle.plugins.shadow.tasks.ShadowJar
import org.jetbrains.kotlin.util.capitalizeDecapitalize.capitalizeAsciiOnly

plugins {
    kotlin("jvm") version "2.0.0"
    id("io.github.goooler.shadow") version "8.1.7"
}

allprojects {
    repositories {
        // Required
        mavenCentral()
        maven("https://repo.papermc.io/repository/maven-public/")
        maven("https://repo.codemc.io/repository/maven-releases/")
        maven("https://repo.opencollab.dev/maven-snapshots/")
        maven("https://jitpack.io")
    }
}


subprojects {
    group = "me.gabber235"
    version = file("../../version.txt").readText().trim()

    apply(plugin = "java")
    apply(plugin = "kotlin")
    apply(plugin = "io.github.goooler.shadow")

    dependencies {
        compileOnly("com.github.gabber235:typewriter:$version")
    }

    tasks.withType<ShadowJar> {
        exclude("kotlin/**")
        exclude("META-INF/maven/**")
        // Important: Use the relocated commandapi which is shadowed by the plugin
        relocate("dev.jorel.commandapi", "com.github.gabber235.typewriter.extensions.commandapi") {
            include("dev.jorel.commandapi.**")
        }
    }

    val targetJavaVersion = 21
    java {
        val javaVersion = JavaVersion.toVersion(targetJavaVersion)
        sourceCompatibility = javaVersion
        targetCompatibility = javaVersion
        toolchain.languageVersion.set(JavaLanguageVersion.of(targetJavaVersion))
    }

    kotlin {
        jvmToolchain(targetJavaVersion)
    }

    val copyTemplates by tasks.registering(Copy::class) {
        filteringCharset = "UTF-8"
        from(projectDir.resolve("src/main/templates")) {
            expand("version" to version)
        }
        into(buildDir.resolve("generated-sources/templates/kotlin/main"))
    }

    sourceSets {
        main {
            java.srcDirs(copyTemplates)
        }
    }

    tasks.register("buildAndMove") {
        dependsOn("shadowJar")

        group = "build"
        description = "Builds the jar and moves it to the server folder"
        outputs.upToDateWhen { false }

        // Move the jar from the build/libs folder to the server/plugins folder
        doLast {
            val jar = file("build/libs/%s-%s-all.jar".format(project.name, project.version))
            val server =
                file("../../plugin/server/plugins/Typewriter/adapters/%s.jar".format(project.name.capitalizeAsciiOnly()))
            jar.copyTo(server, overwrite = true)
        }
    }

    tasks.register("buildRelease") {
        dependsOn("shadowJar")
        group = "build"
        description = "Builds the jar and renames it"

        doLast {
            // Rename the jar to remove the version and -all
            val jar = file("build/libs/%s-%s-all.jar".format(project.name, project.version))
            jar.renameTo(file("build/libs/%s.jar".format(project.name)))
            file("build/libs/%s-%s.jar".format(project.name, project.version)).delete()
        }
    }
}


