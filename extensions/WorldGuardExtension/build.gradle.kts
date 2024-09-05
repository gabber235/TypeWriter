repositories {
    maven("https://maven.enginehub.org/repo/")
}

dependencies {
    compileOnly("com.sk89q.worldguard:worldguard-bukkit:7.0.11-SNAPSHOT")
}

typewriter {
    namespace = "typewritermc"
    engine {
        version = file("../../version.txt").readText().trim().substringBefore("-beta")
        channel = com.typewritermc.moduleplugin.ReleaseChannel.NONE
    }

    extension {
        name = "WorldGuard"
        shortDescription = "Integrate WorldGuard with Typewriter."
        description = """
            |The WorldGuard Extension allows you to create dialogue triggered by WorldGuard regions.
            |Have dialogues that only show up when a player enters or leaves a specific region.
            |Have sidebars that only show when the player is in a specific region.
        """.trimMargin()

        paper {
            dependency("WorldGuard")
        }
    }
}