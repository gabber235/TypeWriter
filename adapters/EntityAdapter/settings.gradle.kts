rootProject.name = "EntityAdapter"

includeBuild("../../plugin")

plugins {
    id("com.gradle.enterprise") version ("3.13.3")
}

gradleEnterprise {
    buildScan {
        termsOfServiceUrl = "https://gradle.com/terms-of-service"
        termsOfServiceAgree = "yes"
    }
}
