repositories { }
dependencies { }

typewriter {
    engine {
        version = file("../../version.txt").readText().trim().substringBefore("-beta")
        channel = com.typewritermc.moduleplugin.ReleaseChannel.NONE
    }
    namespace = "typewritermc"

    extension {
        name = "Basic"
        shortDescription = "For all the most basic entries"
        description = """
            The Basic Extension contains all the essential entries for any server.
            In most cases, it should be installed with Typewriter.
            If you haven't installed Typewriter or the Basic Extension,
            please follow the [Installation Guide](https://docs.typewritermc.com/docs/getting-started/installation)
            first.
        """.trimIndent()

        paper()
    }
}