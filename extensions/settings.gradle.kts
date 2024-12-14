includeBuild("../engine")
includeBuild("../module-plugin")

val ignoringExtensions: List<String> = emptyList()

val directories = file("./").listFiles()?.filter {
    it.name.endsWith("Extension") && it.isDirectory && it.name !in ignoringExtensions
} ?: emptyList()

for (directory in directories) {
    include(directory.name)
}

pluginManagement {
    repositories {
        mavenLocal()
        mavenCentral()
        gradlePluginPortal()
    }
}

plugins {
    id("com.gradle.enterprise") version ("3.13.3")
    id("org.gradle.toolchains.foojay-resolver-convention") version "0.8.0"
}

gradleEnterprise {
    buildScan {
        termsOfServiceUrl = "https://gradle.com/terms-of-service"
        termsOfServiceAgree = "yes"
    }
}