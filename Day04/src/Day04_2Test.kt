import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertInstanceOf

class Day04_2Test {

  @Test
  fun `removing removalbe rolls results right round`() {
    val input = listOf(
      // current || next
      "@.....",  // "......",
      "@.....",  // "......",
      "@.@@@.",  // "...@..",
      "@.@@@.",  // "..@@@.",
      "@.@@@.",  // "...@..",
      "@.....",  // "......",
    )
    val field = Field(input)
    val removableCount = field.countRemovableRolls()

    assertEquals(10, removableCount)

    val nextField = field.removeRolls().first

    assertInstanceOf<Field>(nextField)

    val nextRemovableCount = nextField.countRemovableRolls()

    assertEquals(4, nextRemovableCount)
  }

  @Test
  fun `test empty field is empty`() {
    val input = listOf(
      "...",
      "...",
      "...",
    )
    val field = Field(input)

    assertEquals(true, field.isEmpty())
  }

  @Test
  fun `test non-empty field is not empty`() {
    val input = listOf(
      "...",
      ".@.",
      "...",
    )
    val field = Field(input)

    assertEquals(false, field.isEmpty())
  }

  @Test
  fun `count steps for iterative removing`() {
    val input = listOf(
      "@.....",
      "@.....",
      "@.@@@.",
      "@.@@@.",
      "@.@@@.",
      "@.....",
    )
    val field = Field(input)

    val result = Field.removeUntilSolid(field)

    assertInstanceOf<Triple<Field, Int, Int>>(result)

    assertEquals(true, result.first.isEmpty()) // the resulting field should be empty
    assertEquals(3, result.second) // it should have taken 3 iterations to clear the field
    assertEquals(15, result.third) // it should have removed 15 rolls in total
  }
}
