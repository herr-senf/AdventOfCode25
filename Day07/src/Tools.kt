import java.io.File

object Tools {

  /**
   * Reads the input file located at `Day07/src/input.txt` and returns its content as a list of strings.
   *
   * Each element of the returned list corresponds to a single line from the file.
   *
   * @return a list containing all lines from the input file
   */
  fun readInputFile(): List<String> =
    File("Day07/src/input.txt")
      .readLines()
}
