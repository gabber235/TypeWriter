import "package:typewriter/utils/extensions.dart";

/// Parses a cron expression and returns a [CronExpression] object.
/// For a detailed description of the cron expression syntax, see [link](https://www.netiq.com/documentation/cloud-manager-2-5/ncm-reference/data/bexyssf.html).
///
/// Fields are separated by whitespace. And may look like this:
/// |-------------------------------------------------------------------------|
/// | Field name  | Mandatory? | Allowed values  | Allowed special characters |
/// |-------------|------------|-----------------|----------------------------|
/// | Seconds     | NO         | 0-59            | * / , -                    |
/// | Minutes     | YES        | 0-59            | * / , -                    |
/// | Hours       | YES        | 0-23            | * / , -                    |
/// | Day-of-month| YES        | 1-31            | * / , - ? L W              |
/// | Month       | YES        | 1-12 or JAN-DEC | * / , -                    |
/// | Day-of-week | YES        | 1-7 or MON-SUN  | * / , - ? L #              |
/// |-------------------------------------------------------------------------|
///
/// The special characters are:
/// |--------------------------------------------------------------------------|
/// | Character | Description                                                  |
/// |-----------|--------------------------------------------------------------|
/// | *         | Specifies all possible values for a field. For example, "*" in |
/// |           | the minute field means "every minute".                       |
/// |-----------|--------------------------------------------------------------|
/// | ?         | Used to specify both day-of-month and day-of-week.           |
/// |-----------|--------------------------------------------------------------|
/// | -         | Used to specify ranges For example, "10-12" in the hour field |
/// |           | means "the hours 10, 11 and 12".                             |
/// |-----------|--------------------------------------------------------------|
/// | ,         | Used to specify additional values. For example, "MON,WED,FRI" |
/// |           | in the day-of-week field means "the days Monday, Wednesday,   |
/// |           | and Friday".                                                 |
/// |-----------|--------------------------------------------------------------|
/// | /         | Used to specify increments. For example, "0/15" in the seconds |
/// |           | field means "the seconds 0, 15, 30, and 45". And "5/15" in    |
/// |           | the seconds field means "the seconds 5, 20, 35, and 50".      |
/// |           | It can also be used with the '*' character. If used with '*'  |
/// |           | then the starting value for the increment is 0. For example,  |
/// |           | "*/15" in the seconds field means "the seconds 0, 15, 30, and |
/// |           | 45". A character before the '/' is called the 'begin'         |
/// |           | character in this context.                                   |
/// |-----------|--------------------------------------------------------------|
/// | L         | Used to specify the 'last' day of the month - day of month    |
/// |           | and day of week both. For example, if L is specified in the   |
/// |           | day-of-month field, the meaning is: "the last day of the      |
/// |           | month" - day 31 for January, day 28 for February on non-leap  |
/// |           | years. If L is specified in the day-of-week field by itself,  |
/// |           | it simply means "7" or "SAT". But if it is specified in the   |
/// |           | day-of-week field after another value, it means "the last     |
/// |           | xxx day of the month" - for example "6L" means "the last      |
/// |           | friday of the month". When using the 'L' option, it is        |
/// |           | important not to specify lists, or ranges of values, as you   |
/// |           |'ll get confusing/unexpected results.                         |
/// |-----------|--------------------------------------------------------------|
/// | W         | Used to specify the weekday (Monday-Friday) nearest the given |
/// |           | day. As an example, if you were to specify "15W" as the value |
/// |           | for the day-of-month field, the meaning is: "the nearest      |
/// |           | weekday to the 15th of the month". So if the 15th is a       |
/// |           | Saturday, the trigger will fire on Friday the 14th. If the    |
/// |           | 15th is a Sunday, the trigger will fire on Monday the 16th.   |
/// |           | If the 15th is a Tuesday, then it will fire on Tuesday the   |
/// |           | 15th. However if you specify "1W" as the value for day-of-    |
/// |           | month, and the 1st is a Saturday, the trigger will fire on    |
/// |           | Monday the 3rd, as it will not 'jump' over the boundary of a  |
/// |           | month's days. The 'W' character can only be specified when    |
/// |           | the day-of-month is a single day, not a range or list of days.|
/// |-----------|--------------------------------------------------------------|
/// | #         | Used to specify "the nth" XXX day of the month. For example, |
/// |           | the value of "6#3" in the day-of-week field means the third   |
/// |           | Friday of the month (day 6 = Friday and "#3" = the 3rd one in |
/// |           | the month). Other examples: "2#1" = the first Monday of the   |
/// |           | month and "4#5" = the fifth Wednesday of the month. Note that |
/// |           | if you specify "#5" and there is not 5 of the given day-of-   |
/// |           | week in the month, then no firing will occur that month.      |
/// |--------------------------------------------------------------------------|
///
///
/// Example:
/// |-----------------------------------------------------------------------------|
/// | Expression        | Description                                             |
/// |-------------------|---------------------------------------------------------|
/// | 0 0 12 * * ?      | Fire at 12pm (noon) every day                           |
/// | 0 15 10 ? * *     | Fire at 10:15am every day                               |
/// | 0 15 10 * * ?     | Fire at 10:15am every day                               |
/// | 0 * 14 * * ?      | Fire every minute starting at 2pm and ending at 2:59pm, |
/// | 0/5 14 * * ?      | Fire every five minutes starting at 2:00 p.m. and ending at 2:55 p.m., every day |
/// | 15 10 ? * MON-FRI | Fire at 10:15am every Monday, Tuesday, Wednesday, Thursday and Friday |
/// | 0 15 10 ? * 6L    | Fire at 10:15am on the last Friday of every month       |
/// | 15 10 ? * 6#3     | Fire at 10:15am on the third Friday of every month      |
/// |-----------------------------------------------------------------------------|
///
class CronExpression {
  const CronExpression(
    this.seconds,
    this.minutes,
    this.hours,
    this.dayOfMonth,
    this.month,
    this.dayOfWeek,
  );

  /// The cron expression string
  final SimpleField? seconds;
  final SimpleField minutes;
  final SimpleField hours;
  final DayOfMonthField dayOfMonth;
  final MonthField month;
  final DayOfWeekField dayOfWeek;

  /// Returns a human readable string representation of the cron expression.
  String toHumanReadableString() {
    var text = "";
    if (seconds != null) {
      text += seconds!.toHumanReadableString("second");

      if (!minutes.isWildcard) {
        text += " past ";
        text += minutes.toHumanReadableString("minute");
      }
    } else {
      text += minutes.toHumanReadableString("minute");
    }

    if (!hours.isWildcard) {
      text += " past ";
      text += hours.toHumanReadableString("hour");
    }

    if (!dayOfMonth.isWildcard) {
      text += " ";
      text += dayOfMonth.toHumanReadableString();
    }

    if (!month.isWildcard) {
      text += " in ";
      text += month.toHumanReadableString();
    }

    if (!dayOfWeek.isWildcard) {
      text += " ";
      text += dayOfWeek.toHumanReadableString();
    }

    return text.capitalize;
  }

  /// Parses a cron expression from a string.
  static CronExpression? parse(String expression) {
    final parts = expression.split(" ");
    if (parts.length < 5 || parts.length > 6) {
      return null;
    }

    final hasSeconds = parts.length == 6;
    final iter = parts.iterator;

    final seconds =
        hasSeconds ? SimpleField.parse(iter.nextOrNull, 0, 59) : null;
    final minutes = SimpleField.parse(iter.nextOrNull, 0, 59);
    final hours = SimpleField.parse(iter.nextOrNull, 0, 23);
    final dayOfMonth = DayOfMonthField.parse(iter.nextOrNull);
    final month = MonthField.parse(iter.nextOrNull);
    final dayOfWeek = DayOfWeekField.parse(iter.nextOrNull);

    if ((hasSeconds && seconds == null) ||
        minutes == null ||
        hours == null ||
        dayOfMonth == null ||
        month == null ||
        dayOfWeek == null) {
      return null;
    }

    return CronExpression(
      seconds,
      minutes,
      hours,
      dayOfMonth,
      month,
      dayOfWeek,
    );
  }
}

/// A part of a cron field.
abstract class CronPart {
  const CronPart();

  bool valid(int min, int max) => true;

  /// Parses a part from a string.
  static CronPart? parse(String value) {
    return WildcardPart.parse(value) ??
        ValuePart.parse(value) ??
        RangePart.parse(value) ??
        IncrementPart.parse(value);
  }
}

class WildcardPart extends CronPart {
  const WildcardPart();

  static WildcardPart? parse(String value) =>
      value == "*" || value == "?" ? const WildcardPart() : null;
}

class ValuePart extends CronPart {
  const ValuePart(this.value);
  final int value;

  @override
  bool valid(int min, int max) => value >= min && value <= max;

  static ValuePart? parse(String value) {
    final tryParse = int.tryParse(value);
    return tryParse == null ? null : ValuePart(tryParse);
  }
}

class RangePart extends CronPart {
  const RangePart(this.start, this.end);

  final ValuePart start;
  final ValuePart end;

  @override
  bool valid(int min, int max) => start.value >= min && end.value <= max;

  static RangePart? parse(String value) {
    final parts = value.split("-");
    if (parts.length != 2) {
      return null;
    }
    final start = ValuePart.parse(parts[0]);
    final end = ValuePart.parse(parts[1]);
    if (start == null || end == null) {
      return null;
    }
    return RangePart(start, end);
  }
}

class IncrementPart extends CronPart {
  const IncrementPart(this.part, this.increment);

  final CronPart part; // Can be a value, range or wildcard
  final int increment;

  @override
  bool valid(int min, int max) =>
      part.valid(min, max) && increment > 0 && increment <= max;

  static IncrementPart? parse(String value) {
    final parts = value.split("/");
    if (parts.length != 2) {
      return null;
    }
    final part = CronPart.parse(parts[0]);
    final increment = int.tryParse(parts[1]);
    if (part == null || increment == null) {
      return null;
    }

    if (part is IncrementPart) {
      return null;
    }

    return IncrementPart(part, increment);
  }
}

/// A field in a cron expression.
/// This is used for seconds, minutes and hours.
///
/// |---------------------------------------------|
/// | Field   | Required | Range | Allowed Values |
/// | Seconds | NO       | 0-59  | * / , -        |
/// | Minutes | YES      | 0-59  | * / , -        |
/// | Hours   | YES      | 0-23  | * / , -        |
/// |---------------------------------------------|
///
/// See [CronExpression] for more information.
class SimpleField {
  const SimpleField(this.parts);

  final List<CronPart> parts;

  bool get isWildcard => parts.length == 1 && parts[0] is WildcardPart;

  String humanReadablePart(String field, CronPart part) {
    if (part is WildcardPart) {
      return "every $field";
    } else if (part is ValuePart) {
      return "$field ${part.value}";
    } else if (part is RangePart) {
      return "every $field from ${part.start.value} through ${part.end.value}";
    } else if (part is IncrementPart) {
      final subPart = part.part;
      if (subPart is WildcardPart) {
        return "every ${part.increment.ordinal} $field";
      } else if (subPart is ValuePart) {
        return "every ${part.increment.ordinal} $field starting at $field ${subPart.value}";
      } else if (subPart is RangePart) {
        return "every ${part.increment.ordinal} $field from ${subPart.start.value} through ${subPart.end.value}";
      }
    }
    return "";
  }

  String toHumanReadableString(String field) {
    return parts.map((part) => humanReadablePart(field, part)).join(", and ");
  }

  static SimpleField? parse(String? value, int min, int max) {
    if (value == null) {
      return null;
    }
    final parts = value.split(",");
    final parsedParts = parts.map(CronPart.parse).toList();
    if (parsedParts.any((part) => part == null)) {
      return null;
    }

    final trueParts = parsedParts.whereType<CronPart>().toList();

    if (trueParts.any((part) => !part.valid(min, max))) {
      return null;
    }

    return SimpleField(trueParts);
  }
}

/// Day of the month field.
/// This is used for the day of the month field.
///
/// |--------------------------------------------------|
/// | Field        | Required | Range | Allowed Values |
/// | Day of Month | YES      | 1-31  | * / , - ? L W  |
/// |--------------------------------------------------|
///
/// See [CronExpression] for more information.
abstract class DayOfMonthField {
  const DayOfMonthField();

  bool get isWildcard => false;

  String toHumanReadableString();

  static DayOfMonthField? parse(String? value) {
    return LastNearestWeekdayOfMonthField.parse(value) ??
        LastDayOfMonthField.parse(value) ??
        NearestWeekdayOfMonthField.parse(value) ??
        SimpleDayOfMonthField.parse(value);
  }
}

/// Day of the month field without L and W.
class SimpleDayOfMonthField extends DayOfMonthField {
  const SimpleDayOfMonthField(this.parts);

  final List<CronPart> parts;

  @override
  bool get isWildcard => parts.length == 1 && parts[0] is WildcardPart;

  String _humanReadablePart(CronPart part) {
    if (part is WildcardPart) {
      return "every day";
    } else if (part is ValuePart) {
      return "on the ${part.value.ordinal} day of the month";
    } else if (part is RangePart) {
      return "every day from the ${part.start.value.ordinal} through the ${part.end.value.ordinal} day of the month";
    } else if (part is IncrementPart) {
      final subPart = part.part;
      if (subPart is WildcardPart) {
        return "every ${part.increment.ordinal} day of the month";
      } else if (subPart is ValuePart) {
        return "every ${part.increment.ordinal} day of the month starting on the ${subPart.value.ordinal}";
      } else if (subPart is RangePart) {
        return "every ${part.increment.ordinal} day of the month from the ${subPart.start.value.ordinal} through the ${subPart.end.value.ordinal} day of the month";
      }
    }
    return "";
  }

  @override
  String toHumanReadableString() {
    return parts.map(_humanReadablePart).join(", and ");
  }

  static SimpleDayOfMonthField? parse(String? value) {
    final simpleField = SimpleField.parse(value, 1, 31);
    if (simpleField == null) {
      return null;
    }
    return SimpleDayOfMonthField(simpleField.parts);
  }
}

/// Day of the month field with L.
class LastDayOfMonthField extends DayOfMonthField {
  const LastDayOfMonthField(this.part);

  final ValuePart? part;

  @override
  String toHumanReadableString() {
    if (part == null) {
      return "on the last day of the month";
    }
    return "on the ${part!.value.ordinal} to last day of the month";
  }

  static LastDayOfMonthField? parse(String? value) {
    if (value == null) {
      return null;
    }
    if (value == "L") {
      return const LastDayOfMonthField(null);
    }
    if (!value.startsWith("L-")) {
      return null;
    }
    final part = ValuePart.parse(value.substring(2));
    if (part == null) {
      return null;
    }
    return LastDayOfMonthField(part);
  }
}

/// Day of the month field with W.
class NearestWeekdayOfMonthField extends DayOfMonthField {
  const NearestWeekdayOfMonthField(this.part);

  final ValuePart part;

  @override
  String toHumanReadableString() {
    return "on the nearest weekday to the ${part.value.ordinal} day of the month";
  }

  static NearestWeekdayOfMonthField? parse(String? value) {
    if (value == null) {
      return null;
    }
    if (!value.endsWith("W")) {
      return null;
    }
    final part = ValuePart.parse(value.substring(0, value.length - 1));
    if (part == null) {
      return null;
    }
    return NearestWeekdayOfMonthField(part);
  }
}

/// Day of the month field with L and W.
class LastNearestWeekdayOfMonthField extends DayOfMonthField {
  const LastNearestWeekdayOfMonthField();

  @override
  String toHumanReadableString() {
    return "on the nearest weekday to the last day of the month";
  }

  static LastNearestWeekdayOfMonthField? parse(String? value) =>
      value == "LW" ? const LastNearestWeekdayOfMonthField() : null;
}

/// Month field.
/// This is used for the month field.
///
/// |-------------------------------------------------------|
/// | Field   | Required | Range           | Allowed Values |
/// | Month   | YES      | 1-12 or JAN-DEC | * / , -        |
/// |-------------------------------------------------------|
///
/// See [CronExpression] for more information.
class MonthField {
  const MonthField(this.parts);

  final List<CronPart> parts;

  static const _monthNames = [
    "January",
    "February",
    "March",
    "April",
    "May",
    "June",
    "July",
    "August",
    "September",
    "October",
    "November",
    "December",
  ];

  bool get isWildcard => parts.length == 1 && parts[0] is WildcardPart;

  static String? _replaceMonth(String? value) {
    return value
        ?.replaceAll("JAN", "1")
        .replaceAll("FEB", "2")
        .replaceAll("MAR", "3")
        .replaceAll("APR", "4")
        .replaceAll("MAY", "5")
        .replaceAll("JUN", "6")
        .replaceAll("JUL", "7")
        .replaceAll("AUG", "8")
        .replaceAll("SEP", "9")
        .replaceAll("OCT", "10")
        .replaceAll("NOV", "11")
        .replaceAll("DEC", "12");
  }

  String _humanReadablePart(CronPart part) {
    if (part is WildcardPart) {
      return "of every month";
    } else if (part is ValuePart) {
      return _monthNames[part.value - 1];
    } else if (part is RangePart) {
      return "every month from ${_monthNames[part.start.value - 1]} through ${_monthNames[part.end.value - 1]}";
    } else if (part is IncrementPart) {
      final subPart = part.part;
      if (subPart is WildcardPart) {
        return "every ${part.increment.ordinal} month";
      } else if (subPart is ValuePart) {
        return "every ${part.increment.ordinal} month starting in ${_monthNames[subPart.value - 1]}";
      } else if (subPart is RangePart) {
        return "every ${part.increment.ordinal} month from ${_monthNames[subPart.start.value - 1]} through ${_monthNames[subPart.end.value - 1]}";
      }
    }
    return "";
  }

  String toHumanReadableString() {
    return parts.map(_humanReadablePart).join(", and ");
  }

  static MonthField? parse(String? value) {
    final simpleField = SimpleField.parse(_replaceMonth(value), 1, 12);
    if (simpleField == null) {
      return null;
    }
    return MonthField(simpleField.parts);
  }
}

/// Day of the week field.
/// This is used for the day of the week field.
///
/// |------------------------------------------------------------|
/// | Field        | Required | Range           | Allowed Values |
/// | Day of Week  | YES      | 0-7 or SUN-SAT  | * / , - ? L #  |
/// |------------------------------------------------------------|
///
/// See [CronExpression] for more information.
abstract class DayOfWeekField {
  const DayOfWeekField();

  bool get isWildcard => false;

  String toHumanReadableString();

  static DayOfWeekField? parse(String? value) {
    return LastDayOfWeekField.parse(value) ??
        NthDayOfWeekField.parse(value) ??
        SimpleDayOfWeekField.parse(value);
  }
}

/// Day of the week field without L, # and ?.
class SimpleDayOfWeekField extends DayOfWeekField {
  const SimpleDayOfWeekField(this.parts);

  final List<CronPart> parts;

  static const _dayNames = [
    "Monday",
    "Tuesday",
    "Wednesday",
    "Thursday",
    "Friday",
    "Saturday",
    "Sunday",
  ];

  @override
  bool get isWildcard => parts.length == 1 && parts[0] is WildcardPart;

  static String? _replaceDayOfWeek(String? value) {
    return value
        ?.replaceAll("MON", "1")
        .replaceAll("TUE", "2")
        .replaceAll("WED", "3")
        .replaceAll("THU", "4")
        .replaceAll("FRI", "5")
        .replaceAll("SAT", "6")
        .replaceAll("SUN", "7");
  }

  String _humanReadablePart(CronPart part) {
    if (part is WildcardPart) {
      return "every day of the week";
    } else if (part is ValuePart) {
      return "on ${_dayNames[part.value - 1]}";
    } else if (part is RangePart) {
      return "from ${_dayNames[part.start.value - 1]} through ${_dayNames[part.end.value - 1]}";
    } else if (part is IncrementPart) {
      final subPart = part.part;
      if (subPart is WildcardPart) {
        return "every ${part.increment.ordinal} day of the week";
      } else if (subPart is ValuePart) {
        return "every ${part.increment.ordinal} day of the week starting on ${_dayNames[subPart.value - 1]}";
      } else if (subPart is RangePart) {
        return "every ${part.increment.ordinal} day of the week from ${_dayNames[subPart.start.value - 1]} through ${_dayNames[subPart.end.value - 1]}";
      }
    }
    return "";
  }

  @override
  String toHumanReadableString() {
    return parts.map(_humanReadablePart).join(", and ");
  }

  static SimpleDayOfWeekField? parse(String? value) {
    final simpleField = SimpleField.parse(_replaceDayOfWeek(value), 1, 7);
    if (simpleField == null) {
      return null;
    }
    return SimpleDayOfWeekField(simpleField.parts);
  }
}

/// Day of the week field with L.
class LastDayOfWeekField extends DayOfWeekField {
  const LastDayOfWeekField();

  @override
  String toHumanReadableString() {
    return "on Sunday";
  }

  static LastDayOfWeekField? parse(String? value) {
    if (value == null) {
      return null;
    }
    if (value != "L") {
      return null;
    }
    return const LastDayOfWeekField();
  }
}

/// Day of the week field with #.
class NthDayOfWeekField extends DayOfWeekField {
  const NthDayOfWeekField(this.part, this.nth);

  final ValuePart part;
  final ValuePart nth;

  static const _dayNames = [
    "Monday",
    "Tuesday",
    "Wednesday",
    "Thursday",
    "Friday",
    "Saturday",
    "Sunday",
  ];

  @override
  String toHumanReadableString() {
    return "on the ${nth.value.ordinal} ${_dayNames[part.value - 1]}";
  }

  static NthDayOfWeekField? parse(String? value) {
    if (value == null) {
      return null;
    }
    if (!value.contains("#")) {
      return null;
    }
    final parts = value.split("#");
    if (parts.length != 2) {
      return null;
    }
    final part = ValuePart.parse(parts[0]);
    final nth = ValuePart.parse(parts[1]);
    if (part == null || nth == null) {
      return null;
    }

    if (!part.valid(0, 7) || !nth.valid(1, 5)) {
      return null;
    }

    return NthDayOfWeekField(part, nth);
  }
}
