package com.typewritermc.engine.paper.entry

import com.typewritermc.core.entries.Entry
import com.typewritermc.core.extension.annotations.Tags
import org.bukkit.entity.Player

@Tags("placeholder")
interface PlaceholderEntry : Entry {
    fun display(player: Player?): String?
}
