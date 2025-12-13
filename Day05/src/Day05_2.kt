import java.io.File

fun main() {
  Day05_2.run()
}

object Day05_2 {

  /**
   * Reads the input file, builds an [Inventory] from its contents,
   * merges the freshness ranges, sums their sizes, and prints the
   * total number of fresh ingredients.
   *
   * The method performs the following steps:
   * 1. Calls [readInputFile] to obtain the raw lines from
   *    "Day05/src/input.txt".
   * 2. Constructs an [Inventory] instance, which parses the lines
   *    into freshness ranges and ingredient identifiers.
   * 3. Extracts the list of ranges via [Inventory.freshIngredients].
   * 4. Calls [Combiner.combine] to merge overlapping or adjacent ranges.
   * 5. Calculates the total count of fresh items by summing the size of
   *    each merged range (`LongRange.size()`).
   * 6. Prints a message to standard output in the format
   *    `"Inventory knows X fresh ingredients"` where *X* is the
   *    computed count.
   */
  fun run() {
    val input = readInputFile()
    val inventory = Inventory(input)

    val ranges = inventory.freshIngredients

    val totalCount = Combiner
      .combine(ranges)
      .sumOf { it.size() }

    println("Inventory knows $totalCount fresh ingredients")
  }

  /**
   * Reads the input file located at "Day05/src/input.txt" and returns its contents as a list of strings.
   *
   * Each element of the returned list represents one line from the file, preserving the original order.
   *
   * @return a list of strings, each string being a line from the input file
   */
  private fun readInputFile(): List<String> =
    File("Day05/src/input.txt")
      .readLines()
}

/**
 * Returns the number of `Long` values contained in this range.
 *
 * @return the size of the range, calculated as `last - first + 1`.
 */
fun LongRange.size(): Long =
  last - first + 1
