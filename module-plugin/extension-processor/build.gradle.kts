import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    id("com.google.devtools.ksp") version "2.0.20-1.0.24"
}

repositories {
    maven("https://repo.codemc.io/repository/maven-snapshots/")
}

dependencies {
    implementation("com.google.code.gson:gson:2.11.0")
    implementation(project(":api"))

    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core:1.9.0")

    val ktorVersion = "2.3.12"
    implementation("io.ktor:ktor-client-core:$ktorVersion")
    implementation("io.ktor:ktor-client-cio:$ktorVersion")
}

tasks.withType(KotlinCompile::class.java) {
    kotlinOptions.freeCompilerArgs += "-Xcontext-receivers"
}