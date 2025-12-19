import java.io.File
import kotlin.math.sqrt

fun main() {
  Day08.run()
}

object Day08 {

  fun run() {
    val lines = readInputFile()//"example.txt")
    val nodes = convertLinesToCoordinates(lines)
    val edges = createSortedEdges(nodes)
    val result = mergeCircuits(edges, 1000)

    val product = result.map { it.size }.reduce { acc, i -> acc * i }

    println("The product by length of the 3 largest circuits is $product")
    println("The circuits are: ${result.map { it.size }}")
  }

  private fun mergeCircuits(edges: List<Edge>, count: Int = 10): List<Circuit> {
    val stack = ArrayDeque(edges)
    val mergedCircuits = mutableListOf<Circuit>()

    stack
      .take(count)
      .forEach { edge ->
        val circuits = mergedCircuits.filter { it.contains(edge) }

        if (circuits.isEmpty())
          mergedCircuits.add(Circuit(edge.first, edge.second))
        else if (circuits.size == 1)
          circuits.first().add(edge)
        else {
          circuits.flatMap { it.nodes }
            .let { mergedCircuits.add(Circuit(it)) }

          circuits.forEach { mergedCircuits.remove(it) }
        }
      }

    return mergedCircuits
      .sortedByDescending { it.size }
      .take(3)
  }

  private fun createSortedEdges(nodes: List<Node>): List<Edge> =
    nodes
      .flatMapIndexed { index, first ->
        nodes
          .drop(index + 1)
          .map { second -> Edge(first, second) }
      }
      .sortedBy { it.length }

  private fun convertLinesToCoordinates(lines: List<String>): List<Node> =
    lines
      .map { line ->
        line
          .split(",")
          .map { it.toInt() }
      }
      .map { Node(it[0], it[1], it[2]) }

  private fun readInputFile(filename: String = "input.txt"): List<String> =
    File("Day08/src/$filename").readLines()
}

/**
 * Represents a point in three‑dimensional space with integer coordinates.
 *
 * The class holds three integer fields: `x`, `y`, and `z`. It provides a
 * [dist] function that calculates the Euclidean distance between this
 * coordinate and another [Coordinate] instance. The result is returned as a
 * [Double].
 */
data class Node(val x: Int, val y: Int, val z: Int) {

  /**
   * Calculates the Euclidean distance from this coordinate to the specified [coordinate].
   *
   * @param coordinate the target coordinate to which the distance is calculated
   * @return the Euclidean distance as a [Double]
   */
  fun dist(coordinate: Node): Double =
    sqrt((this.x - coordinate.x).sqr() + (this.y - coordinate.y).sqr() + (this.z - coordinate.z).sqr())

  /**
   * Calculates the square of an integer.
   *
   * This extension function multiplies the integer by itself and returns the
   * result as a [Double].
   *
   * @receiver the integer to be squared
   * @return the square of the receiver as a [Double]
   */
  private fun Int.sqr() =
    (this * this).toDouble()
}

data class Edge(val first: Node, val second: Node) {
  val length: Double by lazy { first.dist(second) }
}

class Circuit(vararg node: Node) {

  constructor(node: Collection<Node>) : this(*node.toTypedArray())

  private val _nodes = mutableSetOf(*node)
  val nodes: Set<Node>
    get() = _nodes

  val size: Int
    get() = _nodes.size

  fun contains(edge: Edge) =
    _nodes.contains(edge.first) || _nodes.contains(edge.second)

  fun add(edge: Edge) {
    _nodes.add(edge.first)
    _nodes.add(edge.second)
  }
}
