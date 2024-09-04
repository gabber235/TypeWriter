package com.typewritermc.basic.entries.action

import com.typewritermc.core.books.pages.Colors
import com.typewritermc.core.entries.Ref
import com.typewritermc.core.extension.annotations.Default
import com.typewritermc.core.extension.annotations.Entry
import com.typewritermc.core.extension.annotations.Help
import com.typewritermc.engine.paper.entry.Criteria
import com.typewritermc.engine.paper.entry.Modifier
import com.typewritermc.engine.paper.entry.TriggerableEntry
import com.typewritermc.engine.paper.entry.entries.ActionEntry
import com.typewritermc.engine.paper.utils.ThreadType.SYNC
import com.typewritermc.engine.paper.utils.toTicks
import org.bukkit.entity.Player
import org.bukkit.potion.PotionEffect
import org.bukkit.potion.PotionEffectType
import java.time.Duration

@Entry(
    "add_potion_effect",
    "Add a potion effect to the player",
    Colors.RED,
    "fa6-solid:flask-vial"
)
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
    val potionEffect: PotionEffectType = PotionEffectType.SPEED,
    @Default("10000")
    val duration: Duration = Duration.ofSeconds(10),
    @Default("1")
    val amplifier: Int = 1,
    val ambient: Boolean = false,
    @Default("true")
    val particles: Boolean = true,
    @Help("Whether or not to show the potion effect icon in the player's inventory.")
    @Default("true")
    val icon: Boolean = true,
) : ActionEntry {
    override fun execute(player: Player) {
        super.execute(player)

        val potion = PotionEffect(potionEffect, duration.toTicks().toInt(), amplifier, ambient, particles, icon)
        SYNC.launch {
            player.addPotionEffect(potion)
        }
    }
}