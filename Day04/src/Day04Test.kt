import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.CsvSource

class Day04Test {

  @Test
  fun `create empty 1x1 field and test for item count 0`() {
    val input = listOf(".")
    val field = Field(input)

    val dimension = field.dimension
    assertEquals(1, dimension.x)
    assertEquals(1, dimension.y)

    val count = field.paperRollCount
    assertEquals(0, count)
  }

  @Test
  fun `create filled 1x1 field and test for item count 1`() {
    val input = listOf("@")
    val field = Field(input)

    val dimension = field.dimension
    assertEquals(1, dimension.x)
    assertEquals(1, dimension.y)

    val count = field.paperRollCount
    assertEquals(1, count)
  }

  @Test
  fun `create filled 5x1 field and test for item count 5`() {
    val input = listOf("@@@@@")
    val field = Field(input)

    val dimension = field.dimension
    assertEquals(5, dimension.x)
    assertEquals(1, dimension.y)

    val count = field.paperRollCount
    assertEquals(5, count)
  }

  @Test
  fun `create mixed 5x1 field and test for item count 3`() {
    val input = listOf("@@..@")
    val field = Field(input)

    val dimension = field.dimension
    assertEquals(5, dimension.x)
    assertEquals(1, dimension.y)

    val count = field.paperRollCount
    assertEquals(3, count)
  }

  @Test
  fun `create mixed 5x2 field and test for item count 5`() {
    val input = listOf(
      "@@..@",
      "...@@",
    )
    val field = Field(input)

    val dimension = field.dimension
    assertEquals(5, dimension.x)
    assertEquals(2, dimension.y)

    val count = field.paperRollCount
    assertEquals(5, count)
  }

  @Test
  fun `mixed row length throws exception`() {
    val input = listOf(
      "@@..@..",
      "...@@",
    )
    assertThrows<IllegalArgumentException> { Field(input) }
  }

  @Test
  fun `invalid characters throw exception`() {
    val input = listOf(
      "@@12@",
      ".f.@@",
    )
    assertThrows<IllegalArgumentException> { Field(input) }
  }

  @Test
  fun `3x3 surrounded center has neighbor count 8`() {
    val input = listOf(
      "@@@",
      "@.@",
      "@@@",
    )
    val field = Field(input)

    val result = field.getNeighborCount(1, 1)

    assertEquals(8, result)
  }

  @Test
  fun `3x3 isolated center has neighbor count 0`() {
    val input = listOf(
      "...",
      ".@.",
      "...",
    )
    val field = Field(input)

    val result = field.getNeighborCount(1, 1)

    assertEquals(0, result)
  }

  @ParameterizedTest
  @CsvSource(
    "1,0",
    "1,2",
    "0,1",
    "2,1",

    "0,0",
    "2,0",
    "0,2",
    "2,2",
  )
  fun `neighbor at edge and corner is correct in isolated 3x3`(x: Int, y: Int) {
    val input = listOf(
      "...",
      ".@.",
      "...",
    )
    val field = Field(input)

    val result = field.getNeighborCount(x, y)

    assertEquals(1, result)
  }

  @ParameterizedTest
  @CsvSource(
    "0,1",
    "1,1",
    "2,2",
  )
  fun `test single line with moving window for correct neighbor count`(x: Int, count: Int) {
    val input = listOf(".@@@")
    val field = Field(input)

    val result = field.getNeighborCount(x, 0)

    assertEquals(count, result)
  }

  @ParameterizedTest
  @CsvSource(
    "1,0,2",
    "1,2,3",
    "0,1,5",
    "2,1,4",

    "0,0,2",
    "2,0,2",
    "0,2,2",
    "2,2,2",
  )
  fun `neighbor at edge and corner is correct in random 3x3`(x: Int, y: Int, count: Int) {
    val input = listOf(
      "@@.",
      ".@.",
      "@@@",
    )
    val field = Field(input)

    val result = field.getNeighborCount(x, y)

    assertEquals(count, result)
  }

  @ParameterizedTest
  @CsvSource(
    "0,false", // empty spot ✗
    "1,false", // empty spot ✗
    "2,false", // empty spot ✗
    "3,false", // 4 neighbors ✗
    "4,true", // 3 neighbors ✓
  )
  fun `removable rolls in 5x2 with pattern`(x: Int, isRemovable: Boolean) {
    val input = listOf(
      "...@@",
      "..@@@",
    )
    val field = Field(input)

    val result = field.isRemovable(x, 0)

    assertEquals(isRemovable, result)
  }

  @ParameterizedTest
  @CsvSource(
    "0,true", // 1 neighbor ✓
    "1,true", // 3 neighbor ✓
    "2,false", // 4 neighbor ✗
    "3,false", // 5 neighbors ✗
    "4,true", // 3 neighbors ✓
  )
  fun `removable rolls in 5x2 with solid first row`(x: Int, isRemovable: Boolean) {
    val input = listOf(
      "@@@@@",
      "..@@@",
    )
    val field = Field(input)

    val result = field.isRemovable(x, 0)

    assertEquals(isRemovable, result)
  }

  @ParameterizedTest
  @CsvSource(
    "0,0,true",
    "1,0,true",
    "2,0,false",

    "0,1,false",
    "1,1,false",
    "2,1,false",

    "0,2,true",
    "1,2,true",
    "2,2,true",
  )
  fun `count removable rolls in 3x3`() {
    val input = listOf(
      "@@.",
      ".@.",
      "@@@",
    )
    val field = Field(input)

    val result = field.countRemovableRolls()

    assertEquals(5, result)
  }

  @Test
  fun `test exercise example`() {
    val input = listOf(
      "..@@.@@@@.",
      "@@@.@.@.@@",
      "@@@@@.@.@@",
      "@.@@@@..@.",
      "@@.@@@@.@@",
      ".@@@@@@@.@",
      ".@.@.@.@@@",
      "@.@@@.@@@@",
      ".@@@@@@@@.",
      "@.@.@@@.@.",
    )
    val field = Field(input)

    val result=field.countRemovableRolls()

    assertEquals(13, result)
  }
}
