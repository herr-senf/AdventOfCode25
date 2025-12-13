import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.CsvSource

class Day03_2Test {

  @ParameterizedTest
  @CsvSource(
    "987654321111111,987654321111",
    "811111111111119,811111111119",
    "234234234234278,434234234278",
    "818181911112111,888911112111",
  )
  fun `execute exercise examples`(bank: String, joltage: Long) {
    val result = Day03_2.getMaxJoltage(bank)

    assertEquals(joltage, result)
  }

  @Test
  fun `execute input examples`() {
    val result = Day03_2.getMaxJoltage("7657456331563216562634654224322452566143465272217643337113211677757511536441571276777725366617123589")

    assertEquals(777777777789, result)
  }
}
