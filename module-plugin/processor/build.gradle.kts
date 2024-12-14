import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    id("com.google.devtools.ksp") version "2.0.21-1.0.26"
}

repositories {
    maven("https://repo.codemc.io/repository/maven-snapshots/")
}

dependencies {
    api("com.google.code.gson:gson:2.11.0")
    api(project(":api"))

    api("org.jetbrains.kotlinx:kotlinx-coroutines-core:1.9.0")
}

tasks.withType(KotlinCompile::class.java) {
    kotlinOptions.freeCompilerArgs += "-Xcontext-receivers"
}