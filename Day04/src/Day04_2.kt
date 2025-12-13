import java.io.File

fun main() {
  Day04_2.run()
}

object Day04_2 {

  /**
   * Reads the input file located at `Day04/src/input.txt`, constructs a [Field] from the
   * data, repeatedly removes rolls that have fewer than four neighboring rolls until
   * no such rolls remain, and prints a summary. The removal process is performed by
   * [Field.removeUntilSolid], which returns a triple containing the final field,
   * the iteration count, and the cumulative number of removed rolls. The method
   * prints a message in the format: "There are X rolls removable after Y iterations."
   */
  fun run() {
    val input = readInputFile()
    val field = Field(input)
    val result = Field.removeUntilSolid(field)

    println("There are ${result.third} rolls removable after ${result.second} iterations.")
  }

  /**
   * Reads the input file located at `Day04/src/input.txt` and returns a list of
   * non‑blank lines from that file.
   *
   * @return A list of strings, each representing a line that contains at least
   *         one non‑whitespace character.
   */
  private fun readInputFile(): List<String> =
    File("Day04/src/input.txt")
      .readLines()
      .filter { it.isNotBlank() }
}
