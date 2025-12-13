import java.io.File

fun main() {
  Day04.run()
}

object Day04 {
  fun run() {
    val input = readInputFile()
    val field = Field(input)
    val removableCount = field.countRemovableRolls()

    println("There are $removableCount rolls removable")
  }

  private fun readInputFile(): List<String> =
    File("Day04/src/input.txt")
      .readLines()
      .filter { it.isNotBlank() }
}
