import java.io.File
import kotlin.math.sqrt

fun main() {
  Day08.run()
}

object Day08 {

  /**
   * Executes the main processing logic to calculate and print the product of the lengths
   * of the three largest circuits formed from the given data.
   *
   * This method reads input data from a file, processes it into a set of coordinates,
   * constructs edges between these coordinates, and merges the edges into circuits.
   * The method then calculates the product of the sizes of the three largest circuits
   * and prints the result along with their individual sizes.
   *
   * The process involves:
   * - Reading input data from a file named "input.txt".
   * - Converting the input lines into coordinate-based nodes.
   * - Creating and sorting edges between these nodes.
   * - Merging the edges into circuits, while limiting the number of processed edges to 1000.
   * - Identifying the three largest circuits and calculating the product of their sizes.
   *
   * Output:
   * - The product of the sizes of the three largest circuits.
   * - The sizes of these circuits as a list.
   */
  fun run() {
    val lines = readInputFile()
    val nodes = convertLinesToCoordinates(lines)
    val edges = createSortedEdges(nodes)
    val result = mergeCircuits(edges, 1000)

    val product = result.map { it.size }.fold(1) { acc, size -> acc * size }

    println("The product by length of the 3 largest circuits is $product")
    println("The circuits are: ${result.map { it.size }}")
  }

  /**
   * Merges a set of circuits from the given edges, limiting the number of processed edges,
   * and returns a list of the largest circuits.
   *
   * The function iterates through the provided edges, determines their membership in existing circuits,
   * and either adds new circuits, merges existing ones, or handles cases where the edge connects to
   * multiple circuits. The final list is sorted by circuit size, and only the largest circuits are returned.
   *
   * @param edges The list of edges from which circuits are formed or merged.
   * @param count The maximum number of edges to process. Defaults to 10 if not specified.
   * @return A sorted list of the largest circuits with at most three circuits included.
   */
  private fun mergeCircuits(edges: List<Edge>, count: Int = 10): List<Circuit> {
    val mergedCircuits = mutableListOf<Circuit>()

    edges
      .take(count)
      .forEach { edge ->
        val circuits = mergedCircuits.filter { it.contains(edge) }

        // Adds or merges circuits based on edge membership
        when {
          circuits.isEmpty() -> mergedCircuits.add(Circuit(edge.first, edge.second))

          circuits.size == 1 -> circuits[0].add(edge)

          circuits.size == 2 -> {
            val newCircuit = Circuit(circuits[0].nodes + circuits[1].nodes)
            mergedCircuits.add(newCircuit)

            circuits.forEach { mergedCircuits.remove(it) }
          }

          else -> error("More than 2 circuits contain the edge $edge")
        }
      }

    return mergedCircuits
      .sortedByDescending { it.size }
      .take(3)
  }

  /**
   * Creates a sorted list of edges connecting all pairs of nodes, ordered by their lengths.
   *
   * This method generates all possible edges between the nodes provided in the input list.
   * Each edge connects a pair of nodes and is represented by an [Edge] object.
   * The resulting list of edges is sorted in ascending order based on their lengths.
   *
   * @param nodes the list of [Node]s for which edges are to be created
   * @return a sorted list of [Edge]s connecting all pairs of nodes
   */
  private fun createSortedEdges(nodes: List<Node>): List<Edge> =
    nodes
      .flatMapIndexed { index, first ->
        nodes
          .drop(index + 1)
          .map { second -> Edge(first, second) }
      }
      .sortedBy { it.length }

  /**
   * Converts a list of strings representing comma-separated coordinates into a list of [Node] objects.
   *
   * Each string in the input list is expected to be a representation of coordinates in the format "x,y,z",
   * where x, y, and z are integers.
   *
   * @param lines the list of strings where each string represents a point in the format "x,y,z"
   * @return a list of [Node] instances created from the parsed coordinate strings
   */
  private fun convertLinesToCoordinates(lines: List<String>): List<Node> =
    lines
      .map { line ->
        line
          .split(",")
          .map { it.toLong() }
      }
      .map { Node(it[0], it[1], it[2]) }

  /**
   * Reads the content of the specified input file and returns it as a list of strings, where each string
   * corresponds to a line in the file.
   *
   * @param filename The name of the file to read. Defaults to "input.txt" if no parameter is provided.
   * @return A list of strings containing the lines of the file.
   */
  private fun readInputFile(filename: String = "input.txt"): List<String> =
    File("Day08/src/$filename").readLines()
}

/**
 * Represents a point in a three-dimensional space with coordinates [x], [y], and [z].
 *
 * This class provides methods for spatial calculations, such as computing
 * the Euclidean distance between two points.
 *
 * @property x the x-coordinate of the point
 * @property y the y-coordinate of the point
 * @property z the z-coordinate of the point
 */
data class Node(val x: Long, val y: Long, val z: Long) {

  /**
   * Calculates the Euclidean distance from this coordinate to the specified [coordinate].
   *
   * @param coordinate the target coordinate to which the distance is calculated
   * @return the Euclidean distance as a [Double]
   */
  fun dist(coordinate: Node): Double =
    sqrt((this.x - coordinate.x).sqr() + (this.y - coordinate.y).sqr() + (this.z - coordinate.z).sqr())

  /**
   * Computes the square of the [Long] value and returns the result as a [Double].
   *
   * This function is a private extension for the [Long] type and is used in operations
   * where calculations involving squares of [Long] values are required.
   *
   * @receiver the [Long] value to be squared
   * @return the square of the receiver as a [Double]
   */
  private fun Long.sqr() =
    (this * this).toDouble()
}

/**
 * Represents an edge connecting two nodes in three-dimensional space.
 *
 * An edge is defined by its two endpoints, [first] and [second], which are instances of [Node].
 * It also calculates the Euclidean distance between these two nodes, accessible via the [length] property.
 *
 * @property first the starting node of the edge
 * @property second the ending node of the edge
 * @property length the Euclidean distance between [first] and [second], lazily calculated
 */
data class Edge(val first: Node, val second: Node) {
  val length: Double by lazy { first.dist(second) }
}

/**
 * Represents a circuit consisting of a set of interconnected nodes in three-dimensional space.
 *
 * This class manages a mutable set of unique nodes and provides functionality for adding edges
 * and checking if an edge is present within the circuit. Each edge connects two nodes and, when added,
 * automatically includes its endpoints in the circuit's node set.
 *
 * @constructor
 * Creates a [Circuit] with an initial set of nodes provided as either a variable argument list or a collection.
 *
 * @param node the initial nodes to include in the circuit; can be passed as vararg or as a collection
 *
 * @property nodes the immutable set of nodes currently contained within the circuit
 * @property size the number of unique nodes in the circuit
 */
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
