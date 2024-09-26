repositories {}
dependencies {}

typewriter {
    engine {
        version = file("../../version.txt").readText().trim().substringBefore("-beta")
        channel = com.typewritermc.moduleplugin.ReleaseChannel.NONE
    }
    namespace = "typewritermc"

    extension {
        name = "Documentation"
        shortDescription = "Documentation for Typewriter."
        description = """
            |This extension contains the documentation for Typewriter.
            |It has examples of how to use the different parts of Typewriter.
            |It should not be used as a dependency.
            """.trimMargin()

        paper()
    }
}
