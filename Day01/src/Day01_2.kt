import Day01_2.solveToPassword
import java.io.File

fun main() {
  Day01_2.run()
}

object Day01_2 {

  /**
   * Executes the password calculation routine.
   *
   * Reads a sequence of steps from the default input file, reports the number of steps,
   * calls [solveToPassword] with a starting position of 50 on a wheel of size 100, and prints
   * the resulting password (the count of zero crossings).
   *
   * This function is intended as an entry point and writes its results directly to the
   * standard output.
   */
  fun run() {
    val startPosition = 50
    val wheelSize = 100

    val sequence = readFileToSequence()

    println("Using ${sequence.size} steps")

    val result = solveToPassword(sequence, startPosition, wheelSize)

    println("Password is ${result.second}")
  }

  /**
   * Moves a circular wheel according to a sequence of step commands.
   *
   * The wheel has a size of [wheelSize] positions, numbered from 0 to `wheelSize - 1`.
   * Each element in [sequence] represents a movement, starting with either `L` (left) or `R`
   * (right) followed by an integer that indicates how many steps to move in that direction.
   * Positive values move to the right, negative values move to the left.  When the wheel
   * passes the first or last position, it wraps around using modular arithmetic.
   *
   * During the execution, every time the wheel lands on position `0` the counter
   * `zeroCount` is incremented.  The counter also accounts for intermediate wraps
   * that land exactly on `0`.  The initial position is not counted unless the wheel
   * immediately starts at `0` and a later step brings it back to `0`.
   *
   * @param sequence an array of strings describing each step, e.g. `"L3"` or `"R10"`
   * @param startPosition the wheel's starting position (0 ≤ startPosition < wheelSize)
   * @param wheelSize the total number of positions on the wheel
   * @return a `Pair<Int, Int>` where `first` is the final position of the wheel and
   * `second` is the number of times the wheel visited position zero during the sequence
   */
  fun solveToPassword(sequence: Array<String>, startPosition: Int, wheelSize: Int): Pair<Int, Int> {
    var position = startPosition
    var zeroCount = 0

    /**
     * Normalizes an overflowing wheel [position] back into the valid range `0..<(wheelSize)`.
     *
     * This lambda applies wrap-around behavior for a circular wheel by repeatedly subtracting
     * [wheelSize] while [position] is greater than or equal to [wheelSize]. Each subtraction
     * corresponds to one full wrap (one complete revolution), and increments [zeroCount] once
     * per wrap.
     *
     * The lambda captures and mutates the surrounding variables [position] and [zeroCount].
     *
     * @receiver None (stored in a local val as a `() -> Unit`)
     * @throws Nothing explicitly; assumes [wheelSize] is positive to terminate.
     */
    val regulateOverflow: () -> Unit = {
      while (position >= wheelSize) {
        position -= wheelSize
        zeroCount++
      }
    }

    /**
     * Normalizes an underflowing wheel [position] back into the valid range `0..<(wheelSize)`.
     *
     * This lambda applies wrap-around behavior for a circular wheel when the current [position]
     * becomes negative. It repeatedly adds [wheelSize] until [position] is within bounds.
     *
     * Zero-crossing accounting:
     * - Each *additional* wrap that occurs while recovering from underflow increments [zeroCount].
     * - The [positionWasZero] flag indicates whether the move started at position `0`. The first
     *   wrap triggered by leaving `0` is treated specially to avoid double-counting a `0` visit.
     * - After normalization, if the final normalized [position] is exactly `0`, [zeroCount] is
     *   incremented once to record landing on zero.
     *
     * This lambda captures and mutates the surrounding variables [position], [wheelSize], and
     * [zeroCount].
     *
     * @param positionWasZero `true` if the wheel was at position `0` before applying the step that
     * caused the underflow; used to prevent counting the initial departure/first wrap as an extra
     * zero-crossing.
     * @throws Nothing explicitly; assumes [wheelSize] is positive to guarantee termination.
     */
    val regulateUnderflow: (Boolean) -> Unit = { positionWasZero ->
      var temp = positionWasZero
      while (position < 0) {
        position += wheelSize
        if (temp) temp = false else zeroCount++
      }
      if (position == 0) zeroCount++
    }

    for (step in sequence) {
      val stepDelta = parseStepDelta(step)
      val startedAtZero = position == 0

      position += stepDelta

      when {
        position < 0 -> regulateUnderflow(startedAtZero)
        position >= wheelSize -> regulateOverflow()
        position == 0 -> zeroCount++
      }
    }

    return position to zeroCount
  }

  /**
   * Reads the puzzle input file located at "Day01/src/input.txt", removes blank lines,
   * and returns the remaining lines as an array of strings.
   *
   * @return an array containing each non‑blank line from the input file.
   */
  private fun readFileToSequence(): Array<String> =
    File("Day01/src/input.txt")
      .readLines()
      .filter { it.isNotBlank() }
      .toTypedArray<String>()

  /**
   * Converts a step description into a signed integer delta.
   *
   * @param step a non‑empty string where the first character is 'L' or 'R'
   *             indicating left or right, followed by the numeric value of the step.
   * @return the integer delta, negative for left steps and positive for right steps.
   */
  private fun parseStepDelta(step: String): Int {
    val direction = if (step[0] == 'L') -1 else 1

    return step.substring(1).toInt() * direction
  }
}
