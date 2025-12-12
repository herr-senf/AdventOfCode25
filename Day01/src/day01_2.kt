import java.io.File

fun main() {
  val startPosition = 50
  val wheelSize = 100

  val sequence = readFileToSequence()

  println("Using ${sequence.size} steps")

  val result = solveToPassword434C49434B(sequence, startPosition, wheelSize)

  println("Password is ${result.second}")
}

fun solveToPassword434C49434B(sequence: Array<String>, startPosition: Int, wheelSize: Int): Pair<Int, Int> {
  var position = startPosition
  var zeroCount = 0

  val regulateOverflow: () -> Unit = {
    while (position >= wheelSize) {
      position -= wheelSize
      zeroCount++
    }
  }

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

private fun readFileToSequence(): Array<String> {
  val sequence = File("Day01/src/input.txt")
    .readLines()
    .filter { it.isNotBlank() }
    .toTypedArray()
  return sequence
}

private fun parseStepDelta(step: String): Int {
  val direction = if (step[0] == 'L') -1 else 1
  val stepValue = step.substring(1).toInt() * direction
  return stepValue
}
