repositories {
    maven("https://repo.citizensnpcs.co/")
}

dependencies {
    // External dependencies
    compileOnly("net.citizensnpcs:citizens-main:2.0.33-SNAPSHOT") {
        exclude(group = "*", module = "*")
    }
}
