import Day02_2.splitRange
import java.io.File

fun main() {
  Day02_2.run()
}

object Day02_2 {

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
   */
  fun run() {
    val ranges = readInputFile()

    val invalids = ranges
      .flatMap { findInvalids(it) }
      .toSet()

    println("There are ${invalids.size} invalids.")

    val sum = invalids.sumOf { it.toLong() }
    println("Their sum is ${sum}.")
  }

  /**
   * Reads the input file `Day02/src/input.txt` and parses each comma‑separated range
   * specification into a `Pair<Long, Long>`. The file is read line by line, each line
   * split by commas, and each resulting token is converted with [splitRange].
   *
   * @return a list of pairs where the first element is the start of the range and
   * the second element is the end of the range, inclusive.
   */
  private fun readInputFile(): List<Pair<Long, Long>> =
    File("Day02/src/input.txt")
      .readLines()
      .flatMap { it.split(",") }
      .map { splitRange(it) }

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
   * Determines whether the given string is considered valid.
   *
   * A string is considered *invalid* if it can be decomposed into two or more
   * consecutive identical substrings that together make up the entire string.
   * The method checks all possible substring lengths that evenly divide the
   * string length (up to half the length) to detect such repetition.
   *
   * @param test the string to validate
   * @return `true` if the string does not contain any such repeated pattern,
   *         `false` otherwise
   */
  fun checkValid(test: String): Boolean {
    if (test.length <= 1) return true

    var length = 0
    var valid = true

    while (valid && length < test.length / 2) {
      length++
      if (test.length % length != 0) continue

      val token = test.substring(0, length)

      valid = valid && !containsRepetition(length, test, token)
    }

    return valid
  }

  /**
   * Checks whether every block of a given length, starting from the position `length`
   * in the supplied text, equals the provided token.
   *
   * @param length the size of each block to compare
   * @param text the string that is examined
   * @param token the value each block must match
   * @return `true` if all such blocks match the token; `false` otherwise
   */
  private fun containsRepetition(length: Int, text: String, token: String): Boolean {
    var start = length
    while (start < text.length) {
      val test = text.substring(start, start + length)
      if (test != token) {
        return false
      }
      start += length
    }
    return true
  }

  /**
   * Returns a list of all numbers within the specified inclusive range that are considered
   * invalid according to the puzzle rule.
   *
   * @param range a pair where the first value is the start of the range and the second value
   *              is the end of the range, both inclusive.
   * @return a list of string representations of each number that fails the {@link checkValid} test;
   *         the list is sorted in ascending numeric order and is empty if no numbers in the range
   *         are invalid.
   */
  fun findInvalids(range: Pair<Long, Long>): List<String> =
    (range.first..range.second)
      .asSequence()
      .map { it.toString() }
      .mapNotNull { if (checkValid(it)) null else it }
      .toList()
}
