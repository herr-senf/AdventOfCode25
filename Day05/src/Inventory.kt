/**
 * Represents a collection of ingredients and their freshness status.
 *
 * The inventory is constructed from a list of strings that typically come
 * from a text file. The list is parsed in two phases:
 *
 * 1. **Freshness ranges** – Consecutive non‑blank lines contain ranges
 *    in the format `"start-end"`. Each range is parsed into a
 *    `LongRange` and stored in the private `freshIngredients` list.
 *    Parsing stops when an empty line is encountered.
 *
 * 2. **Ingredient identifiers** – Subsequent lines are treated as
 *    ingredient identifiers. For each identifier, the constructor
 *    records whether it falls within any of the parsed freshness ranges.
 *    The result is stored in the private `ingredientMap` where the key is
 *    the ingredient ID and the value is `true` if the ingredient is fresh
 *    and `false` otherwise.
 *
 * After construction, the inventory exposes a few read‑only properties
 * and a helper method to query freshness.
 *
 * @param lines the raw lines from which the inventory is built
 */
class Inventory(lines: List<String>) {

  private val _freshIngredients = mutableListOf<LongRange>()
  val freshIngredients: List<LongRange>
    get() = _freshIngredients
  private val ingredientMap = mutableMapOf<Long, Boolean>()

  val ingredientCount: Int
    get() = ingredientMap.size
  val freshCount: Int by lazy { ingredientMap.count { it.value } }

  /**
   * Initializes this inventory from the provided [lines] in two passes.
   *
   * ### Input format
   * The input is expected to be split into two sections:
   *
   * 1. **Freshness ranges**: consecutive **non-blank** lines, each describing a numeric range
   *    (e.g. `"start-end"`). Each range is parsed and appended to [_freshIngredients].
   *    Parsing stops at the first blank line.
   * 2. **Ingredient identifiers**: the remaining lines (after the blank separator) are treated
   *    as ingredient IDs. Each ID is converted to [Long] and stored in [ingredientMap] with a
   *    boolean indicating whether it lies within *any* range in [_freshIngredients].
   *
   * ### Notes / assumptions
   * - This block assumes [lines] contains at least one element and that a blank separator line
   *   exists after the ranges section. If the iterator runs out before a blank line is found,
   *   `iterator.next()` will throw.
   * - IDs are parsed using [String.toLong]; invalid numeric input will throw.
   */
  init {
    val iterator = lines.iterator()
    var line = iterator.next()

    while (line.isNotBlank()) {
      _freshIngredients += parseRange(line)

      line = iterator.next()
    }

    while (iterator.hasNext()) {
      line = iterator.next()
      val id = line.toLong()

      ingredientMap[id] = _freshIngredients.any { it.contains(id) }
    }
  }

  /**
   * Parses a string representing a numeric range in the format "start-end" into a LongRange.
   *
   * @param range the string to parse, expected to contain two numbers separated by a hyphen.
   * @return a LongRange constructed from the parsed start and end values.
   */
  private fun parseRange(range: String): LongRange {
    try {
      val split = range.split("-")
      return LongRange(split[0].toLong(), split[1].toLong())
    } catch (e: Exception) {
      println("Error parsing range $range")
      throw e
    }
  }

  /**
   * Checks whether the ingredient identified by the given ID is marked as fresh.
   *
   * @param id the unique identifier of the ingredient to check
   * @return `true` if the ingredient is fresh; `false` otherwise
   */
  fun isFresh(id: Long): Boolean =
    ingredientMap[id] ?: false
}

/**
 * A utility object that merges a collection of [LongRange]s into a minimal set of
 * non‑overlapping, sorted ranges.
 *
 * The merging algorithm works as follows:
 *
 * * The input list is first sorted by the start of each range.
 * * Ranges that overlap or are immediately adjacent are merged into a single
 *   range that spans from the earliest start to the latest end.
 * * If a range is entirely contained within a previously merged range, it is
 *   discarded.
 *
 * After processing, the resulting list of ranges is sorted by their start
 * values and contains no overlaps.
 */
object Combiner {

  /**
   * Merges overlapping or consecutive ranges into a compact list of non‑overlapping ranges.
   *
   * @param ranges The list of [LongRange] instances to combine. Ranges may overlap or be adjacent.
   * @return A sorted list of [LongRange] objects where all overlapping or contiguous ranges
   *         from the input have been merged into single ranges.
   */
  fun combine(ranges: List<LongRange>): List<LongRange> {
    val result = mutableListOf<LongRange>()

    for (range in ranges.sortedBy { it.first }) {
      val containsLeft = result.firstOrNull { it.contains(range.first) || it.contains(range.first - 1) }

      if (containsLeft != null) {
        if (containsLeft.contains(range.last)) continue // the current range is completely contained, can be omitted

        result.remove(containsLeft)
        result += LongRange(containsLeft.first, range.last)
      } else
        result += range
    }

    return result.sortedBy { it.first }
  }
}
