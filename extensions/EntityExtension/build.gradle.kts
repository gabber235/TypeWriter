repositories {}
dependencies {
    compileOnly("com.extollit.gaming:hydrazine-path-engine:1.8.1")
}

typewriter {
    engine {
        version = file("../../version.txt").readText().trim().substringBefore("-beta")
        channel = com.typewritermc.moduleplugin.ReleaseChannel.NONE
    }
    namespace = "typewritermc"

    extension {
        name = "Entity"
        shortDescription = "Create custom entities."
        description = """
            |The Entity Extension contains all the essential entries working with entities.
            |It allows you to create dynamic entities such as NPC's or Holograms.
            |
            |In most cases, it should be installed with Typewriter.
            |If you haven't installed Typewriter or the extension yet,
            |please follow the [Installation Guide](https://docs.typewritermc.com/docs/getting-started/installation)
            |first.
        """.trimMargin()

        paper()
    }
}