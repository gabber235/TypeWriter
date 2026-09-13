/// Comparator used by nullable extrema helpers.
typedef ValueComparator<T> = int Function(T left, T right);

/// Returns the greater nonnull bound, treating null as an absent bound.
///
/// This semantics lets type merging retain the stricter lower or upper bound:
/// callers choose [maximumNullable] for lower bounds and
/// [minimumNullable] for upper bounds.
T? maximumNullable<T>(
  T? left,
  T? right, {
  required ValueComparator<T> compare,
}) => switch ((left, right)) {
  (null, _) => right,
  (_, null) => left,
  (final a?, final b?) => compare(a, b) >= 0 ? a : b,
};

/// Returns the lesser nonnull bound, treating null as an absent bound.
T? minimumNullable<T>(
  T? left,
  T? right, {
  required ValueComparator<T> compare,
}) => switch ((left, right)) {
  (null, _) => right,
  (_, null) => left,
  (final a?, final b?) => compare(a, b) <= 0 ? a : b,
};

/// Comparable specialization of [maximumNullable].
T? maximumNullableComparable<T extends Comparable<dynamic>>(
  T? left,
  T? right,
) => maximumNullable(left, right, compare: (a, b) => a.compareTo(b));

/// Comparable specialization of [minimumNullable].
T? minimumNullableComparable<T extends Comparable<dynamic>>(
  T? left,
  T? right,
) => minimumNullable(left, right, compare: (a, b) => a.compareTo(b));
