package com.typewritermc.basic.entries.audience

import com.typewritermc.core.books.pages.Colors
import com.typewritermc.core.extension.annotations.Entry
import com.typewritermc.engine.paper.entry.entries.AudienceDisplay
import com.typewritermc.engine.paper.entry.entries.AudienceEntry
import org.bukkit.WeatherType
import org.bukkit.entity.Player
import org.bukkit.event.EventHandler
import org.bukkit.event.player.PlayerChangedWorldEvent

@Entry("weather_audience", "Display certain weather to the player", Colors.GREEN, "fluent:weather-rain-showers-day-24-filled")
/**
 * The `Weather Audience` entry is an audience filter that displays certain weather to the player.
 *
 * ## How could this be used?
 * This could be used to conditionally have it rain for the player.
 */
class WeatherAudienceEntry(
    override val id: String = "",
    override val name: String = "",
    val weather: WeatherType = WeatherType.DOWNFALL,
    ) : AudienceEntry {
    override fun display(): AudienceDisplay = WeatherAudienceDisplay(weather)
}

class WeatherAudienceDisplay(
    private val weather: WeatherType,
) : AudienceDisplay() {
    override fun onPlayerAdd(player: Player) {
        player.setPlayerWeather(weather)
    }

    @EventHandler
    fun onWorldChange(event: PlayerChangedWorldEvent) {
        if (event.player !in this) return
        event.player.setPlayerWeather(weather)
    }

    override fun onPlayerRemove(player: Player) {
        player.resetPlayerWeather()
    }
}