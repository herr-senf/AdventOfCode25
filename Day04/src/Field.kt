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
      + isRoll(x + 1, y) + isRoll(x - 1, y)
      + isRoll(x, y + 1) + isRoll(x, y - 1)
      + isRoll(x + 1, y + 1) + isRoll(x - 1, y + 1)
      + isRoll(x + 1, y - 1) + isRoll(x - 1, y - 1)
      )
  }

  private fun isRoll(x: Int, y: Int): Int {
    if (x < 0 || x >= dimension.x) return 0
    if (y < 0 || y >= dimension.y) return 0

    return if (input[y][x] == '@') 1 else 0
  }

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
    isRoll(x, y) == 1 && getNeighborCount(x, y) < 4
}

data class Dimension(val x: Int = 0, val y: Int = 0)
