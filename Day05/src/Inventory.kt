class Inventory(lines: List<String>) {

  private val freshIngredients = mutableListOf<LongRange>()
  private val ingredientMap = mutableMapOf<Long, Boolean>()

  val ingredientCount: Int
    get() = ingredientMap.size
  val freshCount: Int by lazy { ingredientMap.count { it.value } }

  init {
    val iterator = lines.iterator()
    var line = iterator.next()

    while (line.isNotBlank()) {
      freshIngredients += parseRange(line)

      line = iterator.next()
    }

    while (iterator.hasNext()) {
      line = iterator.next()
      val id = line.toLong()

      ingredientMap[id] = freshIngredients.any { it.contains(id) }
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
