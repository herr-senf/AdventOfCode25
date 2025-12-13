import java.io.File

fun main() {
  Day05.run()
}

object Day05 {

  /**
   * Reads the input file, creates an [Inventory] instance from its lines,
   * counts the number of ingredients marked as fresh, and prints a message
   * to the standard output. The printed text follows the format:
   * "Inventory contains X fresh ingredients", where *X* is the count of
   * fresh items found in the inventory.
   */
  fun run() {
    val input = readInputFile()
    val inventory = Inventory(input)

    val freshCount = inventory.freshCount

    println("Inventory contains $freshCount fresh ingredients")
  }

  /**
   * Reads the input file located at "Day05/src/input.txt" and returns its contents as a list of strings.
   *
   * Each element of the returned list represents one line from the file, preserving the original order.
   *
   * @return a list of strings, each string being a line from the input file
   */
  private fun readInputFile(): List<String> =
    File("Day05/src/input.txt")
      .readLines()
}
