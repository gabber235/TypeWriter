rootProject.name = "module-plugin"

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
includeBuild("../engine")
include("api")
include("processor")
include("engine-processor")
include("extension-processor")
