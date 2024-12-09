repositories {}
dependencies {
    compileOnly(project(":RoadNetworkExtension"))
}

typewriter {
    engine {
        version = file("../../version.txt").readText().trim().substringBefore("-beta")
        channel = com.typewritermc.moduleplugin.ReleaseChannel.NONE
    }
    namespace = "typewritermc"

    extension {
        name = "Quest"
        shortDescription = "Questing System in Typewriter"
        description = """
            |A questing system for Typewriter that allows players to track quests and complete them.
            |The questing system is for displaying the progress to a player.
            |
            |It is **not** necessary to use this extension for quests.
            |It is just a visual novelty.
            """.trimMargin()

        paper()
    }
}
