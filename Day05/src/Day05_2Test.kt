import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test

class Day05_2Test {

  @Test
  fun `non-overlapping ranges don't combine`() {
    val r1 = LongRange(0, 9)
    val r2 = LongRange(20, 29)
    val ranges = listOf(r1, r2)

    val result = Combiner.combine(ranges)

    assertEquals(ranges, result)
  }

  @Test
  fun `non-overlapping ranges (different order) don't combine`() {
    val r1 = LongRange(0, 9)
    val r2 = LongRange(20, 29)
    val ranges = listOf(r1, r2)

    val result = Combiner.combine(listOf(r2, r1))

    assertEquals(ranges, result)
  }

  @Test
  fun `combine touching ranges`() {
    val r1 = LongRange(0, 9)
    val r2 = LongRange(10, 29)
    val ranges = listOf(r1, r2)

    val result = Combiner.combine(ranges)

    assertEquals(listOf(LongRange(0, 29)), result)
  }

  @Test
  fun `combine overlapping ranges`() {
    val r1 = LongRange(0, 9)
    val r2 = LongRange(7, 29)
    val ranges = listOf(r1, r2)

    val result = Combiner.combine(ranges)

    assertEquals(listOf(LongRange(0, 29)), result)
  }

  @Test
  fun `combine identical ranges`() {
    val r1 = LongRange(0, 29)
    val r2 = LongRange(0, 29)
    val ranges = listOf(r1, r2)

    val result = Combiner.combine(ranges)

    assertEquals(listOf(LongRange(0, 29)), result)
  }

  @Test
  fun `test exercise example`() {
    val r1 = LongRange(3, 5)
    val r2 = LongRange(10, 14)
    val r3 = LongRange(16, 20)
    val r4 = LongRange(12, 18)

    val result = Combiner.combine(listOf(r1, r2, r3, r4))

    assertEquals(listOf(r1, LongRange(10, 20)), result)
  }
}
