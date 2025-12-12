import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.CsvSource

class Day01_02Test {

  @Test
  fun `use 434C49434B generation with R1000`() {
    val result = Day01_2.solveToPassword(arrayOf("R1000"), 50, 100)

    assertEquals(10, result.second)
    assertEquals(50, result.first)
  }

  @Test
  fun `simple around the wheel test`() {
    val result = Day01_2.solveToPassword(arrayOf("L463"), 0, 100)

    assertEquals(37, result.first)
    assertEquals(4, result.second)
  }

  @Test
  fun `left full turn`() {
    val result = Day01_2.solveToPassword(arrayOf("L500"), 0, 100)

    assertEquals(0, result.first)
    assertEquals(5, result.second)
  }

  @Test
  fun `right full turn`() {
    val result = Day01_2.solveToPassword(arrayOf("R500"), 0, 100)

    assertEquals(0, result.first)
    assertEquals(5, result.second)
  }

  @ParameterizedTest
  @CsvSource(
    "8,1",
    "16,2",
    "8000,1000"
  )
  fun `turn x times wheel size`(stepsize: Int, turns: Int) {
    val result = Day01_2.solveToPassword(arrayOf("R$stepsize"), 0, 8)

    assertEquals(turns, result.second)
  }

  @Test
  fun solveTo434C49434B() {
    val sequence: Array<String> =
      arrayOf(
        "L68",
        "L30",
        "R48",
        "L5",
        "R60",
        "L55",
        "L1",
        "L99",
        "R14",
        "L82",
      )

    val result = Day01_2.solveToPassword(sequence, 50, 100)

    assertEquals(32, result.first)
    assertEquals(6, result.second)
  }
}
