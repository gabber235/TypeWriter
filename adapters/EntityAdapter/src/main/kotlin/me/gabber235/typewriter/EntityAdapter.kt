package me.gabber235.typewriter

import App
import me.gabber235.typewriter.adapters.Adapter
import me.gabber235.typewriter.adapters.TypewriteAdapter

@Adapter("Entity", "For all entity related interactions", App.VERSION)
/**
 * The Entity Adapter contains all the essential entries working with entities.
 * It allows you to create dynamic entities such as NPC's or Holograms.
 *
 * In most cases, it should be installed with Typewriter.
 * If you haven't installed Typewriter or the adapter yet,
 * please follow the [Installation Guide](../../docs/02-getting-started/01-installation.mdx)
 * first.
 */
object EntityAdapter : TypewriteAdapter() {
    override fun initialize() {

    }
}
