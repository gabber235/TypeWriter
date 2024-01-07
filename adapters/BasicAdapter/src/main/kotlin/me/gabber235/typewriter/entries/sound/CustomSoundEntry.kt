package me.gabber235.typewriter.entries.sound

import me.gabber235.typewriter.adapters.Colors
import me.gabber235.typewriter.adapters.Entry
import me.gabber235.typewriter.entry.entries.SoundIdEntry
import me.gabber235.typewriter.utils.Icons

@Entry("custom_sound", "A custom sound", Colors.ORANGE, Icons.VOLUME_HIGH)
/**
 * The `Custom Sound Entry` is an entry that allow you to add sounds from a resource pack.
 * And use it in other entries.
 *
 * ## How could this be used?
 * For npc's, for example, you can add a custom sound to the npc, every time the npc talks, the sound will play.
 * Or you can use it during cinematics where a ncp talks.
 */
class CustomSoundEntry(
    override val id: String,
    override val name: String,
    override val soundId: String,
) : SoundIdEntry