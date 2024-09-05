import com.typewritermc.loader.ExtensionFlag

repositories {
    maven("https://nexus.sirblobman.xyz/public/")
}

dependencies {
    compileOnly("com.github.sirblobman.api:core:2.9-SNAPSHOT")
    compileOnly("com.github.sirblobman.combatlogx:api:11.4-SNAPSHOT")
}

typewriter {
    engine {
        version = file("../../version.txt").readText().trim().substringBefore("-beta")
        channel = com.typewritermc.moduleplugin.ReleaseChannel.NONE
    }
    namespace = "typewritermc"

    extension {
        name = "CombatLogX"
        shortDescription = "Integrate CombatLogX with Typewriter."
        description = """
            |The CombatLogX Extension allows you to create entries that are triggered when a player enters or leaves combat.
        """.trimMargin()

        flag(ExtensionFlag.Deprecated)

        paper {
            dependency("CombatLogX")
        }
    }
}