import com.typewritermc.loader.ExtensionFlag

repositories {
    maven("https://mvn.lumine.io/repository/maven-public/")
}

dependencies {
    compileOnly("io.lumine:Mythic-Dist:5.6.1")
}

typewriter {
    engine {
        version = file("../../version.txt").readText().trim().substringBefore("-beta")
        channel = com.typewritermc.moduleplugin.ReleaseChannel.NONE
    }
    namespace = "typewritermc"

    extension {
        name = "MythicMobs"
        shortDescription = "Integrate MythicMobs with Typewriter."
        description = """
            |The MythicMobs Extension allows you to create MyticMobs, and trigger Skills from Typewriter.
            |Create cool particles during cinematics or have dialgues triggered when interacting with a MythicMob.
        """.trimMargin()

        flag(ExtensionFlag.Deprecated)

        paper {
            dependency("MythicMobs")
        }
    }
}