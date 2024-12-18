package com.typewritermc.basic.entries.cinematic

import com.typewritermc.core.books.pages.Colors
import com.typewritermc.core.extension.annotations.Default
import com.typewritermc.core.extension.annotations.Entry
import com.typewritermc.core.extension.annotations.Segments
import com.typewritermc.engine.paper.entry.Criteria
import com.typewritermc.engine.paper.entry.entries.CinematicAction
import com.typewritermc.engine.paper.entry.entries.CinematicEntry
import com.typewritermc.engine.paper.entry.entries.Segment
import com.typewritermc.engine.paper.entry.temporal.SimpleCinematicAction
import org.bukkit.WeatherType
import org.bukkit.entity.Player

@Entry(
    "game_weather_cinematic",
    "A cinematic that changes the weather",
    Colors.CYAN,
    "fluent:weather-rain-showers-day-24-filled"
)
/**
 * The `GameWeatherCinematicEntry` is an entry that changes the weather during a cinematic.
 *
 * ## How could this be used?
 * This can be used to simulate the weather changing during a cinematic.
 */
class GameWeatherCinematicEntry(
    override val id: String = "",
    override val name: String = "",
    override val criteria: List<Criteria> = emptyList(),
    @Segments(Colors.CYAN, "fluent:weather-rain-showers-day-24-filled")
    val segments: List<GameWeatherSegment> = emptyList(),
) : CinematicEntry {
    override fun create(player: Player): CinematicAction = GameWeatherCinematicAction(player, this)
}

data class GameWeatherSegment(
    override val startFrame: Int = 0,
    override val endFrame: Int = 0,
    @Default("\"DOWNFALL\"")
    val weather: WeatherType = WeatherType.DOWNFALL,
) : Segment

class GameWeatherCinematicAction(
    val player: Player,
    entry: GameWeatherCinematicEntry,
) : SimpleCinematicAction<GameWeatherSegment>() {
    override val segments: List<GameWeatherSegment> = entry.segments

    override suspend fun startSegment(segment: GameWeatherSegment) {
        super.startSegment(segment)
        player.setPlayerWeather(segment.weather)
    }

    override suspend fun stopSegment(segment: GameWeatherSegment) {
        super.stopSegment(segment)
        player.resetPlayerWeather()
    }
}