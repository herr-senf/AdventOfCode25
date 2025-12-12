import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.CsvSource
import org.junit.jupiter.params.provider.ValueSource

class Day02Test {

  @Test
  fun `test 1010`() {
    val result = findInvalids(1010L to 1010L)

    assertEquals(listOf("1010"), result)
  }

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
  fun `examples from excercise`(rangeString: String, invalidsString: String) {
    val range = splitRange(rangeString)
    val invalids = invalidsString.split(";")

    val result = findInvalids(range)

    assertEquals(invalids, result)
  }

  @ParameterizedTest
  @ValueSource(
    strings = ["1698522-1698528"],
  )
  fun `examples from excercise without invalid numbers`(rangeString: String) {
    val range = splitRange(rangeString)

    val result = findInvalids(range)

    assertEquals(true, result.isEmpty())
  }

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
  fun `summing examples from excercise`(rangeString: String, invalidsString: String) {
    val range = splitRange(rangeString)
    val invalidSum = invalidsString.split(";").sumOf { it.toLong() }

    val result = findInvalids(range).sumOf { it.toLong() }

    assertEquals(invalidSum, result)
  }

  @ParameterizedTest
  @ValueSource(
    strings = ["1698522-1698528"],
  )
  fun `summing examples from excercise without invalids`(rangeString: String) {
    val range = splitRange(rangeString)

    val result = findInvalids(range).sumOf { it.toLong() }

    assertEquals(0, result)
  }
}
