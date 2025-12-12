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

    var next = current + stepValue
    when {
      next < 0 ->
        while (next < 0) {
          next += wheelSize
          if (current != 0) zeroCount++
        }

      next >= wheelSize ->
        while (next >= wheelSize) {
          next -= wheelSize
          if (current != 0) zeroCount++
        }

      next == 0 -> if (current != 0) zeroCount++
    }

    current = next
  }

  return current to zeroCount
}
