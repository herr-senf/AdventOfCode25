import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.CsvSource

class Day05Test {

  @Test
  fun `a simple inventory can be created`() {
    val input = listOf(
      "1-1",
      "",
      "1"
    )

    val inventory = Inventory(input)

    assertEquals(1, inventory.ingredientCount)
  }

  @ParameterizedTest
  @CsvSource(
    "1,false",
    "5,true",
    "8,false",
    "11,true",
    "17,true",
    "32,false",
  )
  fun `test exercise example`(id: Long, fresh: Boolean) {
    val input = listOf(
      "3-5",
      "10-14",
      "16-20",
      "12-18",
      "",
      "1",
      "5",
      "8",
      "11",
      "17",
      "32",
    )

    val inventory = Inventory(input)

    assertEquals(6, inventory.ingredientCount)
    assertEquals(3, inventory.freshCount)

    assertEquals(fresh, inventory.isFresh(id))
  }
}

