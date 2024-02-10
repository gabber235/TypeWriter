package me.gabber235.typewriter.entries.action

import me.gabber235.typewriter.adapters.Colors
import me.gabber235.typewriter.adapters.Entry
import me.gabber235.typewriter.adapters.modifiers.Help
import me.gabber235.typewriter.entry.Criteria
import me.gabber235.typewriter.entry.Modifier
import me.gabber235.typewriter.entry.Ref
import me.gabber235.typewriter.entry.TriggerableEntry
import me.gabber235.typewriter.entry.entries.ActionEntry
import org.bukkit.entity.Player
import org.bukkit.potion.PotionEffect
import org.bukkit.potion.PotionEffectType

@Entry("add_potion_effect", "Add a potion effect to the player", Colors.RED, "fa6-solid:flask-vial")
/**
 * The `Add Potion Effect Action` is an action that adds a potion effect to the player.
 *
 * ## How could this be used?
 *
 * This action can be useful in a variety of situations. You can use it to provide players with buffs or debuffs, such as speed or slowness, or to create custom effects.
 */
class AddPotionEffectActionEntry(
    override val id: String = "",
    override val name: String = "",
    override val criteria: List<Criteria> = emptyList(),
    override val modifiers: List<Modifier> = emptyList(),
    override val triggers: List<Ref<TriggerableEntry>> = emptyList(),
    @Help("The potion effect to add.")
    val potionEffect: PotionEffectType = PotionEffectType.SPEED,
    @Help("The duration of the potion effect in ticks.")
    val duration: Int = 20,
    @Help("The amplifier of the potion effect.")
    val amplifier: Int = 1,
    @Help("Whether or not the effect is ambient")
    val ambient: Boolean = false,
    @Help("Whether or not to show the potion effect particles.")
    val particles: Boolean = true,
    @Help("Whether or not to show the potion effect icon in the player's inventory.")
    val icon: Boolean = true,
) : ActionEntry {
    override fun execute(player: Player) {
        super.execute(player)

        val potion = PotionEffect(potionEffect, duration, amplifier, ambient, particles, icon)
        player.addPotionEffect(potion)

    }
}