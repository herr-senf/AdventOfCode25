import Tools.readInputFile

fun main() {
  Day06.run()
}

object Day06 {

  /**
   * Executes the main processing routine.
   *
   * The function performs the following steps in order:
   *
   * 1. Calls [readInputFile] to load all lines from the input file.
   * 2. Passes the loaded lines to [createBuckets] to transform the textual data into
   *    a list of [Bucket] instances, each containing an operation and a list of
   *    string values.
   * 3. Calculates the result for each bucket by invoking its [Bucket.calculate]
   *    method and aggregates these results into a single sum using `sumOf`.
   * 4. Outputs the computed sum to the standard output in the form
   *    `The sum is <value>`.
   *
   * This method orchestrates the overall workflow without exposing the internal
   * implementation details of reading, bucket creation, or calculation logic.
   */
  fun run() {
    val lines = readInputFile()

    val buckets = createBuckets(lines)

    val sum = buckets.sumOf { it.calculate() }

    println("The sum is $sum")
  }

  /**
   * Transforms a raw list of strings into a list of [Bucket] instances.
   *
   * Each element of `lines` represents a row of values separated by spaces.
   * The method interprets all rows except the last one as numeric values,
   * converting each whitespace‑separated token into a string.  The last
   * line contains a sequence of operation signs; each sign is mapped to an
   * [Operation] using `Operation.bySign`.  A bucket is created for each
   * column of values, associating the corresponding operation with the
   * list of values from that column.
   *
   * @param lines a list of strings read from the input file, where
   *   all lines except the last contain numeric strings and the final
   *   line contains operation signs separated by spaces
   *
   * @return a list of [Bucket] objects, each containing an operation
   *   and the list of string values belonging to its column
   */
  private fun createBuckets(lines: List<String>): List<Bucket> {
    val numbers = lines
      .take(lines.size - 1)
      .map { line ->
        line
          .split(" ")
          .filter { it.isNotBlank() }
      }
      .toList()
    val operations = lines
      .last()
      .split(" ")
      .filter { it.isNotBlank() }
      .map { Operation.bySign(it) }
      .toList()


    val buckets = mutableListOf<Bucket>()

    for (i in operations.indices) {
      val values = numbers.map { it[i] }.toList()

      buckets += Bucket(operations[i], values)
    }

    return buckets
  }
}
