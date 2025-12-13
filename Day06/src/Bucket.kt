/**
 * A container that associates an arithmetic operation with a list of string values.
 *
 * The values are interpreted as long integers.  The class provides two primary operations:
 *
 * * **`calculate()`** – Converts each string to a long, then reduces the list by applying
 *   the associated operation from left to right.  For example, with the `PLUS` operation
 *   the result is the sum of all numeric values; with `MULTIPLY` it is their product.
 *
 * * **`rotate()`** – Produces a new bucket whose values are derived by reading the
 *   original strings column‑by‑column from the last character to the first,
 *   concatenating non‑space characters found in each column.  The resulting
 *   strings are returned as the new `values` list in the same order.
 *
 * Instances are immutable; `rotate()` returns a copy of the bucket with the
 * transformed values, leaving the original unchanged.
 */
data class Bucket(val operation: Operation, val values: List<String>) {

  /**
   * Computes the result of applying the bucket's operation to its values.
   *
   * The method parses each string in the bucket's [values] list to a
   * [Long]. It then reduces the sequence of numbers by repeatedly
   * invoking [Operation.calc] from left to right, producing a single
   * aggregated value.
   *
   * @return the aggregated Long result of the bucket's operation
   */
  fun calculate(): Long =
    values
      .map { it.toLong() }
      .reduce { left, right -> operation.calc(left, right) }

  /**
   * Creates a new bucket containing the values of the current bucket rotated
   * by reading the original strings column‑by‑column from the last character to
   * the first. Only non‑space characters are concatenated for each column.
   *
   * @return a copy of the bucket whose [values] list is the rotated result
   */
  fun rotate(): Bucket {
    val maxLength = values.maxOf { it.length }

    val result = mutableListOf<String>()
    for (l in maxLength downTo 1) {
      val temp = values
        .filter { it.length >= l }
        .map { it[l - 1] }
        .filter { it != ' ' }
        .joinToString(separator = "")

      if (temp.isNotBlank())
        result += temp
    }

    return this.copy(values = result)
  }
}

