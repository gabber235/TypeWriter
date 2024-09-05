import com.typewritermc.loader.ExtensionFlag

repositories {
    maven("https://repo.citizensnpcs.co/")
}

dependencies {
    // External dependencies
    compileOnly("net.citizensnpcs:citizens-main:2.0.33-SNAPSHOT") {
        exclude(group = "*", module = "*")
    }
}

typewriter {
    engine {
        version = file("../../version.txt").readText().trim().substringBefore("-beta")
        channel = com.typewritermc.moduleplugin.ReleaseChannel.NONE
    }
    namespace = "typewritermc"

    extension {
        name = "Citizens"
        shortDescription = "Create custom interactions with Citizens NPCs."
        description = """
            |The Citizens extension allows you to create custom interactions with Citizens NPCs.
            |Letting you to create dialogues, actions, and more when interacting with NPCs.
        """.trimMargin()
        flag(ExtensionFlag.Unsupported)

        paper {
            dependency("Citizens")
        }
    }
}