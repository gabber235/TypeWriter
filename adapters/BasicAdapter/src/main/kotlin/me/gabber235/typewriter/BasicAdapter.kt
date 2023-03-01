package me.gabber235.typewriter

import App
import me.gabber235.typewriter.adapters.Adapter
import me.gabber235.typewriter.adapters.TypewriteAdapter


@Adapter("Basic", "For all the most basic entries", App.VERSION)
object BasicAdapter : TypewriteAdapter()