package me.gabber235.typewriter.facts

import com.google.gson.annotations.SerializedName
import me.gabber235.typewriter.entry.Entry
import me.gabber235.typewriter.entry.EntryDatabase
import me.gabber235.typewriter.utils.CronExpression
import me.gabber235.typewriter.utils.DurationParser
import java.time.LocalDateTime
import java.util.*
import kotlin.time.Duration
import kotlin.time.toJavaDuration

data class FactEntry(
	override val id: String = "",
	override val name: String = "",
	val comment: String = "",
	val lifetime: FactLifetime = FactLifetime.PERMANENT,
	val data: String = "",
) : Entry {
	val cache: Any? by lazy {
		try {
			when (lifetime) {
				FactLifetime.TIMED -> DurationParser.parse(data)
				FactLifetime.CRON  -> CronExpression.createDynamic(data)
				else               -> null
			}
		} catch (e: Exception) {
			null
		}
	}
}


val FactEntry.formattedName: String
	get() = name.split(".")
		.joinToString(" | ") { part -> part.replaceFirstChar { if (it.isLowerCase()) it.titlecase(Locale.getDefault()) else it.toString() } }
		.split("_")
		.joinToString(" ") { part -> part.replaceFirstChar { if (it.isLowerCase()) it.titlecase(Locale.getDefault()) else it.toString() } }

enum class FactLifetime {
	@SerializedName("permanent")
	PERMANENT,  // Saved permanently, it never gets removed

	@SerializedName("cron")
	CRON,       // Saved until a specified date, like (0 0 * * 1)

	@SerializedName("timed")
	TIMED,      // Saved for a specified duration, like 20 minutes

	@SerializedName("session")
	SESSION,    // Saved until a player logouts of the server
}

data class Fact(val id: String, val value: Int, val lastUpdate: LocalDateTime = LocalDateTime.now()) {
	override fun equals(other: Any?): Boolean {
		if (this === other) return true
		if (other !is Fact) return false

		if (id != other.id) return false

		return true
	}

	override fun hashCode(): Int = id.hashCode()
}

val Fact.hasExpired: Boolean
	get() {
		val entry = EntryDatabase.getFact(id) ?: return true
		return when (val cache = entry.cache) {
			is Duration       -> {
				LocalDateTime.now().isAfter(lastUpdate.plus(cache.toJavaDuration()))
			}

			is CronExpression -> {
				cache.nextLocalDateTimeAfter(lastUpdate).isBefore(LocalDateTime.now())
			}

			else              -> false
		}
	}