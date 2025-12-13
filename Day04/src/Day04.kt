import java.io.File

fun main() {
  Day04.run()
}

object Day04 {

  /**
   * Reads the input file, constructs a [Field] from it, counts how many rolls are removable
   * (a roll is removable if it has fewer than four neighboring rolls), and prints the
   * total count in the format: "There are X rolls removable".
   */
  fun run() {
    val input = readInputFile()
    val field = Field(input)
    val removableCount = field.countRemovableRolls()

    println("There are $removableCount rolls removable")
  }

  /**
   * Reads the input file located at `Day04/src/input.txt` and returns a list of
   * non‑blank lines.
   *
   * @return A list of strings, each string representing a non‑empty line from
   * the input file.
   */
  private fun readInputFile(): List<String> =
    File("Day04/src/input.txt")
      .readLines()
      .filter { it.isNotBlank() }
}
