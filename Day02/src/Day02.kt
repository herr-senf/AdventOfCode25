import java.io.File

fun main() {
  Day02.run()
}

object Day02 {

  /**
   * Reads a comma‑separated list of number ranges from the file `Day02/src/input.txt`,
   * converts each range into a pair of long values, and collects all numbers that are
   * considered invalid according to the logic in [Day02.findInvalids].
   *
   * The collected invalid numbers are stored in a set to avoid duplicates.  The method
   * then prints:
   *
   * * The total number of distinct invalid numbers found.
   * * The arithmetic sum of those numbers.
   *
   * This function is intended to be invoked from `main()` as the entry point of the
   * application. It performs all I/O and output to the console and has no return
   * value.
   *
   * @return `Unit`
   */
  fun run() {
    val ranges = File("Day02/src/input.txt")
      .readLines()
      .flatMap { it.split(",") }
      .map { splitRange(it) }
    val invalids = ranges
      .flatMap { findInvalids(it) }
      .toSet()

    println("There are ${invalids.size} invalids.")

    val sum = invalids.sumOf { it.toLong() }
    println("Their sum is ${sum}.")
  }

  /**
   * Splits a range string formatted as "start-end" into a pair of Long values.
   *
   * @param string a string containing two numbers separated by a hyphen.
   * @return a Pair where the first component is the start value and the second component is the end value.
   */
  fun splitRange(string: String): Pair<Long, Long> {
    val result = string.split("-")

    assert(result.size == 2) { "Something went wrong" }

    return result[0].toLong() to result[1].toLong()
  }

  /**
   * Determines whether a string is considered valid according to the puzzle rule.
   *
   * A string is **invalid** if its length is even and the first half of the string is
   * identical to the second half. In all other cases the string is deemed **valid**.
   *
   * @param text the string to be validated.
   * @return `true` if the string is valid, `false` otherwise.
   */
  fun checkValid(text: String): Boolean {
    if (text.length % 2 != 0) return true

    val halfLength = text.length / 2
    val test = text.substring(0, halfLength)

    return text != test + test
  }

  /**
   * Returns a list of all numbers within the specified inclusive range that are considered
   * invalid according to the puzzle rule.
   *
   * @param range a pair where the first value is the start of the range and the second value
   * is the end of the range, both inclusive.
   * @return a list of string representations of each number that fails the
   * [checkValid] test. The list is sorted in ascending numeric order and is empty
   * if no numbers in the range are invalid.
   */
  fun findInvalids(range: Pair<Long, Long>): List<String> =
    (range.first..range.second)
      .asSequence()
      .map { it.toString() }
      .mapNotNull { if (checkValid(it)) null else it }
      .toList()
}
