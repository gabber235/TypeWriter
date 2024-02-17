package me.gabber235.typewriter.entry.entries

import me.gabber235.typewriter.adapters.Tags
import me.gabber235.typewriter.adapters.modifiers.Help
import me.gabber235.typewriter.entry.PlaceholderEntry
import me.gabber235.typewriter.entry.StaticEntry
import me.gabber235.typewriter.utils.Sound
import org.bukkit.entity.Player

@Tags("entity")
interface EntityEntry : StaticEntry

@Tags("speaker")
interface SpeakerEntry : EntityEntry, PlaceholderEntry {
    @Help("The name of the entity that will be displayed in the chat (e.g. 'Steve' or 'Alex').")
    val displayName: String

    @Help("The sound that will be played when the entity speaks.")
    val sound: Sound

    override fun display(player: Player?): String? = displayName
}

@Tags("npc")
interface Npc : SpeakerEntry, SoundSourceEntry
