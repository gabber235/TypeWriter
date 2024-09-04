package com.typewritermc.example

//<code-block:initializer>
import com.typewritermc.core.extension.Initializable
import com.typewritermc.core.extension.annotations.Initializer

@Initializer
object ExampleInitializer : Initializable {
    override fun initialize() {
        // Do something when the extension is initialized
    }

    override fun shutdown() {
        // Do something when the extension is shutdown
    }
}
//</code-block:initializer>