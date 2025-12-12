import java.io.File

fun main() {
  Day03.run()
}

object Day03 {

  /**
   * Calculates the sum of all joltage values for each bank read from the input file.
   *
   * The method reads a list of banks using [readBanks], then
   * transforms each bank into its joltage representation with [getJoltage].
   * Each joltage string is parsed into a `Long`, the values are summed,
   * and the total is printed to the console with a descriptive message.
   *
   * The input file is located at `Day03/src/input.txt` and contains one bank per
   * non‑blank line.
   *
   * @see #readBanks()
   * @see #getJoltage(String)
   */
  fun run() {
    val banks = readBanks()

    val summedJoltage = banks
      .map { getJoltage(it) }
      .sumOf { it.toLong() }

    println("The sum of all cells in the banks is $summedJoltage")
  }

  /**
   * Reads the file "Day03/src/input.txt" and returns all non‑blank lines as a list of strings.
   *
   * @return List<String> of non‑blank lines from the file
   */
  private fun readBanks(): List<String> =
    File("Day03/src/input.txt")
      .readLines()
      .filter { it.isNotBlank() }

  /**
   * Returns a string composed of the largest character in the given `bank` string (excluding the last character)
   * followed by the largest character after the position of that largest character.
   *
   * @param bank the input string representing a bank of characters.
   * @return a concatenated string of the two largest characters found as described above.
   */
  fun getJoltage(bank: String): String {
    val left = getFirstDigit(bank)
    val right = getSecondDigit(bank, left.second + 1)

    return "${left.first}${right}"
  }

  /**
   * Returns the largest character in the given [bank] string and its index.
   *
   * @param bank the input string to search.
   * @return a pair containing the largest character and its zero-based index.
   */
  private fun getFirstDigit(bank: String): Pair<Char, Int> =
    bank
      .toCharArray()
      .dropLast(1)
      .mapIndexed { index, ch -> ch to index }
      .maxBy { it.first }

  /**
   * Returns the maximum character from the given bank string after skipping the specified number of characters.
   *
   * @param bank the bank string from which to extract the character
   * @param skipToIndex the number of characters to skip from the beginning of the string
   * @return the maximum character found in the remaining portion of the string
   */
  private fun getSecondDigit(bank: String, skipToIndex: Int): Char =
    bank
      .toCharArray()
      .drop(skipToIndex)
      .maxBy { it }
}
