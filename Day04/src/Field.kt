/**
 * A two‑dimensional field of paper rolls.
 *
 * The field is created from a list of strings, where each string represents one row of the field.
 * Cells containing a roll are denoted by `@` and empty cells by `.`.
 *
 * The class provides functions to analyze the configuration of rolls, determine which rolls can be
 * removed (a roll is removable if it has fewer than four neighboring rolls), and to iteratively
 * remove removable rolls until the field becomes stable.
 *
 * @property input The original list of strings used to build the field.
 * @property dimension The width (x) and height (y) of the field, computed lazily from the input.
 * @property paperRollCount The total number of rolls present in the field.
 */
class Field(private val input: List<String>) {

  /**
   * Lazily initializes the dimensions of the field.
   *
   * The width (`x`) is determined by the length of the first row of the input,
   * while the height (`y`) corresponds to the total number of rows.
   * This guarantees that the `Dimension` instance accurately reflects the
   * actual size of the underlying grid used to construct the [Field].
   */
  val dimension by lazy { Dimension(input[0].length, input.size) }

  /**
   * Counts the total number of paper rolls in the field.
   *
   * The value is computed lazily by summing, for each row of the input, the number of
   * cells that contain the `'@'` character. Once initialized, the property remains
   * unchanged for the lifetime of the [Field] instance.
   */
  val paperRollCount by lazy { input.sumOf { row -> row.count { cell -> cell == '@' } } }

  init {
    val rowLength = input[0].length
    var consistent = input.map { it.length }.all { it == rowLength }
    consistent = consistent && input.all { row -> row.all { cell -> cell == '@' || cell == '.' } }

    require(consistent) { throw IllegalArgumentException() }
  }

  /**
   * Returns the number of neighboring rolls around the cell at coordinates (x, y).
   *
   * @param x the horizontal coordinate of the target cell
   * @param y the vertical coordinate of the target cell
   * @return the count of adjacent rolls, considering all eight surrounding cells
   */
  fun getNeighborCount(x: Int, y: Int): Int {
    return (0
      + intRoll(x + 1, y) + intRoll(x - 1, y)
      + intRoll(x, y + 1) + intRoll(x, y - 1)
      + intRoll(x + 1, y + 1) + intRoll(x - 1, y + 1)
      + intRoll(x + 1, y - 1) + intRoll(x - 1, y - 1)
      )
  }

  /**
   * Checks whether the cell at the given coordinates contains a roll.
   *
   * @param x the horizontal coordinate of the cell
   * @param y the vertical coordinate of the cell
   * @return `true` if the coordinates are within the field bounds and the cell
   * contains a roll (`'@'`); otherwise `false`
   */
  private fun isRoll(x: Int, y: Int): Boolean {
    if (x < 0 || x >= dimension.x) return false
    if (y < 0 || y >= dimension.y) return false

    return input[y][x] == '@'
  }

  /**
   * Returns `1` if the cell at the specified coordinates contains a roll, otherwise returns `0`.
   *
   * @param x the horizontal coordinate of the cell to check
   * @param y the vertical coordinate of the cell to check
   * @return `1` if a roll exists at `(x, y)`; otherwise `0`
   */
  private fun intRoll(x: Int, y: Int): Int =
    if (isRoll(x, y)) 1 else 0

  /**
   * Counts all rolls that can be removed from the field.
   *
   * A roll is considered removable if it is present at the given coordinates
   * and has fewer than four neighboring rolls. The method iterates over every
   * cell in the field's dimensions and accumulates the number of removable
   * rolls.
   *
   * @return the total number of removable rolls in the current field
   */
  fun countRemovableRolls(): Int {
    var count = 0
    for (y in 0 until dimension.y) {
      for (x in 0 until dimension.x) {
        count += if (isRemovable(x, y)) 1 else 0
      }
    }
    return count
  }

  /**
   * Determines whether a roll at the specified coordinates can be removed.
   *
   * A roll is considered removable if it exists at the given location and has fewer than four
   * neighboring rolls. This check accounts for the field boundaries through the [isRoll]
   * helper method and counts adjacent rolls using [getNeighborCount].
   *
   * @param x the horizontal coordinate of the target cell
   * @param y the vertical coordinate of the target cell
   * @return `true` if the cell contains a roll, and it has less than four neighboring rolls; otherwise `false`
   */
  fun isRemovable(x: Int, y: Int): Boolean =
    isRoll(x, y) && getNeighborCount(x, y) < 4

  /**
   * Removes all removable rolls from the field.
   *
   * A roll is considered removable when it is present at a cell and has fewer than four
   * neighboring rolls. The method iterates over every cell in the field, replaces each
   * removable roll with a dot ('.'), counts how many rolls were removed, and builds a
   * new field representation with the updated rows.
   *
   * @return A [Pair] where the first component is the new [Field] with removable rolls
   * removed and the second component is the number of rolls that were removed.
   */
  fun removeRolls(): Pair<Field, Int> {
    val result = mutableListOf<String>()
    var removed = 0

    for (y in 0 until dimension.y) {
      var row = ""
      for (x in 0 until dimension.x) {
        row += (
          if (!isRoll(x, y))
            '.'
          else
            if (isRemovable(x, y)) {
              removed++
              '.'
            } else
              '@'
          )
      }
      result.add(row)
    }

    return Field(result.toList()) to removed
  }

  /**
   * Determines whether the field contains no rolls.
   *
   * @return `true` if every cell in the input grid is empty (`'.'`);
   * otherwise `false`.
   */
  fun isEmpty(): Boolean =
    input.all { row -> row.all { cell -> cell == '.' } }

  companion object {

    /**
     * Repeatedly removes removable rolls from the given field until no more can be removed.
     *
     * @param field the initial state of the field from which removable rolls are to be removed
     * @return a [Triple] where the first component is the final field after all removable rolls have been removed,
     *         the second component is the number of removal iterations performed,
     *         and the third component is the total number of rolls removed
     */
    fun removeUntilSolid(field: Field): Triple<Field, Int, Int> {
      var result = field
      var iterations = 0
      var removeCount = 0

      while (result.countRemovableRolls() > 0) {
        iterations++

        val temp = result.removeRolls()

        removeCount += temp.second
        result = temp.first
      }

      return Triple(result, iterations, removeCount)
    }
  }

  /**
   * Represents a two‑dimensional coordinate or size.
   *
   * This immutable data class holds integer values for the horizontal (x) and vertical (y)
   * components. Default values are zero, making it convenient for use as a default
   * dimension or origin point.
   */
  data class Dimension(val x: Int = 0, val y: Int = 0)
}
