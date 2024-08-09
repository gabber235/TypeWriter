package com.typewritermc.example

//<code-block:adapter>
import me.gabber235.typewriter.adapters.Adapter
import me.gabber235.typewriter.adapters.TypewriterAdapter

@Adapter("Example", "An example adapter for documentation purposes", "0.0.1")
object ExampleAdapter : TypewriterAdapter() {
    override fun initialize() {
        // Do something when the adapter is initialized
    }

    override fun shutdown() {
        // Do something when the adapter is shutdown
    }
}
//</code-block:adapter>