import java.io.File

fun main() {
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
