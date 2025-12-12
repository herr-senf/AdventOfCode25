import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.CsvSource
import org.junit.jupiter.params.provider.ValueSource

class Day02_2Test {

  @ParameterizedTest
  @CsvSource(
    "11-22,11;22",
    "95-115,99;111",
    "998-1012,999;1010",
    "1188511880-1188511890,1188511885",
    "222220-222224,222222",
    "446443-446449,446446",
    "38593856-38593862,38593859",
    "565653-565659,565656",
    "824824821-824824827,824824824",
    "2121212118-2121212124, 2121212121",
  )
  fun `test exercise examples`(rangeString: String, invalids: String) {
    val range = Day02_2.splitRange(rangeString)
    val result = Day02_2.findInvalids(range)

    assertEquals(invalids.split(";"), result)
  }

  @ParameterizedTest
  @ValueSource(
    strings = ["1698522-1698528"],
  )
  fun `test exercise with no invalids examples`(rangeString: String) {
    val range = Day02_2.splitRange(rangeString)
    val result = Day02_2.findInvalids(range)

    assertEquals(true, result.isEmpty())
  }
}
