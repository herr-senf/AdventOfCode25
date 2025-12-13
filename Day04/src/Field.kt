class Field(private val input: List<String>) {
  val dimension by lazy { Dimension(input[0].length, input.size) }

  val paperRollCount = input.sumOf { row -> row.count { cell -> cell == '@' } }

  init {
    val rowLength = input[0].length
    var consistent = input.map { it.length }.all { it == rowLength }
    consistent = consistent && input.all { row -> row.all { cell -> cell == '@' || cell == '.' } }

    require(consistent) { throw IllegalArgumentException() }
  }

  fun getNeighborCount(x: Int, y: Int): Int {
    return (0
      + intRoll(x + 1, y) + intRoll(x - 1, y)
      + intRoll(x, y + 1) + intRoll(x, y - 1)
      + intRoll(x + 1, y + 1) + intRoll(x - 1, y + 1)
      + intRoll(x + 1, y - 1) + intRoll(x - 1, y - 1)
      )
  }

  private fun isRoll(x: Int, y: Int): Boolean {
    if (x < 0 || x >= dimension.x) return false
    if (y < 0 || y >= dimension.y) return false

    return input[y][x] == '@'
  }

  private fun intRoll(x: Int, y: Int): Int =
    if (isRoll(x, y)) 1 else 0

  fun countRemovableRolls(): Int {
    var count = 0
    for (y in 0 until dimension.y) {
      for (x in 0 until dimension.x) {
        count += if (isRemovable(x, y)) 1 else 0
      }
    }
    return count
  }

  fun isRemovable(x: Int, y: Int): Boolean =
    isRoll(x, y) && getNeighborCount(x, y) < 4

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

  fun isEmpty(): Boolean =
    input.all { row -> row.all { cell -> cell == '.' } }

  companion object {
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
}

data class Dimension(val x: Int = 0, val y: Int = 0)
