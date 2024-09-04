package com.typewritermc.utils

import com.typewritermc.engine.paper.utils.DurationParser
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test

internal class DurationParserTest {

	@Test
	fun `When a single duration is provided expect it to be parsed correctly`() {
		Assertions.assertEquals(1, DurationParser.parse("1ms").inWholeMilliseconds)
		Assertions.assertEquals(123, DurationParser.parse("123sec").inWholeSeconds)
		Assertions.assertEquals(52, DurationParser.parse("52 m").inWholeMinutes)
		Assertions.assertEquals(23, DurationParser.parse("23hr").inWholeHours)
		Assertions.assertEquals(31, DurationParser.parse("31d").inWholeDays)
		Assertions.assertEquals(12, DurationParser.parse("12 w").inWholeDays / 7)
		Assertions.assertEquals(5, DurationParser.parse("5 months").inWholeDays / 30)
		Assertions.assertEquals(2, DurationParser.parse("2yr").inWholeDays / 365)
	}

	@Test
	fun `When multiple durations are provided expect the sum of the durations as result`() {
		Assertions.assertEquals(
			123 + 52 * 60 + 23 * 60 * 60 + 31 * 24 * 60 * 60 + 12 * 7 * 24 * 60 * 60 + 5 * 30 * 24 * 60 * 60 + 2 * 365 * 24 * 60 * 60,
			DurationParser.parse("123sec 52 m 23hr 31d 12 w 5 months 2yr").inWholeSeconds
		)

		Assertions.assertEquals(
			60 + 20,
			DurationParser.parse("1 hr 20 mins").inWholeMinutes
		)

		Assertions.assertEquals(
			60 + 20,
			DurationParser.parse("1h20m0s").inWholeMinutes
		)
	}

	@Test
	fun `When a duration string contains other accepted characters expect it to parse`() {
		Assertions.assertEquals(
			27681,
			DurationParser.parse("27,681 ms").inWholeMilliseconds
		)

		Assertions.assertEquals(
			27681,
			DurationParser.parse("27_681 milliseconds").inWholeMilliseconds
		)
	}

	@Test
	fun `When an empty duration string is given expect the resulting duration to be zero`() {
		Assertions.assertEquals(
			0,
			DurationParser.parse("").inWholeSeconds
		)
	}
}