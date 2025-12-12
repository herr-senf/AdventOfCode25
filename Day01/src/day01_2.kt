import java.io.File

fun main() {
  val startPosition = 50
  val wheelSize = 100

  val sequence = File("Day01/src/input.txt")
    .readLines()
    .filter { it.isNotBlank() }
    .toTypedArray()

  val result = solveToPassword434C49434B(sequence, startPosition, wheelSize)

  println("Password is ${result.second}")
}

fun solveToPassword434C49434B(sequence: Array<String>, startPosition: Int, wheelSize: Int): Pair<Int, Int> {
  var current = startPosition
  var zeroCount = 0

  for (step in sequence) {
    val direction = if (step[0] == 'L') -1 else 1
    val stepValue = step.substring(1).toInt() * direction

    var currentWasZero = current == 0
    current += stepValue

    when {
      current < 0 ->
        while (current < 0) {
          current += wheelSize
          if (currentWasZero) currentWasZero = false else zeroCount++
        }

      current >= wheelSize ->
        while (current >= wheelSize) {
          current -= wheelSize
          zeroCount++
        }

      current == 0 -> zeroCount++
    }
  }

  return current to zeroCount
}
