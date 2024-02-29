includeBuild("../plugin")

val directories = file("./").listFiles()?.filter {
    it.name.endsWith("Adapter") && it.isDirectory
} ?: emptyList()

for (directory in directories) {
    include(directory.name)
}

plugins {
    id("com.gradle.enterprise") version ("3.13.3")
}

gradleEnterprise {
    buildScan {
        termsOfServiceUrl = "https://gradle.com/terms-of-service"
        termsOfServiceAgree = "yes"
    }
}
