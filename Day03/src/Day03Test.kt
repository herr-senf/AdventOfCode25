import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.CsvSource

class Day03Test {

  @ParameterizedTest
  @CsvSource(
    "987654321111111,98",
    "811111111111119,89",
    "234234234234278,78",
    "818181911112111,92",
  )
  fun `execute exercise examples`(bank: String, joltage: String) {
    val result = Day03.getJoltage(bank)

    assertEquals(joltage, result)
  }
}
