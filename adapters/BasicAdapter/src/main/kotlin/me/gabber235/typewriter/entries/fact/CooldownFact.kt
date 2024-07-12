package me.gabber235.typewriter.entries.fact

import me.gabber235.typewriter.adapters.Colors
import me.gabber235.typewriter.adapters.Entry
import me.gabber235.typewriter.entry.Ref
import me.gabber235.typewriter.entry.emptyRef
import me.gabber235.typewriter.entry.entries.ExpirableFactEntry
import me.gabber235.typewriter.entry.entries.GroupEntry
import me.gabber235.typewriter.entry.entries.PersistableFactEntry
import me.gabber235.typewriter.facts.FactData
import me.gabber235.typewriter.facts.FactId
import java.time.Duration
import java.time.LocalDateTime
import kotlin.math.max

@Entry(
    "cooldown_fact",
    "Cooldown fact for tracking time also when player is offline",
    Colors.BLUE,
    "material-symbols:person-pin"
)
class CooldownFact(
    override val id: String = "",
    override val name: String = "",
    override val comment: String = "",
    override val group: Ref<GroupEntry> = emptyRef(),
) : PersistableFactEntry, ExpirableFactEntry {

    override fun read(id: FactId): FactData {
        val data = super<PersistableFactEntry>.read(id)
        return FactData(calculateValue(data), data.lastUpdate)
    }

    override fun hasExpired(id: FactId, data: FactData): Boolean {
        return calculateValue(data) == 0
    }

    private fun calculateValue(data: FactData): Int {
        val timeDifference = Duration.between(data.lastUpdate, LocalDateTime.now())
        return max(0, (data.value - timeDifference.seconds).toInt())
    }
}