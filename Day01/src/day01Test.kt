import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test

class Day01Test {

  @Test
  fun `start at 11 add 8 to right results in 19`() {
    val result = solveToPassword(arrayOf("R8"), 11, 100)

    assertEquals(19, result.first)
  }

  @Test
  fun `start at 5 add 10 to left results in 95`() {
    val result = solveToPassword(arrayOf("L10"), 5, 100)

    assertEquals(95, result.first)
  }

  @Test
  fun solveToDemo() {
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

    val result = solveToPassword(sequence, 50, 100)

    assertEquals(32, result.first)
    assertEquals(3, result.second)
  }

  @Test
  fun `use 434C49434B generation with R1000`() {
    val result = solveToPassword434C49434B(arrayOf("R1000"), 50, 100)

    assertEquals(10, result.second)
    assertEquals(50, result.first)
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

    val result = solveToPassword434C49434B(sequence, 50, 100)

    assertEquals(32, result.first)
    assertEquals(6, result.second)
  }
}
