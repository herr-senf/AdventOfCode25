import java.io.File

fun main() {
  Day05.run()
}

object Day05 {

  fun run() {
    val input = readInputFile()
    val inventory = Inventory(input)

    val freshCount = inventory.freshCount

    println("Inventory contains $freshCount fresh ingredients")
  }

  private fun readInputFile(): List<String> =
    File("Day05/src/input.txt")
      .readLines()
}
