/**
 * Represents a binary arithmetic operation that can be performed on two [Long] operands.
 *
 * Each enum constant implements the operation by overriding [calc].  Operations are
 * identified by a textual sign (for example `"+"` or `"*"`), which can be used to
 * look up the constant through [bySign].
 */
enum class Operation(private val sign: String) {
  PLUS("+") {
    override fun calc(left: Long, right: Long): Long = left + right
  },
  MULTIPLY("*") {
    override fun calc(left: Long, right: Long) = left * right
  }, ;

  abstract fun calc(left: Long, right: Long): Long

  companion object {
    fun bySign(sign: String): Operation =
      entries.first { it.sign == sign }
  }
}
