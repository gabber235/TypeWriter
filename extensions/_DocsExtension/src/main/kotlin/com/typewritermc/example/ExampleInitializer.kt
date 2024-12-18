package com.typewritermc.example

//<code-block:initializer>
import com.typewritermc.core.extension.Initializable
import com.typewritermc.core.extension.annotations.Singleton

@Singleton
object ExampleInitializer : Initializable {
    override suspend fun initialize() {
        // Do something when the extension is initialized
    }

    override suspend fun shutdown() {
        // Do something when the extension is shutdown
    }
}
//</code-block:initializer>