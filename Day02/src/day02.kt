import java.io.File

fun main() {
  val ranges = File("Day02/src/input.txt")
    .readLines()
    .flatMap { it.split(",") }
    .map { splitRange(it) }

  val invalids = ranges
    .flatMap { findInvalids(it) }
    .toSet()

  println("There are ${invalids.size} invalids.")

  val sum = invalids.sumOf { it.toLong() }
  println("Their sum is ${sum}.")
}

fun splitRange(string: String): Pair<Long, Long> {
  val result = string.split("-")

  assert(result.size == 2) { "Something went wrong" }

  return result[0].toLong() to result[1].toLong()
}

fun checkValid(text: String): Boolean {
  if (text.length % 2 != 0) return true

  val halfLength = text.length / 2
  val test = text.substring(0, halfLength)

  return text != test + test
}

fun findInvalids(range: Pair<Long, Long>): List<String> =
  (range.first..range.second)
    .asSequence()
    .map { it.toString() }
    .mapNotNull { if (checkValid(it)) null else it }
    .toList()
