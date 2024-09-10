package com.typewritermc.basic.entries.fact

import com.typewritermc.core.books.pages.Colors
import com.typewritermc.core.extension.annotations.Entry
import com.typewritermc.core.entries.Ref
import com.typewritermc.core.entries.emptyRef
import com.typewritermc.engine.paper.entry.entries.ExpirableFactEntry
import com.typewritermc.engine.paper.entry.entries.GroupEntry
import com.typewritermc.engine.paper.entry.entries.PersistableFactEntry
import com.typewritermc.engine.paper.facts.FactData
import com.typewritermc.engine.paper.facts.FactId
import java.time.Duration
import java.time.LocalDateTime
import kotlin.math.max

@Entry(
    "countdown_fact",
    "A fact that counts down from the set value",
    Colors.BLUE,
    "material-symbols:person-pin"
)
/**
 * The `Cooldown Fact` is a fact reflects the time since the last set value.
 *
 * When the value is set, it will count every second down from the set value.
 *
 * Suppose the value is set to 10.
 * Then after 3 seconds, the value will be 7.
 *
 * The countdown will continue regardless if the player is online/offline or if the server is online/offline.
 *
 * ## How could this be used?
 * This can be used to create a cooldown on a specific action.
 * For example, daily rewards that the player can only get once a day.
 */
class CountdownFact(
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