package me.gabber235.typewriter.facts

import com.google.gson.annotations.SerializedName
import me.gabber235.typewriter.entry.Entry
import java.time.LocalDateTime
import java.util.*

data class FactEntry(
	override val id: String,
	override val name: String,
	val lifetime: FactLifetime,
	val data: String,
) : Entry


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

	@SerializedName("server")
	SERVER,     // Saved until the shutdown of a server

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