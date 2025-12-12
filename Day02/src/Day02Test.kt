import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.CsvSource
import org.junit.jupiter.params.provider.ValueSource

class Day02Test {

  /**
   * Confirms that [Day02.findInvalids] correctly flags a single number
   * in a minimal inclusive range.
   *
   * The test provides the range `1010L to 1010L`, which contains only the number
   * 1010. It asserts that the function returns a list with the string
   * representation `"1010"`, demonstrating that the logic for detecting
   * invalid numbers operates as expected when the range consists of a single
   * element.
   */
  @Test
  fun `test 1010`() {
    val result = Day02.findInvalids(1010L to 1010L)

    assertEquals(listOf("1010"), result)
  }

  /**
   * Verifies that [Day02.findInvalids] returns the expected list of invalid numbers for a given numeric range.
   *
   * The test splits the range string into a pair of `Long` values using `Day02.splitRange`, splits the
   * expected invalid numbers string on semicolons, and asserts that the result from `Day02.findInvalids`
   * matches the expected list.
   *
   * @param rangeString a string representation of an inclusive numeric range in the form "start-end".
   * @param invalidsString a semicolon‑separated list of the numbers that are expected to be identified as invalid.
   */
  @ParameterizedTest
  @CsvSource(
    "11-22,11;22",
    "95-115,99",
    "998-1012,1010",
    "1188511880-1188511890,1188511885",
    "222220-222224,222222",
    "446443-446449,446446",
    "38593856-38593862,38593859",
  )
  fun `examples from exercise`(rangeString: String, invalidsString: String) {
    val range = Day02.splitRange(rangeString)
    val invalids = invalidsString.split(";")

    val result = Day02.findInvalids(range)

    assertEquals(invalids, result)
  }

  /**
   * Verifies that [Day02.findInvalids] correctly identifies that the range
   * `1698522-1698528` contains no invalid numbers according to the puzzle rule.
   *
   * @param rangeString a string representation of an inclusive numeric range in the form "start-end".
   */
  @ParameterizedTest
  @ValueSource(
    strings = ["1698522-1698528"],
  )
  fun `examples from exercise without invalid numbers`(rangeString: String) {
    val range = Day02.splitRange(rangeString)

    val result = Day02.findInvalids(range)

    assertEquals(true, result.isEmpty())
  }

  /**
   * Verifies that the sum of invalid numbers identified by [Day02.findInvalids] matches the expected total for a
   * given numeric range.
   *
   * @param rangeString a string representing an inclusive numeric range in the format `"start-end"`.
   * @param invalidsString a semicolon‑separated list of numbers expected to be identified as invalid.
   */
  @ParameterizedTest
  @CsvSource(
    "11-22,11;22",
    "95-115,99",
    "998-1012,1010",
    "1188511880-1188511890,1188511885",
    "222220-222224,222222",
    "446443-446449,446446",
    "38593856-38593862,38593859",
  )
  fun `summing examples from exercise`(rangeString: String, invalidsString: String) {
    val range = Day02.splitRange(rangeString)
    val invalidSum = invalidsString.split(";").sumOf { it.toLong() }

    val result = Day02.findInvalids(range).sumOf { it.toLong() }

    assertEquals(invalidSum, result)
  }

  /**
   * Verifies that the sum of invalid numbers identified by [Day02.findInvalids] is zero
   * for a range that contains no invalid entries.
   *
   * The test splits the provided range string, calculates the list of invalid numbers,
   * sums them, and asserts that the result equals 0.
   *
   * @param rangeString a string representation of an inclusive numeric range in the form "start-end".
   */
  @ParameterizedTest
  @ValueSource(
    strings = ["1698522-1698528"],
  )
  fun `summing examples from exercise without invalids`(rangeString: String) {
    val range = Day02.splitRange(rangeString)

    val result = Day02.findInvalids(range).sumOf { it.toLong() }

    assertEquals(0, result)
  }
}
