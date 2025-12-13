import Day06_2.createBuckets
import Tools.readInputFile

fun main() {
  Day06_2.run()
}

object Day06_2 {

  /**
   * Executes the main routine of the application.
   *
   * The method orchestrates the following workflow:
   *
   * 1. Reads all lines from the input file using [Tools.readInputFile].
   * 2. Transforms those lines into a collection of [Bucket] objects
   *    via [createBuckets].
   * 3. Calculates the aggregated result of each bucket by invoking
   *    [Bucket.calculate] and sums those results.
   * 4. Prints the total sum to standard output in the format
   *    `The sum is <value>`.
   *
   * No return value is produced; the method writes the computed sum to the
   * console. It relies on the internal helper functions for reading, bucket
   * creation, and calculation logic, keeping the main flow concise and
   * readable.
   */
  fun run() {
    val lines = readInputFile()

    val buckets = createBuckets(lines)

    val sum = buckets.sumOf { it.calculate() }

    println("The sum is $sum")
  }

  /**
   * Transforms a list of input lines into a list of [Bucket] objects.
   *
   * All lines except the last are treated as rows of numeric values.
   * The final line contains operation signs that determine the operation
   * for each column. For each column, a [Bucket] is created that
   * associates the column's values with its operation. After creation,
   * each bucket is rotated via [Bucket.rotate] before being returned.
   *
   * @param lines the raw lines read from the input file
   * @return a list of rotated Bucket instances
   */
  private fun createBuckets(lines: List<String>): List<Bucket> {
    val numbers = lines
      .take(lines.size - 1)
      .toList()
    val operations = lines.last()
    val columnInfo = createColumnInfo(operations)

    val buckets = mutableListOf<Bucket>()
    for (info in columnInfo) {
      val values = numbers
        .map { line -> line.substring(info.start, info.start + info.width) }
        .toList()

      buckets += Bucket(info.operation, values)
    }

    return buckets.map { it.rotate() }
  }

  /**
   * Parses a string of operation signs into a list of [Column] objects.
   *
   * The input string consists of operation signs separated by spaces. Each sign
   * is mapped to an [Operation] using [Operation.bySign].  The resulting
   * list contains one column per sign.  The column's `start` property records
   * the index of the sign in the original string, and the `width` property
   * represents the number of consecutive spaces that follow the sign
   * (or one more than the last such count for the final column).
   *
   * @param operations the raw operation line read from the input; it
   *   contains operation signs separated by spaces
   * @return a list of [Column] instances that describe the position and
   *   span of each operation within the original string
   */
  private fun createColumnInfo(operations: String): List<Column> {
    val columns = mutableListOf<Column>()

    var i = 0
    var width = 0
    var currentColumn: Column? = null
    while (i < operations.length) {
      val ch = operations[i++]

      if (ch == ' ')
        width++
      else {
        if (currentColumn != null) {
          columns += currentColumn.copy(width = width)
        }
        currentColumn = Column(Operation.bySign("$ch"), i - 1)
        width = 0
      }
    }
    columns += currentColumn!!.copy(width = width + 1)

    return columns
  }

  /**
   * Represents a column within a bucket of values.
   *
   * A column is defined by the operation that will be applied to its values, the
   * starting index of the column in the original string list, and the width of
   * each value segment to be considered. The width defaults to zero, meaning
   * that the full string at the specified start position is used.
   *
   * @param operation the arithmetic operation to apply to the column's values
   * @param start the starting index of the column in the original list of strings
   * @param width the number of characters to include from each string; a value of
   *   zero indicates that the entire string is used
   */
  data class Column(val operation: Operation, val start: Int, val width: Int = 0)
}

