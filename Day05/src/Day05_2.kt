import java.io.File

fun main() {
  Day05_2.run()
}

object Day05_2 {

  fun run() {
    val input = readInputFile()
    val inventory = Inventory(input)

    val ranges = inventory.freshIngredients

    val totalCount = Combiner
      .combine(ranges)
      .sumOf { it.size() }

    println("Inventory knows $totalCount fresh ingredients")
  }

  private fun readInputFile(): List<String> =
    File("Day05/src/input.txt")
      .readLines()
}

fun LongRange.size(): Long = last - first + 1
