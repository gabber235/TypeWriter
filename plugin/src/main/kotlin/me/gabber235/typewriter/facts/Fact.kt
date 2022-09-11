package me.gabber235.typewriter.facts

import com.google.gson.annotations.SerializedName

data class FactEntry(
	val id: String,
	val name: String,
	val lifetime: FactLifetime,
	val data: String,
)

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

data class Fact(val name: String, val value: Int) {
	override fun equals(other: Any?): Boolean {
		if (this === other) return true
		if (other !is Fact) return false

		if (name != other.name) return false

		return true
	}

	override fun hashCode(): Int = name.hashCode()
}