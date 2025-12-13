class Inventory(lines: List<String>) {

  private val _freshIngredients = mutableListOf<LongRange>()
  val freshIngredients: List<LongRange>
    get() = _freshIngredients
  private val ingredientMap = mutableMapOf<Long, Boolean>()

  val ingredientCount: Int
    get() = ingredientMap.size
  val freshCount: Int by lazy { ingredientMap.count { it.value } }

  init {
    val iterator = lines.iterator()
    var line = iterator.next()

    while (line.isNotBlank()) {
      _freshIngredients += parseRange(line)

      line = iterator.next()
    }

    while (iterator.hasNext()) {
      line = iterator.next()
      val id = line.toLong()

      ingredientMap[id] = _freshIngredients.any { it.contains(id) }
    }
  }

  private fun parseRange(range: String): LongRange {
    try {
      val split = range.split("-")
      return LongRange(split[0].toLong(), split[1].toLong())
    } catch (e: Exception) {
      println("Error parsing range $range")
      throw e
    }
  }

  fun isFresh(id: Long): Boolean =
    ingredientMap[id] ?: false
}

object Combiner {

  fun combine(ranges: List<LongRange>): List<LongRange> {
    val result = mutableListOf<LongRange>()

    for (range in ranges.sortedBy { it.first }) {
      val containsLeft = result.firstOrNull { it.contains(range.first) || it.contains(range.first - 1) }

      if (containsLeft != null) {
        if (containsLeft.contains(range.last)) continue // the current range is completely contained, can be omitted

        result.remove(containsLeft)
        result += LongRange(containsLeft.first, range.last)
      } else
        result += range
    }

    return result.sortedBy { it.first }
  }
}
