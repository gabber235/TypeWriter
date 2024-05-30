package me.gabber235.typewriter

import App
import me.gabber235.typewriter.adapters.Adapter
import me.gabber235.typewriter.adapters.TypewriterAdapter

@Adapter("Basic", "For all the most basic entries", App.VERSION)
/**
 * The Basic Adapter contains all the essential entries for Typewriter.
 * In most cases, it should be installed with Typewriter.
 * If you haven't installed Typewriter or the adapter yet,
 * please follow the [Installation Guide](../../docs/02-getting-started/01-installation.mdx)
 * first.
 */
object BasicAdapter : TypewriterAdapter() {
    override fun initialize() {

    }
}