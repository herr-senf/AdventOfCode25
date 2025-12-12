import Day01.solveToPassword
import java.io.File

fun main() {
  Day01.run()
}

object Day01 {

  /**
   * Executes the password-solving routine for Day01.
   *
   * The function reads the input file located at `Day01/src/input.txt`,
   * filtering out blank lines and converting the remaining lines into an array.
   * It then calls [solveToPassword] with a starting position of 50 and a wheel size
   * of 100. The result, a `Pair<Int, Int>`, is asserted to have the first element
   * equal to 3. If the assertion fails an `AssertionError` is thrown; otherwise
   * the result is printed to the console.
   *
   * The input file should contain movement steps such as "L5" or "R10", one per
   * line. Blank lines are ignored.
   */
  fun run() {
    val startPosition = 50
    val wheelSize = 100

    val sequence = File("Day01/src/input.txt")
      .readLines()
      .filter { it.isNotBlank() }
      .toTypedArray()

    val result = solveToPassword(sequence, startPosition, wheelSize)

    assert(result.first == 3) { "Result must be 3 but was $result" }

    println("Result is $result")
  }

  /**
   * Computes the final position on a circular wheel and counts how many times the wheel lands on position zero.
   *
   * @param sequence An array of strings representing movement steps. Each string begins with 'L' or 'R' followed
   *                 by a positive integer indicating how many positions to move left (negative) or right (positive).
   *                 For example, "L3" means move three positions to the left, "R5" means move five positions to the
   *                 right.
   * @param startPosition The starting position on the wheel. Positions are numbered from 0 to {@code wheelSize - 1}
   *                      inclusive.
   * @param wheelSize The total number of positions on the wheel. Movements wrap around so that positions are taken
   *                  modulo {@code wheelSize}.
   * @return A [Pair] where the first component is the final position after all movements and the second component is
   *         the number of times the wheel was at position zero during the sequence.
   */
  fun solveToPassword(sequence: Array<String>, startPosition: Int, wheelSize: Int): Pair<Int, Int> {
    var current = startPosition
    var zeroCount = 0

    for (step in sequence) {
      val direction = if (step[0] == 'L') -1 else 1
      val stepValue = step.substring(1).toInt() * direction

      var next = current + stepValue
      if (next < 0) {
        while (next < 0) next += wheelSize
      } else if (next >= wheelSize) {
        while (next >= wheelSize)
          next -= wheelSize
      }

      if (next == 0) zeroCount++

      current = next
    }

    return current to zeroCount
  }
}
