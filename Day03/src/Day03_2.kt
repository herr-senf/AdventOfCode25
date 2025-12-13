import Day03_2.getMaxJoltage
import Day03_2.readBanks
import java.io.File
import kotlin.math.max

fun main() {
  Day03_2.run()
}

object Day03_2 {

  /**
   * Calculates and displays the total joltage for all banks listed in the input file.
   *
   * The function first retrieves a list of bank strings via [readBanks].
   * For each bank it calculates the maximum joltage value using [getMaxJoltage].
   * The resulting values are summed and the final total is printed to standard output.
   *
   * The output format is: `The sum of all cells in the banks is X` where X is the computed sum.
   *
   * @see readBanks
   * @see getMaxJoltage
   */
  fun run() {
    val banks = readBanks()

    val summedJoltage = banks.sumOf { getMaxJoltage(it) }

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
   * Calculates the maximum joltage value from a given bank string.
   *
   * The function repeatedly removes characters from the input string until it is reduced to
   * a length of at most 12. In each iteration the leftmost character that is smaller than
   * the character immediately to its right is removed; if no such character exists, the
   * smallest character in the string is removed instead. After the loop terminates,
   * the first 12 characters of the remaining string are parsed into a `Long` and returned.
   *
   * @param bank A string representing a bank of characters from which to compute the joltage.
   * @return The joltage value represented by the first 12 characters of the processed string,
   * parsed as a `Long`.
   */
  fun getMaxJoltage(bank: String): Long {
    var result = bank
    var index = 0

    while (result.length > 12 && index < result.length) {
      if (index == result.length - 1) {
        val minChar = result.min()
        val leastChatIndex = result.indexOfLast { it == minChar }
        result = result.removeRange(leastChatIndex, leastChatIndex + 1)
        index = 0
        continue
      }

      val left = result[index]
      val right = result[index + 1]

      if (left >= right) {
        index++
      } else {
        result = result.removeRange(index, index + 1)
        index = max(0, index - 1)
      }
    }

    return result.substring(0, 12).toLong()
  }
}
