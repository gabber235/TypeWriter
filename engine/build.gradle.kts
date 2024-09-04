plugins {
    id("java")
    kotlin("jvm") version "2.0.20"
    id("java-library")
    `maven-publish`
    id("io.github.goooler.shadow") version "8.1.7" apply false
}

group = "com.typewritermc"
val versionFile = if (file("version.txt").exists()) file("version.txt") else file("../version.txt")
version = versionFile.readText().trim()

allprojects {
    apply(plugin = "java")
    apply(plugin = "kotlin")

    repositories {
        mavenCentral()
        // PacketEvents
        maven("https://repo.codemc.io/repository/maven-snapshots/")
    }


    tasks.test {
        useJUnitPlatform()
    }
    kotlin {
        jvmToolchain(21)
    }
}

subprojects {
    apply(plugin = "io.github.goooler.shadow")
    apply(plugin = "java-library")
    apply(plugin = "maven-publish")

    group = rootProject.group
    version = rootProject.version

    dependencies {
        api("io.insert-koin:koin-core:3.5.6")
        compileOnly("com.google.code.gson:gson:2.11.0")
        testImplementation(kotlin("test"))
    }


    publishing {
        repositories {
            maven {
                name = "TypewriterReleases"
                url = uri("https://maven.typewritermc.com/releases")
                credentials(PasswordCredentials::class)
                authentication {
                    create<BasicAuthentication>("basic")
                }
            }
            maven {
                name = "TypewriterBeta"
                url = uri("https://maven.typewritermc.com/beta")
                credentials(PasswordCredentials::class)
                authentication {
                    create<BasicAuthentication>("basic")
                }
            }
        }
        publications {
            create<MavenPublication>("maven") {
                group = project.group
                // Remove everything after the beta. So 1.0.0-beta-1 becomes 1.0.0
                version = project.version.toString().substringBefore("-beta")
                artifactId = project.name

                from(components["kotlin"])
                artifact(tasks["shadowJar"])
            }
        }
    }
}
