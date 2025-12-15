import java.io.File
import kotlin.math.sqrt

val example = listOf(
  "162,817,812",
  "57,618,57",
  "906,360,560",
  "592,479,940",
  "352,342,300",
  "466,668,158",
  "542,29,236",
  "431,825,988",
  "739,650,466",
  "52,470,668",
  "216,146,977",
  "819,987,18",
  "117,168,530",
  "805,96,715",
  "346,949,466",
  "970,615,88",
  "941,993,340",
  "862,61,35",
  "984,92,344",
  "425,690,689",
)

fun main() {
  Day08.run()
}

object Day08 {

  fun run() {
    val inputs = readInputFile()
//    val inputs = example
    val nodes = generateNodes(inputs)
    val edges = generateEdges(nodes)
    val circuits = generateCircuits(edges, 1000)

    val result = circuits
      .map { it.size }
      .sortedByDescending { it }
      .take(3)
      .reduce { s1, s2 -> s1 * s2 }

    println("The product of the largest 3 circuits is $result")
  }

  /**
   * Reads the input file located at "Day08/src/input.txt" and returns its contents as a list of strings,
   * where each element represents one line of the file.
   *
   * @return A list of strings containing each line from the input file.
   */
  private fun readInputFile(): List<String> =
    File("Day08/src/input.txt")
      .readLines()

  /**
   * Creates a list of [Node] objects from a list of strings, where each string
   * contains three comma‑separated integer values representing the X, Y, and
   * Z coordinates of a point in 3‑D space.
   *
   * The method processes each line as follows:
   * 1. Splits the string by commas.
   * 2. Converts each substring to an integer.
   * 3. Builds a [Coordinate] from the three integers.
   * 4. Wraps the coordinate in a [Node].
   *
   * The resulting nodes are returned as a new list.
   *
   * @param input a list of strings, each string formatted as `"x,y,z"` where
   *   `x`, `y`, and `z` are integer values.
   * @return a list of [Node] instances corresponding to the input coordinates.
   */
  private fun generateNodes(input: List<String>) =
    input
      .map { line ->
        line
          .split(",")
          .map { it.toInt() }
      }
      .map { Coordinate(it[0], it[1], it[2]) }
      .map { Node(it) }
      .toList()

  /**
   * Generates all possible edges between the supplied nodes and sorts them by distance.
   *
   * For each unique pair of nodes an [Edge] is created with the distance computed
   * from their coordinates. The resulting list is sorted in ascending order of the
   * edge distances, making it suitable for algorithms that require a priority
   * ordering such as minimum spanning tree construction.
   *
   * @param nodes a list of [Node] instances that will be connected pairwise
   * @return a list of [Edge] objects, sorted by the `distance` property
   */
  private fun generateEdges(nodes: List<Node>) =
    nodes
      .flatMapIndexed { i, a ->
        nodes
          .drop(i + 1)
          .map { b -> Edge(a, b, a.coordinate.dist(b.coordinate)) }
      }
      .sortedBy { it.distance }

  /**
   * Builds a list of circuits from a sorted list of edges.
   *
   * The method processes edges from the given `sortedEdges` list, up to the
   * specified `processedCount`.  Each edge is examined in order, and the
   * connecting nodes are used to create or merge circuits as follows:
   *
   * * If neither endpoint is part of an existing circuit, a new circuit
   *   is created containing the two nodes.
   * * If both endpoints belong to different circuits, the two circuits
   *   are merged into a single circuit that contains all nodes from both.
   * * If only one endpoint is part of a circuit, the other node is added
   *   to that circuit.
   *
   * The algorithm stops when either the stack of edges is exhausted or
   * the number of processed edges reaches `processedCount`.
   *
   * @param sortedEdges The list of edges to process, sorted by their
   *   distance or any other criterion that dictates the order of
   *   processing.
   * @param processedCount The maximum number of edges to process from
   *   the list.
   * @return A list of [Circuit] objects that represent the
   *   interconnected nodes discovered during processing.
   */
  private fun generateCircuits(sortedEdges: List<Edge>, processedCount: Int): List<Circuit> {
    val stack = ArrayDeque(sortedEdges)
    val circuits = mutableListOf<Circuit>()

    var edgesProcessed = 0
    while (stack.isNotEmpty() && edgesProcessed < processedCount) {
      val shortest = stack.removeFirst()

      val start = shortest.start
      val circuitStart = start.circuit
      val end = shortest.end
      val circuitEnd = end.circuit

      when {
        circuitStart == null && circuitEnd == null -> {
          val circuit = Circuit(start, end)
          circuits.add(circuit)
        }

        circuitStart != null && circuitEnd != null -> {
          // both junctions are part of a circuit; if it's the same, we can ignore them
          if (circuitStart != circuitEnd) {
            // start and end junction are already part of different circuits; let's merge those two circuits
            circuits.remove(circuitStart)
            circuits.remove(circuitEnd)
            val circuit = Circuit(*circuitStart.nodes, *circuitEnd.nodes)
            circuits.add(circuit)
          }
        }

        circuitStart != null -> circuitStart += end
        circuitEnd != null -> circuitEnd += start

        else -> error("Unexpected things happened: $shortest")
      }

      edgesProcessed++
    }

    return circuits
  }
}

/**
 * Represents a point in three‑dimensional space with integer coordinates.
 *
 * The class holds three integer fields: `x`, `y`, and `z`. It provides a
 * [dist] function that calculates the Euclidean distance between this
 * coordinate and another [Coordinate] instance. The result is returned as a
 * [Double].
 */
data class Coordinate(val x: Int, val y: Int, val z: Int) {

  /**
   * Calculates the Euclidean distance from this coordinate to the specified [coordinate].
   *
   * @param coordinate the target coordinate to which the distance is calculated
   * @return the Euclidean distance as a [Double]
   */
  fun dist(coordinate: Coordinate): Double =
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

/**
 * Represents a collection of interconnected [Node] objects that form a circuit.
 *
 * A circuit manages the membership of its nodes, ensuring that each node belongs to at most one circuit.
 * When a node is added to a circuit, it is automatically removed from any previous circuit,
 * and its [Circuit] reference is updated accordingly. Removing a node clears its
 * reference, allowing it to be reused elsewhere.
 *
 * @property nodes An array containing all nodes currently part of the circuit.
 * @property size The number of nodes contained in the circuit.
 *
 * @constructor Creates a circuit containing the given nodes. Each node is added
 * sequentially, applying the same rules as [add].
 */
class Circuit(vararg nodes: Node) {

  private val _nodes: MutableSet<Node> = mutableSetOf()
  val nodes: Array<Node>
    get() = _nodes.toTypedArray()

  val size: Int
    get() = _nodes.size

  init {
    nodes.forEach(::add)
  }

  operator fun plusAssign(node: Node) {
    add(node)
  }

  operator fun minusAssign(node: Node) {
    remove(node)
  }

  /**
   * Adds the specified [node] to this circuit.
   *
   * If the node is already part of this circuit, the call has no effect.
   * If the node belongs to another circuit, it is first removed from that circuit
   * before being added to this one. After the operation, the node's
   * [Node.circuit] reference points to this circuit.
   *
   * @param node the node to add to the circuit
   */
  fun add(node: Node) {
    if (node.circuit === this) return

    if (node.circuit != null)
      node.circuit?.let { it -= node }

    this._nodes.add(node)
    node.circuit = this
  }

  /**
   * Removes the specified [node] from this circuit.
   *
   * The node’s [Node.circuit] reference is cleared, and the node is removed from the internal collection.
   *
   * @param node the node to remove from the circuit
   */
  fun remove(node: Node) {
    node.circuit = null
    this._nodes.remove(node)
  }
}

/**
 * Represents a point that can participate in a [Circuit].
 *
 * A node is identified by a three‑dimensional coordinate.
 * It may belong to a single circuit at a time. Adding the node to a circuit
 * automatically updates its `circuit` reference, and if the node was already
 * part of another circuit, it is removed from that previous circuit before the
 * new assignment.
 *
 * Removing a node from a circuit clears its `circuit` reference.
 */
data class Node(val coordinate: Coordinate) {
  var circuit: Circuit? = null
}

/**
 * Represents a directed connection between two [Node] instances.
 *
 * Each `Edge` holds a reference to a starting node, an ending node, and the
 * distance between them. The class is a data class, so it automatically
 * provides value-based equality and a convenient `copy` method.
 *
 * This type is typically used to model the relationships between nodes in
 * graph‑based structures such as circuits or maps, where the distance
 * indicates the weight or length of the link.
 */
data class Edge(val start: Node, val end: Node, val distance: Double)
