import java.io.File

fun main() {
  Day04_2.run()
}

object Day04_2 {
  fun run() {
    val input = readInputFile()
    val field = Field(input)
    val result = Field.removeUntilSolid(field)

    println("There are ${result.third} rolls removable after ${result.second} iterations.")
  }

  private fun readInputFile(): List<String> =
    File("Day04/src/input.txt")
      .readLines()
      .filter { it.isNotBlank() }
}
