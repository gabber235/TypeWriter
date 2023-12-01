package me.gabber235.typewriter.entries.action

import me.gabber235.typewriter.adapters.Colors
import me.gabber235.typewriter.adapters.Entry
import me.gabber235.typewriter.adapters.modifiers.Help
import me.gabber235.typewriter.entry.Criteria
import me.gabber235.typewriter.entry.Modifier
import me.gabber235.typewriter.entry.entries.ActionEntry
import me.gabber235.typewriter.utils.Icons
import me.gabber235.typewriter.utils.SoundId
import net.kyori.adventure.sound.Sound
import org.bukkit.Location
import org.bukkit.entity.Player
import java.util.*

@Entry("play_sound", "Play sound at player, or location", Colors.RED, Icons.MUSIC)
/**
 * The `Play Sound Action` is an action that plays a sound for the player. This action provides you with the ability to play any sound that is available in Minecraft, at a specified location.
 *
 * ## How could this be used?
 *
 * This action can be useful in a variety of situations. You can use it to provide audio feedback to players, such as when they successfully complete a challenge, or to create ambiance in your Minecraft world, such as by playing background music or sound effects. The possibilities are endless!
 */
class PlaySoundActionEntry(
    override val id: String = "",
    override val name: String = "",
    override val criteria: List<Criteria> = emptyList(),
    override val modifiers: List<Modifier> = emptyList(),
    override val triggers: List<String> = emptyList(),
    @Help("The sound to play.")
    val sound: SoundId = SoundId.EMPTY,
    @Help("The location to play the sound from. (Defaults to player's location)")
    // The location to play the sound at. If this field is left blank, the sound will be played at the location of the player triggering the action.
    val location: Optional<Location> = Optional.empty(),
    @Help("The volume of the sound.")
    val volume: Float = 1.0f,
    @Help("The pitch of the sound.")
    val pitch: Float = 1.0f,
) : ActionEntry {
    override fun execute(player: Player) {
        super.execute(player)

        val key = this.sound.namespacedKey ?: return
        val sound = Sound.sound(key, Sound.Source.MASTER, volume, pitch)

        if (location.isPresent) {
            val location = location.get()
            player.playSound(sound, location.x, location.y, location.z)
        } else {
            player.playSound(sound)
        }
    }
}