package com.typewritermc.utils

import com.typewritermc.engine.paper.utils.CronExpression
import com.typewritermc.engine.paper.utils.CronExpression.*
import org.junit.jupiter.api.*
import org.junit.jupiter.api.Assertions.*
import java.time.*
import java.util.*

class CronExpressionTest {
	private var original: TimeZone? = null
	private var zoneId: ZoneId? = null

	@BeforeEach
	fun setUp() {
		original = TimeZone.getDefault()
		TimeZone.setDefault(TimeZone.getTimeZone("Europe/Oslo"))
		zoneId = TimeZone.getDefault().toZoneId()
	}

	@AfterEach
	fun tearDown() {
		TimeZone.setDefault(original)
	}

	@Test
	@Throws(Exception::class)
	fun shall_parse_number() {
		val field = SimpleField(CronFieldType.MINUTE, "5")
		assertPossibleValues(field, 5)
	}

	private fun assertPossibleValues(field: SimpleField, vararg values: Int) {
		val valid: Set<Int> = values.toSet()
		for (i in field.fieldType.from..field.fieldType.to) {
			val errorText = "$i:$valid"
			if (valid.contains(i)) {
				assertTrue(field.matches(i), errorText)
			} else {
				assertFalse(field.matches(i), errorText)
			}
		}
	}

	@Test
	@Throws(Exception::class)
	fun shall_parse_number_with_increment() {
		val field = SimpleField(CronFieldType.MINUTE, "0/15")
		assertPossibleValues(field, 0, 15, 30, 45)
	}

	@Test
	@Throws(Exception::class)
	fun shall_parse_range() {
		val field = SimpleField(CronFieldType.MINUTE, "5-10")
		assertPossibleValues(field, 5, 6, 7, 8, 9, 10)
	}

	@Test
	@Throws(Exception::class)
	fun shall_parse_range_with_increment() {
		val field = SimpleField(CronFieldType.MINUTE, "20-30/2")
		assertPossibleValues(field, 20, 22, 24, 26, 28, 30)
	}

	@Test
	@Throws(Exception::class)
	fun shall_parse_asterix() {
		val field = SimpleField(CronFieldType.DAY_OF_WEEK, "*")
		assertPossibleValues(field, 1, 2, 3, 4, 5, 6, 7)
	}

	@Test
	@Throws(Exception::class)
	fun shall_parse_asterix_with_increment() {
		val field = SimpleField(CronFieldType.DAY_OF_WEEK, "*/2")
		assertPossibleValues(field, 1, 3, 5, 7)
	}

	@Test
	@Throws(Exception::class)
	fun shall_ignore_field_in_day_of_week() {
		val field = DayOfWeekField("?")
		assertTrue(field.matches(ZonedDateTime.now().toLocalDate()), "day of week is ?")
	}

	@Test
	@Throws(Exception::class)
	fun shall_ignore_field_in_day_of_month() {
		val field = DayOfMonthField("?")
		assertTrue(field.matches(ZonedDateTime.now().toLocalDate()), "day of month is ?")
	}

	@Test
	fun shall_give_error_if_invalid_count_field() {
		assertThrows<IllegalArgumentException> {
			CronExpression("* 3 *")
		}
	}

	@Test
	fun shall_give_error_if_minute_field_ignored() {
		assertThrows<IllegalArgumentException> {
			val field = SimpleField(CronFieldType.MINUTE, "?")
			field.matches(1)
		}
	}

	@Test
	fun shall_give_error_if_hour_field_ignored() {
		assertThrows<IllegalArgumentException> {
			val field = SimpleField(CronFieldType.HOUR, "?")
			field.matches(1)
		}
	}

	@Test
	fun shall_give_error_if_month_field_ignored() {
		assertThrows<IllegalArgumentException> {
			val field = SimpleField(CronFieldType.MONTH, "?")
			field.matches(1)
		}
	}

	@Test
	@Throws(Exception::class)
	fun shall_give_last_day_of_month_in_leapyear() {
		val field = DayOfMonthField("L")
		assertTrue(field.matches(LocalDate.of(2012, 2, 29)), "day of month is L")
	}

	@Test
	@Throws(Exception::class)
	fun shall_give_last_day_of_month() {
		val field = DayOfMonthField("L")
		val now = YearMonth.now()
		assertTrue(
			field.matches(LocalDate.of(now.year, now.monthValue, now.lengthOfMonth())),
			"L matches to the last day of month"
		)
	}

	@Test
	@Throws(Exception::class)
	fun check_all() {
		val cronExpr = CronExpression("* * * * * *")
		var after: ZonedDateTime = ZonedDateTime.of(2012, 4, 10, 13, 0, 1, 0, zoneId)
		var expected: ZonedDateTime = ZonedDateTime.of(2012, 4, 10, 13, 0, 2, 0, zoneId)
		assertEquals(expected, cronExpr.nextTimeAfter(after))
		after = ZonedDateTime.of(2012, 4, 10, 13, 2, 0, 0, zoneId)
		expected = ZonedDateTime.of(2012, 4, 10, 13, 2, 1, 0, zoneId)
		assertEquals(expected, cronExpr.nextTimeAfter(after))
		after = ZonedDateTime.of(2012, 4, 10, 13, 59, 59, 0, zoneId)
		expected = ZonedDateTime.of(2012, 4, 10, 14, 0, 0, 0, zoneId)
		assertEquals(expected, cronExpr.nextTimeAfter(after))
	}

	@Test
	fun check_invalid_input() {
		assertThrows<IllegalArgumentException> {
			CronExpression("")
		}
	}

	@Test
	@Throws(Exception::class)
	fun check_second_number() {
		val cronExpr = CronExpression("3 * * * * *")
		var after: ZonedDateTime = ZonedDateTime.of(2012, 4, 10, 13, 1, 0, 0, zoneId)
		var expected: ZonedDateTime = ZonedDateTime.of(2012, 4, 10, 13, 1, 3, 0, zoneId)
		assertEquals(expected, cronExpr.nextTimeAfter(after))
		after = ZonedDateTime.of(2012, 4, 10, 13, 1, 3, 0, zoneId)
		expected = ZonedDateTime.of(2012, 4, 10, 13, 2, 3, 0, zoneId)
		assertEquals(expected, cronExpr.nextTimeAfter(after))
		after = ZonedDateTime.of(2012, 4, 10, 13, 59, 3, 0, zoneId)
		expected = ZonedDateTime.of(2012, 4, 10, 14, 0, 3, 0, zoneId)
		assertEquals(expected, cronExpr.nextTimeAfter(after))
		after = ZonedDateTime.of(2012, 4, 10, 23, 59, 3, 0, zoneId)
		expected = ZonedDateTime.of(2012, 4, 11, 0, 0, 3, 0, zoneId)
		assertEquals(expected, cronExpr.nextTimeAfter(after))
		after = ZonedDateTime.of(2012, 4, 30, 23, 59, 3, 0, zoneId)
		expected = ZonedDateTime.of(2012, 5, 1, 0, 0, 3, 0, zoneId)
		assertEquals(expected, cronExpr.nextTimeAfter(after))
	}

	@Test
	@Throws(Exception::class)
	fun check_second_increment() {
		val cronExpr = CronExpression("5/15 * * * * *")
		var after: ZonedDateTime = ZonedDateTime.of(2012, 4, 10, 13, 0, 0, 0, zoneId)
		var expected: ZonedDateTime = ZonedDateTime.of(2012, 4, 10, 13, 0, 5, 0, zoneId)
		assertEquals(expected, cronExpr.nextTimeAfter(after))
		after = ZonedDateTime.of(2012, 4, 10, 13, 0, 5, 0, zoneId)
		expected = ZonedDateTime.of(2012, 4, 10, 13, 0, 20, 0, zoneId)
		assertEquals(expected, cronExpr.nextTimeAfter(after))
		after = ZonedDateTime.of(2012, 4, 10, 13, 0, 20, 0, zoneId)
		expected = ZonedDateTime.of(2012, 4, 10, 13, 0, 35, 0, zoneId)
		assertEquals(expected, cronExpr.nextTimeAfter(after))
		after = ZonedDateTime.of(2012, 4, 10, 13, 0, 35, 0, zoneId)
		expected = ZonedDateTime.of(2012, 4, 10, 13, 0, 50, 0, zoneId)
		assertEquals(expected, cronExpr.nextTimeAfter(after))
		after = ZonedDateTime.of(2012, 4, 10, 13, 0, 50, 0, zoneId)
		expected = ZonedDateTime.of(2012, 4, 10, 13, 1, 5, 0, zoneId)
		assertEquals(expected, cronExpr.nextTimeAfter(after))

		// if rolling over minute then reset second (cron rules - increment affects only values in own field)
		after = ZonedDateTime.of(2012, 4, 10, 13, 0, 50, 0, zoneId)
		expected = ZonedDateTime.of(2012, 4, 10, 13, 1, 10, 0, zoneId)
		assertTrue(CronExpression("10/100 * * * * *").nextTimeAfter(after) == expected)
		after = ZonedDateTime.of(2012, 4, 10, 13, 1, 10, 0, zoneId)
		expected = ZonedDateTime.of(2012, 4, 10, 13, 2, 10, 0, zoneId)
		assertTrue(CronExpression("10/100 * * * * *").nextTimeAfter(after) == expected)
	}

	@Test
	@Throws(Exception::class)
	fun check_second_list() {
		val cronExpr = CronExpression("7,19 * * * * *")
		var after: ZonedDateTime = ZonedDateTime.of(2012, 4, 10, 13, 0, 0, 0, zoneId)
		var expected: ZonedDateTime = ZonedDateTime.of(2012, 4, 10, 13, 0, 7, 0, zoneId)
		assertEquals(expected, cronExpr.nextTimeAfter(after))
		after = ZonedDateTime.of(2012, 4, 10, 13, 0, 7, 0, zoneId)
		expected = ZonedDateTime.of(2012, 4, 10, 13, 0, 19, 0, zoneId)
		assertEquals(expected, cronExpr.nextTimeAfter(after))
		after = ZonedDateTime.of(2012, 4, 10, 13, 0, 19, 0, zoneId)
		expected = ZonedDateTime.of(2012, 4, 10, 13, 1, 7, 0, zoneId)
		assertEquals(expected, cronExpr.nextTimeAfter(after))
	}

	@Test
	@Throws(Exception::class)
	fun check_second_range() {
		val cronExpr = CronExpression("42-45 * * * * *")
		var after: ZonedDateTime = ZonedDateTime.of(2012, 4, 10, 13, 0, 0, 0, zoneId)
		var expected: ZonedDateTime = ZonedDateTime.of(2012, 4, 10, 13, 0, 42, 0, zoneId)
		assertEquals(expected, cronExpr.nextTimeAfter(after))
		after = ZonedDateTime.of(2012, 4, 10, 13, 0, 42, 0, zoneId)
		expected = ZonedDateTime.of(2012, 4, 10, 13, 0, 43, 0, zoneId)
		assertEquals(expected, cronExpr.nextTimeAfter(after))
		after = ZonedDateTime.of(2012, 4, 10, 13, 0, 43, 0, zoneId)
		expected = ZonedDateTime.of(2012, 4, 10, 13, 0, 44, 0, zoneId)
		assertEquals(expected, cronExpr.nextTimeAfter(after))
		after = ZonedDateTime.of(2012, 4, 10, 13, 0, 44, 0, zoneId)
		expected = ZonedDateTime.of(2012, 4, 10, 13, 0, 45, 0, zoneId)
		assertEquals(expected, cronExpr.nextTimeAfter(after))
		after = ZonedDateTime.of(2012, 4, 10, 13, 0, 45, 0, zoneId)
		expected = ZonedDateTime.of(2012, 4, 10, 13, 1, 42, 0, zoneId)
		assertEquals(expected, cronExpr.nextTimeAfter(after))
	}

	@Test
	fun check_second_invalid_range() {
		assertThrows<IllegalArgumentException> {
			CronExpression("42-63 * * * * *")
		}
	}

	@Test
	@Throws(IllegalArgumentException::class)
	fun check_second_invalid_increment_modifier() {
		assertThrows<IllegalArgumentException> {

			CronExpression("42#3 * * * * *")
		}
	}

	@Test
	@Throws(Exception::class)
	fun check_minute_number() {
		val cronExpr = CronExpression("0 3 * * * *")
		var after: ZonedDateTime = ZonedDateTime.of(2012, 4, 10, 13, 1, 0, 0, zoneId)
		var expected: ZonedDateTime = ZonedDateTime.of(2012, 4, 10, 13, 3, 0, 0, zoneId)
		assertEquals(expected, cronExpr.nextTimeAfter(after))
		after = ZonedDateTime.of(2012, 4, 10, 13, 3, 0, 0, zoneId)
		expected = ZonedDateTime.of(2012, 4, 10, 14, 3, 0, 0, zoneId)
		assertEquals(expected, cronExpr.nextTimeAfter(after))
	}

	@Test
	@Throws(Exception::class)
	fun check_minute_increment() {
		val cronExpr = CronExpression("0 0/15 * * * *")
		var after: ZonedDateTime = ZonedDateTime.of(2012, 4, 10, 13, 0, 0, 0, zoneId)
		var expected: ZonedDateTime = ZonedDateTime.of(2012, 4, 10, 13, 15, 0, 0, zoneId)
		assertEquals(expected, cronExpr.nextTimeAfter(after))
		after = ZonedDateTime.of(2012, 4, 10, 13, 15, 0, 0, zoneId)
		expected = ZonedDateTime.of(2012, 4, 10, 13, 30, 0, 0, zoneId)
		assertEquals(expected, cronExpr.nextTimeAfter(after))
		after = ZonedDateTime.of(2012, 4, 10, 13, 30, 0, 0, zoneId)
		expected = ZonedDateTime.of(2012, 4, 10, 13, 45, 0, 0, zoneId)
		assertEquals(expected, cronExpr.nextTimeAfter(after))
		after = ZonedDateTime.of(2012, 4, 10, 13, 45, 0, 0, zoneId)
		expected = ZonedDateTime.of(2012, 4, 10, 14, 0, 0, 0, zoneId)
		assertEquals(expected, cronExpr.nextTimeAfter(after))
	}

	@Test
	@Throws(Exception::class)
	fun check_minute_list() {
		val cronExpr = CronExpression("0 7,19 * * * *")
		var after: ZonedDateTime = ZonedDateTime.of(2012, 4, 10, 13, 0, 0, 0, zoneId)
		var expected: ZonedDateTime = ZonedDateTime.of(2012, 4, 10, 13, 7, 0, 0, zoneId)
		assertEquals(expected, cronExpr.nextTimeAfter(after))
		after = ZonedDateTime.of(2012, 4, 10, 13, 7, 0, 0, zoneId)
		expected = ZonedDateTime.of(2012, 4, 10, 13, 19, 0, 0, zoneId)
		assertEquals(expected, cronExpr.nextTimeAfter(after))
	}

	@Test
	@Throws(Exception::class)
	fun check_hour_number() {
		val cronExpr = CronExpression("0 * 3 * * *")
		var after: ZonedDateTime = ZonedDateTime.of(2012, 4, 10, 13, 1, 0, 0, zoneId)
		var expected: ZonedDateTime = ZonedDateTime.of(2012, 4, 11, 3, 0, 0, 0, zoneId)
		assertEquals(expected, cronExpr.nextTimeAfter(after))
		after = ZonedDateTime.of(2012, 4, 11, 3, 0, 0, 0, zoneId)
		expected = ZonedDateTime.of(2012, 4, 11, 3, 1, 0, 0, zoneId)
		assertEquals(expected, cronExpr.nextTimeAfter(after))
		after = ZonedDateTime.of(2012, 4, 11, 3, 59, 0, 0, zoneId)
		expected = ZonedDateTime.of(2012, 4, 12, 3, 0, 0, 0, zoneId)
		assertEquals(expected, cronExpr.nextTimeAfter(after))
	}

	@Test
	@Throws(Exception::class)
	fun check_hour_increment() {
		val cronExpr = CronExpression("0 * 0/15 * * *")
		var after: ZonedDateTime = ZonedDateTime.of(2012, 4, 10, 13, 0, 0, 0, zoneId)
		var expected: ZonedDateTime = ZonedDateTime.of(2012, 4, 10, 15, 0, 0, 0, zoneId)
		assertEquals(expected, cronExpr.nextTimeAfter(after))
		after = ZonedDateTime.of(2012, 4, 10, 15, 0, 0, 0, zoneId)
		expected = ZonedDateTime.of(2012, 4, 10, 15, 1, 0, 0, zoneId)
		assertEquals(expected, cronExpr.nextTimeAfter(after))
		after = ZonedDateTime.of(2012, 4, 10, 15, 59, 0, 0, zoneId)
		expected = ZonedDateTime.of(2012, 4, 11, 0, 0, 0, 0, zoneId)
		assertEquals(expected, cronExpr.nextTimeAfter(after))
		after = ZonedDateTime.of(2012, 4, 11, 0, 0, 0, 0, zoneId)
		expected = ZonedDateTime.of(2012, 4, 11, 0, 1, 0, 0, zoneId)
		assertEquals(expected, cronExpr.nextTimeAfter(after))
		after = ZonedDateTime.of(2012, 4, 11, 15, 0, 0, 0, zoneId)
		expected = ZonedDateTime.of(2012, 4, 11, 15, 1, 0, 0, zoneId)
		assertEquals(expected, cronExpr.nextTimeAfter(after))
	}

	@Test
	@Throws(Exception::class)
	fun check_hour_list() {
		val cronExpr = CronExpression("0 * 7,19 * * *")
		var after: ZonedDateTime = ZonedDateTime.of(2012, 4, 10, 13, 0, 0, 0, zoneId)
		var expected: ZonedDateTime = ZonedDateTime.of(2012, 4, 10, 19, 0, 0, 0, zoneId)
		assertEquals(expected, cronExpr.nextTimeAfter(after))
		after = ZonedDateTime.of(2012, 4, 10, 19, 0, 0, 0, zoneId)
		expected = ZonedDateTime.of(2012, 4, 10, 19, 1, 0, 0, zoneId)
		assertEquals(expected, cronExpr.nextTimeAfter(after))
		after = ZonedDateTime.of(2012, 4, 10, 19, 59, 0, 0, zoneId)
		expected = ZonedDateTime.of(2012, 4, 11, 7, 0, 0, 0, zoneId)
		assertEquals(expected, cronExpr.nextTimeAfter(after))
	}

	@Test
	@Throws(Exception::class)
	fun check_hour_shall_run_25_times_in_DST_change_to_wintertime() {
		val cron = CronExpression("0 1 * * * *")
		val start: ZonedDateTime = ZonedDateTime.of(2011, 10, 30, 0, 0, 0, 0, zoneId)
		val slutt: ZonedDateTime = start.plusDays(1)
		var tid: ZonedDateTime = start

		// throws: Unsupported unit: Seconds
		// assertEquals(25, Duration.between(start.toLocalDate(), slutt.toLocalDate()).toHours());
		var count = 0
		var lastTime: ZonedDateTime? = tid
		while (tid.isBefore(slutt)) {
			val nextTime: ZonedDateTime = cron.nextTimeAfter(tid)
			assertTrue(nextTime.isAfter(lastTime))
			lastTime = nextTime
			tid = tid.plusHours(1)
			count++
		}
		assertEquals(25, count)
	}

	@Test
	@Throws(Exception::class)
	fun check_hour_shall_run_23_times_in_DST_change_to_summertime() {
		val cron = CronExpression("0 0 * * * *")
		val start: ZonedDateTime = ZonedDateTime.of(2011, 3, 27, 1, 0, 0, 0, zoneId)
		val slutt: ZonedDateTime = start.plusDays(1)
		var tid: ZonedDateTime = start

		// throws: Unsupported unit: Seconds
		// assertEquals(23, Duration.between(start.toLocalDate(), slutt.toLocalDate()).toHours());
		var count = 0
		var lastTime: ZonedDateTime? = tid
		while (tid.isBefore(slutt)) {
			val nextTime: ZonedDateTime = cron.nextTimeAfter(tid)
			assertTrue(nextTime.isAfter(lastTime))
			lastTime = nextTime
			tid = tid.plusHours(1)
			count++
		}
		assertEquals(23, count)
	}

	@Test
	@Throws(Exception::class)
	fun check_dayOfMonth_number() {
		val cronExpr = CronExpression("0 * * 3 * *")
		var after: ZonedDateTime = ZonedDateTime.of(2012, 4, 10, 13, 0, 0, 0, zoneId)
		var expected: ZonedDateTime = ZonedDateTime.of(2012, 5, 3, 0, 0, 0, 0, zoneId)
		assertEquals(expected, cronExpr.nextTimeAfter(after))
		after = ZonedDateTime.of(2012, 5, 3, 0, 0, 0, 0, zoneId)
		expected = ZonedDateTime.of(2012, 5, 3, 0, 1, 0, 0, zoneId)
		assertEquals(expected, cronExpr.nextTimeAfter(after))
		after = ZonedDateTime.of(2012, 5, 3, 0, 59, 0, 0, zoneId)
		expected = ZonedDateTime.of(2012, 5, 3, 1, 0, 0, 0, zoneId)
		assertEquals(expected, cronExpr.nextTimeAfter(after))
		after = ZonedDateTime.of(2012, 5, 3, 23, 59, 0, 0, zoneId)
		expected = ZonedDateTime.of(2012, 6, 3, 0, 0, 0, 0, zoneId)
		assertEquals(expected, cronExpr.nextTimeAfter(after))
	}

	@Test
	@Throws(Exception::class)
	fun check_dayOfMonth_increment() {
		val cronExpr = CronExpression("0 0 0 1/15 * *")
		var after: ZonedDateTime = ZonedDateTime.of(2012, 4, 10, 13, 0, 0, 0, zoneId)
		var expected: ZonedDateTime = ZonedDateTime.of(2012, 4, 16, 0, 0, 0, 0, zoneId)
		assertEquals(expected, cronExpr.nextTimeAfter(after))
		after = ZonedDateTime.of(2012, 4, 16, 0, 0, 0, 0, zoneId)
		expected = ZonedDateTime.of(2012, 5, 1, 0, 0, 0, 0, zoneId)
		assertEquals(expected, cronExpr.nextTimeAfter(after))
		after = ZonedDateTime.of(2012, 4, 30, 0, 0, 0, 0, zoneId)
		assertEquals(expected, cronExpr.nextTimeAfter(after))
		after = ZonedDateTime.of(2012, 5, 1, 0, 0, 0, 0, zoneId)
		expected = ZonedDateTime.of(2012, 5, 16, 0, 0, 0, 0, zoneId)
		assertEquals(expected, cronExpr.nextTimeAfter(after))
	}

	@Test
	@Throws(Exception::class)
	fun check_dayOfMonth_list() {
		val cronExpr = CronExpression("0 0 0 7,19 * *")
		var after: ZonedDateTime = ZonedDateTime.of(2012, 4, 10, 13, 0, 0, 0, zoneId)
		var expected: ZonedDateTime = ZonedDateTime.of(2012, 4, 19, 0, 0, 0, 0, zoneId)
		assertEquals(expected, cronExpr.nextTimeAfter(after))
		after = ZonedDateTime.of(2012, 4, 19, 0, 0, 0, 0, zoneId)
		expected = ZonedDateTime.of(2012, 5, 7, 0, 0, 0, 0, zoneId)
		assertEquals(expected, cronExpr.nextTimeAfter(after))
		after = ZonedDateTime.of(2012, 5, 7, 0, 0, 0, 0, zoneId)
		expected = ZonedDateTime.of(2012, 5, 19, 0, 0, 0, 0, zoneId)
		assertEquals(expected, cronExpr.nextTimeAfter(after))
		after = ZonedDateTime.of(2012, 5, 30, 0, 0, 0, 0, zoneId)
		expected = ZonedDateTime.of(2012, 6, 7, 0, 0, 0, 0, zoneId)
		assertEquals(expected, cronExpr.nextTimeAfter(after))
	}

	@Test
	@Throws(Exception::class)
	fun check_dayOfMonth_last() {
		val cronExpr = CronExpression("0 0 0 L * *")
		var after: ZonedDateTime = ZonedDateTime.of(2012, 4, 10, 13, 0, 0, 0, zoneId)
		var expected: ZonedDateTime = ZonedDateTime.of(2012, 4, 30, 0, 0, 0, 0, zoneId)
		assertEquals(expected, cronExpr.nextTimeAfter(after))
		after = ZonedDateTime.of(2012, 2, 12, 0, 0, 0, 0, zoneId)
		expected = ZonedDateTime.of(2012, 2, 29, 0, 0, 0, 0, zoneId)
		assertEquals(expected, cronExpr.nextTimeAfter(after))
	}

	@Test
	@Throws(Exception::class)
	fun check_dayOfMonth_number_last_L() {
		val cronExpr = CronExpression("0 0 0 3L * *")
		var after: ZonedDateTime = ZonedDateTime.of(2012, 4, 10, 13, 0, 0, 0, zoneId)
		var expected: ZonedDateTime = ZonedDateTime.of(2012, 4, 30 - 3, 0, 0, 0, 0, zoneId)
		assertEquals(expected, cronExpr.nextTimeAfter(after))
		after = ZonedDateTime.of(2012, 2, 12, 0, 0, 0, 0, zoneId)
		expected = ZonedDateTime.of(2012, 2, 29 - 3, 0, 0, 0, 0, zoneId)
		assertEquals(expected, cronExpr.nextTimeAfter(after))
	}

	@Test
	@Throws(Exception::class)
	fun check_dayOfMonth_closest_weekday_W() {
		val cronExpr = CronExpression("0 0 0 9W * *")

		// 9 - is weekday in May
		var after: ZonedDateTime = ZonedDateTime.of(2012, 5, 2, 0, 0, 0, 0, zoneId)
		var expected: ZonedDateTime = ZonedDateTime.of(2012, 5, 9, 0, 0, 0, 0, zoneId)
		assertEquals(expected, cronExpr.nextTimeAfter(after))

		// 9 - is weekday in May
		after = ZonedDateTime.of(2012, 5, 8, 0, 0, 0, 0, zoneId)
		assertEquals(expected, cronExpr.nextTimeAfter(after))

		// 9 - saturday, friday the closest weekday in june
		after = ZonedDateTime.of(2012, 5, 9, 0, 0, 0, 0, zoneId)
		expected = ZonedDateTime.of(2012, 6, 8, 0, 0, 0, 0, zoneId)
		assertEquals(expected, cronExpr.nextTimeAfter(after))

		// 9 - sunday, monday the closest weekday in september
		after = ZonedDateTime.of(2012, 9, 1, 0, 0, 0, 0, zoneId)
		expected = ZonedDateTime.of(2012, 9, 10, 0, 0, 0, 0, zoneId)
		assertEquals(expected, cronExpr.nextTimeAfter(after))
	}

	@Test
	fun check_dayOfMonth_invalid_modifier() {
		assertThrows<IllegalArgumentException> {
			CronExpression("0 0 0 9X * *")
		}
	}

	@Test
	fun check_dayOfMonth_invalid_increment_modifier() {
		assertThrows<IllegalArgumentException> {
			CronExpression("0 0 0 9#2 * *")
		}
	}

	@Test
	@Throws(Exception::class)
	fun check_month_number() {
		val after: ZonedDateTime = ZonedDateTime.of(2012, 2, 12, 0, 0, 0, 0, zoneId)
		val expected: ZonedDateTime = ZonedDateTime.of(2012, 5, 1, 0, 0, 0, 0, zoneId)
		assertTrue(CronExpression("0 0 0 1 5 *").nextTimeAfter(after) == expected)
	}

	@Test
	@Throws(Exception::class)
	fun check_month_increment() {
		var after: ZonedDateTime = ZonedDateTime.of(2012, 2, 12, 0, 0, 0, 0, zoneId)
		var expected: ZonedDateTime = ZonedDateTime.of(2012, 5, 1, 0, 0, 0, 0, zoneId)
		assertTrue(CronExpression("0 0 0 1 5/2 *").nextTimeAfter(after) == expected)
		after = ZonedDateTime.of(2012, 5, 1, 0, 0, 0, 0, zoneId)
		expected = ZonedDateTime.of(2012, 7, 1, 0, 0, 0, 0, zoneId)
		assertTrue(CronExpression("0 0 0 1 5/2 *").nextTimeAfter(after) == expected)

		// if rolling over year then reset month field (cron rules - increments only affect own field)
		after = ZonedDateTime.of(2012, 5, 1, 0, 0, 0, 0, zoneId)
		expected = ZonedDateTime.of(2013, 5, 1, 0, 0, 0, 0, zoneId)
		assertTrue(CronExpression("0 0 0 1 5/10 *").nextTimeAfter(after) == expected)
	}

	@Test
	@Throws(Exception::class)
	fun check_month_list() {
		val cronExpr = CronExpression("0 0 0 1 3,7,12 *")
		var after: ZonedDateTime = ZonedDateTime.of(2012, 2, 12, 0, 0, 0, 0, zoneId)
		var expected: ZonedDateTime = ZonedDateTime.of(2012, 3, 1, 0, 0, 0, 0, zoneId)
		assertEquals(expected, cronExpr.nextTimeAfter(after))
		after = ZonedDateTime.of(2012, 3, 1, 0, 0, 0, 0, zoneId)
		expected = ZonedDateTime.of(2012, 7, 1, 0, 0, 0, 0, zoneId)
		assertEquals(expected, cronExpr.nextTimeAfter(after))
		after = ZonedDateTime.of(2012, 7, 1, 0, 0, 0, 0, zoneId)
		expected = ZonedDateTime.of(2012, 12, 1, 0, 0, 0, 0, zoneId)
		assertEquals(expected, cronExpr.nextTimeAfter(after))
	}

	@Test
	@Throws(Exception::class)
	fun check_month_list_by_name() {
		val cronExpr = CronExpression("0 0 0 1 MAR,JUL,DEC *")
		var after: ZonedDateTime = ZonedDateTime.of(2012, 2, 12, 0, 0, 0, 0, zoneId)
		var expected: ZonedDateTime = ZonedDateTime.of(2012, 3, 1, 0, 0, 0, 0, zoneId)
		assertEquals(expected, cronExpr.nextTimeAfter(after))
		after = ZonedDateTime.of(2012, 3, 1, 0, 0, 0, 0, zoneId)
		expected = ZonedDateTime.of(2012, 7, 1, 0, 0, 0, 0, zoneId)
		assertEquals(expected, cronExpr.nextTimeAfter(after))
		after = ZonedDateTime.of(2012, 7, 1, 0, 0, 0, 0, zoneId)
		expected = ZonedDateTime.of(2012, 12, 1, 0, 0, 0, 0, zoneId)
		assertEquals(expected, cronExpr.nextTimeAfter(after))
	}

	@Test
	fun check_month_invalid_modifier() {
		assertThrows<IllegalArgumentException> {
			CronExpression("0 0 0 1 ? *")
		}
	}

	@Test
	@Throws(Exception::class)
	fun check_dayOfWeek_number() {
		val cronExpr = CronExpression("0 0 0 * * 3")
		var after: ZonedDateTime = ZonedDateTime.of(2012, 4, 1, 0, 0, 0, 0, zoneId)
		var expected: ZonedDateTime = ZonedDateTime.of(2012, 4, 4, 0, 0, 0, 0, zoneId)
		assertEquals(expected, cronExpr.nextTimeAfter(after))
		after = ZonedDateTime.of(2012, 4, 4, 0, 0, 0, 0, zoneId)
		expected = ZonedDateTime.of(2012, 4, 11, 0, 0, 0, 0, zoneId)
		assertEquals(expected, cronExpr.nextTimeAfter(after))
		after = ZonedDateTime.of(2012, 4, 12, 0, 0, 0, 0, zoneId)
		expected = ZonedDateTime.of(2012, 4, 18, 0, 0, 0, 0, zoneId)
		assertEquals(expected, cronExpr.nextTimeAfter(after))
		after = ZonedDateTime.of(2012, 4, 18, 0, 0, 0, 0, zoneId)
		expected = ZonedDateTime.of(2012, 4, 25, 0, 0, 0, 0, zoneId)
		assertEquals(expected, cronExpr.nextTimeAfter(after))
	}

	@Test
	@Throws(Exception::class)
	fun check_dayOfWeek_increment() {
		val cronExpr = CronExpression("0 0 0 * * 3/2")
		var after: ZonedDateTime = ZonedDateTime.of(2012, 4, 1, 0, 0, 0, 0, zoneId)
		var expected: ZonedDateTime = ZonedDateTime.of(2012, 4, 4, 0, 0, 0, 0, zoneId)
		assertEquals(expected, cronExpr.nextTimeAfter(after))
		after = ZonedDateTime.of(2012, 4, 4, 0, 0, 0, 0, zoneId)
		expected = ZonedDateTime.of(2012, 4, 6, 0, 0, 0, 0, zoneId)
		assertEquals(expected, cronExpr.nextTimeAfter(after))
		after = ZonedDateTime.of(2012, 4, 6, 0, 0, 0, 0, zoneId)
		expected = ZonedDateTime.of(2012, 4, 8, 0, 0, 0, 0, zoneId)
		assertEquals(expected, cronExpr.nextTimeAfter(after))
		after = ZonedDateTime.of(2012, 4, 8, 0, 0, 0, 0, zoneId)
		expected = ZonedDateTime.of(2012, 4, 11, 0, 0, 0, 0, zoneId)
		assertEquals(expected, cronExpr.nextTimeAfter(after))
	}

	@Test
	@Throws(Exception::class)
	fun check_dayOfWeek_list() {
		val cronExpr = CronExpression("0 0 0 * * 1,5,7")
		var after: ZonedDateTime = ZonedDateTime.of(2012, 4, 1, 0, 0, 0, 0, zoneId)
		var expected: ZonedDateTime = ZonedDateTime.of(2012, 4, 2, 0, 0, 0, 0, zoneId)
		assertEquals(expected, cronExpr.nextTimeAfter(after))
		after = ZonedDateTime.of(2012, 4, 2, 0, 0, 0, 0, zoneId)
		expected = ZonedDateTime.of(2012, 4, 6, 0, 0, 0, 0, zoneId)
		assertEquals(expected, cronExpr.nextTimeAfter(after))
		after = ZonedDateTime.of(2012, 4, 6, 0, 0, 0, 0, zoneId)
		expected = ZonedDateTime.of(2012, 4, 8, 0, 0, 0, 0, zoneId)
		assertEquals(expected, cronExpr.nextTimeAfter(after))
	}

	@Test
	@Throws(Exception::class)
	fun check_dayOfWeek_list_by_name() {
		val cronExpr = CronExpression("0 0 0 * * MON,FRI,SUN")
		var after: ZonedDateTime = ZonedDateTime.of(2012, 4, 1, 0, 0, 0, 0, zoneId)
		var expected: ZonedDateTime = ZonedDateTime.of(2012, 4, 2, 0, 0, 0, 0, zoneId)
		assertEquals(expected, cronExpr.nextTimeAfter(after))
		after = ZonedDateTime.of(2012, 4, 2, 0, 0, 0, 0, zoneId)
		expected = ZonedDateTime.of(2012, 4, 6, 0, 0, 0, 0, zoneId)
		assertEquals(expected, cronExpr.nextTimeAfter(after))
		after = ZonedDateTime.of(2012, 4, 6, 0, 0, 0, 0, zoneId)
		expected = ZonedDateTime.of(2012, 4, 8, 0, 0, 0, 0, zoneId)
		assertEquals(expected, cronExpr.nextTimeAfter(after))
	}

	@Test
	@Throws(Exception::class)
	fun check_dayOfWeek_last_friday_in_month() {
		val cronExpr = CronExpression("0 0 0 * * 5L")
		var after: ZonedDateTime = ZonedDateTime.of(2012, 4, 1, 1, 0, 0, 0, zoneId)
		var expected: ZonedDateTime = ZonedDateTime.of(2012, 4, 27, 0, 0, 0, 0, zoneId)
		assertEquals(expected, cronExpr.nextTimeAfter(after))
		after = ZonedDateTime.of(2012, 4, 27, 0, 0, 0, 0, zoneId)
		expected = ZonedDateTime.of(2012, 5, 25, 0, 0, 0, 0, zoneId)
		assertEquals(expected, cronExpr.nextTimeAfter(after))
		after = ZonedDateTime.of(2012, 2, 6, 0, 0, 0, 0, zoneId)
		expected = ZonedDateTime.of(2012, 2, 24, 0, 0, 0, 0, zoneId)
		assertEquals(expected, cronExpr.nextTimeAfter(after))
		after = ZonedDateTime.of(2012, 2, 6, 0, 0, 0, 0, zoneId)
		expected = ZonedDateTime.of(2012, 2, 24, 0, 0, 0, 0, zoneId)
		assertTrue(CronExpression("0 0 0 * * FRIL").nextTimeAfter(after) == expected)
	}

	@Test
	fun check_dayOfWeek_invalid_modifier() {
		assertThrows<IllegalArgumentException> {
			CronExpression("0 0 0 * * 5W")
		}
	}

	@Test
	fun check_dayOfWeek_invalid_increment_modifier() {
		assertThrows<IllegalArgumentException> {
			CronExpression("0 0 0 * * 5?3")
		}
	}

	@Test
	@Throws(Exception::class)
	fun check_dayOfWeek_shall_interpret_0_as_sunday() {
		val after: ZonedDateTime = ZonedDateTime.of(2012, 4, 1, 0, 0, 0, 0, zoneId)
		var expected: ZonedDateTime = ZonedDateTime.of(2012, 4, 8, 0, 0, 0, 0, zoneId)
		assertTrue(CronExpression("0 0 0 * * 0").nextTimeAfter(after) == expected)
		expected = ZonedDateTime.of(2012, 4, 29, 0, 0, 0, 0, zoneId)
		assertTrue(CronExpression("0 0 0 * * 0L").nextTimeAfter(after) == expected)
		expected = ZonedDateTime.of(2012, 4, 8, 0, 0, 0, 0, zoneId)
		assertTrue(CronExpression("0 0 0 * * 0#2").nextTimeAfter(after) == expected)
	}

	@Test
	@Throws(Exception::class)
	fun check_dayOfWeek_shall_interpret_7_as_sunday() {
		val after: ZonedDateTime = ZonedDateTime.of(2012, 4, 1, 0, 0, 0, 0, zoneId)
		var expected: ZonedDateTime = ZonedDateTime.of(2012, 4, 8, 0, 0, 0, 0, zoneId)
		assertTrue(CronExpression("0 0 0 * * 7").nextTimeAfter(after) == expected)
		expected = ZonedDateTime.of(2012, 4, 29, 0, 0, 0, 0, zoneId)
		assertTrue(CronExpression("0 0 0 * * 7L").nextTimeAfter(after) == expected)
		expected = ZonedDateTime.of(2012, 4, 8, 0, 0, 0, 0, zoneId)
		assertTrue(CronExpression("0 0 0 * * 7#2").nextTimeAfter(after) == expected)
	}

	@Test
	@Throws(Exception::class)
	fun check_dayOfWeek_nth_day_in_month() {
		var after: ZonedDateTime = ZonedDateTime.of(2012, 4, 1, 0, 0, 0, 0, zoneId)
		var expected: ZonedDateTime = ZonedDateTime.of(2012, 4, 20, 0, 0, 0, 0, zoneId)
		assertTrue(CronExpression("0 0 0 * * 5#3").nextTimeAfter(after) == expected)
		after = ZonedDateTime.of(2012, 4, 20, 0, 0, 0, 0, zoneId)
		expected = ZonedDateTime.of(2012, 5, 18, 0, 0, 0, 0, zoneId)
		assertTrue(CronExpression("0 0 0 * * 5#3").nextTimeAfter(after) == expected)
		after = ZonedDateTime.of(2012, 3, 30, 0, 0, 0, 0, zoneId)
		expected = ZonedDateTime.of(2012, 4, 1, 0, 0, 0, 0, zoneId)
		assertTrue(CronExpression("0 0 0 * * 7#1").nextTimeAfter(after) == expected)
		after = ZonedDateTime.of(2012, 4, 1, 0, 0, 0, 0, zoneId)
		expected = ZonedDateTime.of(2012, 5, 6, 0, 0, 0, 0, zoneId)
		assertTrue(CronExpression("0 0 0 * * 7#1").nextTimeAfter(after) == expected)
		after = ZonedDateTime.of(2012, 2, 6, 0, 0, 0, 0, zoneId)
		expected = ZonedDateTime.of(2012, 2, 29, 0, 0, 0, 0, zoneId)
		assertTrue(CronExpression("0 0 0 * * 3#5").nextTimeAfter(after) == expected) // leapday
		after = ZonedDateTime.of(2012, 2, 6, 0, 0, 0, 0, zoneId)
		expected = ZonedDateTime.of(2012, 2, 29, 0, 0, 0, 0, zoneId)
		assertTrue(CronExpression("0 0 0 * * WED#5").nextTimeAfter(after) == expected) // leapday
	}

	@Test
	fun shall_not_not_support_rolling_period() {
		assertThrows<IllegalArgumentException> {
			CronExpression("* * 5-1 * * *")
		}
	}

	@Test
	fun non_existing_date_throws_exception() {
		// Will check for the next 4 years - no 30th of February is found so an IAE is thrown.
		assertThrows<IllegalArgumentException> {
			CronExpression("* * * 30 2 *").nextTimeAfter(ZonedDateTime.now())
		}
	}

	@Test
	@Throws(Exception::class)
	fun test_default_barrier() {
		val cronExpr = CronExpression("* * * 29 2 *")
		val after: ZonedDateTime = ZonedDateTime.of(2012, 3, 1, 0, 0, 0, 0, zoneId)
		val expected: ZonedDateTime = ZonedDateTime.of(2016, 2, 29, 0, 0, 0, 0, zoneId)
		// the default barrier is 4 years - so leap years are considered.
		assertEquals(expected, cronExpr.nextTimeAfter(after))
	}

	@Test
	fun test_one_year_barrier() {
		val after: ZonedDateTime = ZonedDateTime.of(2012, 3, 1, 0, 0, 0, 0, zoneId)
		val barrier: ZonedDateTime = ZonedDateTime.of(2013, 3, 1, 0, 0, 0, 0, zoneId)
		// The next leap year is 2016, so an IllegalArgumentException is expected.
		assertThrows<IllegalArgumentException> {
			CronExpression("* * * 29 2 *").nextTimeAfter(after, barrier)
		}
	}

	@Test
	fun test_two_year_barrier() {
		val after: ZonedDateTime = ZonedDateTime.of(2012, 3, 1, 0, 0, 0, 0, zoneId)
		// The next leap year is 2016, so an IllegalArgumentException is expected.
		assertThrows<IllegalArgumentException> {
			CronExpression("* * * 29 2 *").nextTimeAfter(after, (1000L * 60 * 60 * 24 * 356 * 2))
		}
	}

	@Test
	fun test_seconds_specified_but_should_be_omitted() {
		assertThrows<IllegalArgumentException> {
			CronExpression.createWithoutSeconds("* * * 29 2 *")
		}
	}

	@Test
	@Throws(Exception::class)
	fun test_without_seconds() {
		val after: ZonedDateTime = ZonedDateTime.of(2012, 3, 1, 0, 0, 0, 0, zoneId)
		val expected: ZonedDateTime = ZonedDateTime.of(2016, 2, 29, 0, 0, 0, 0, zoneId)
		assertTrue(CronExpression.createWithoutSeconds("* * 29 2 *").nextTimeAfter(after) == expected)
	}

	@Test
	fun testTriggerProblemSameMonth() {
		assertEquals(
			ZonedDateTime.parse("2020-01-02T00:50:00Z"),
			CronExpression("00 50 * 1-8 1 *")
				.nextTimeAfter(ZonedDateTime.parse("2020-01-01T23:50:00Z"))
		)
	}

	@Test
	fun testTriggerProblemNextMonth() {
		assertEquals(
			ZonedDateTime.parse("2020-02-01T00:50:00Z"),
			CronExpression("00 50 * 1-8 2 *")
				.nextTimeAfter(ZonedDateTime.parse("2020-01-31T23:50:00Z"))
		)
	}

	@Test
	fun testTriggerProblemNextYear() {
		assertEquals(
			ZonedDateTime.parse("2020-01-01T00:50:00Z"),
			CronExpression("00 50 * 1-8 1 *")
				.nextTimeAfter(ZonedDateTime.parse("2019-12-31T23:50:00Z"))
		)
	}

	@Test
	fun testTriggerProblemNextMonthMonthAst() {
		assertEquals(
			ZonedDateTime.parse("2020-02-01T00:50:00Z"),
			CronExpression("00 50 * 1-8 * *")
				.nextTimeAfter(ZonedDateTime.parse("2020-01-31T23:50:00Z"))
		)
	}

	@Test
	fun testTriggerProblemNextYearMonthAst() {
		assertEquals(
			ZonedDateTime.parse("2020-01-01T00:50:00Z"),
			CronExpression("00 50 * 1-8 * *")
				.nextTimeAfter(ZonedDateTime.parse("2019-12-31T23:50:00Z"))
		)
	}

	@Test
	fun testTriggerProblemNextMonthDayAst() {
		assertEquals(
			ZonedDateTime.parse("2020-02-01T00:50:00Z"),
			CronExpression("00 50 * * 2 *")
				.nextTimeAfter(ZonedDateTime.parse("2020-01-31T23:50:00Z"))
		)
	}

	@Test
	fun testTriggerProblemNextYearDayAst() {
		assertEquals(
			ZonedDateTime.parse("2020-01-01T00:50:00Z"),
			CronExpression("00 50 * * 1 *")
				.nextTimeAfter(ZonedDateTime.parse("2019-12-31T22:50:00Z"))
		)
	}

	@Test
	fun testTriggerProblemNextMonthAllAst() {
		assertEquals(
			ZonedDateTime.parse("2020-02-01T00:50:00Z"),
			CronExpression("00 50 * * * *")
				.nextTimeAfter(ZonedDateTime.parse("2020-01-31T23:50:00Z"))
		)
	}

	@Test
	fun testTriggerProblemNextYearAllAst() {
		assertEquals(
			ZonedDateTime.parse("2020-01-01T00:50:00Z"),
			CronExpression("00 50 * * * *")
				.nextTimeAfter(ZonedDateTime.parse("2019-12-31T23:50:00Z"))
		)
	}
}