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

  private fun readInputFile(): List<String> =
    File("Day08/src/input.txt")
      .readLines()

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

  private fun generateEdges(nodes: List<Node>) =
    nodes
      .flatMapIndexed { i, a ->
        nodes
          .drop(i + 1)
          .map { b -> Edge(a, b, a.coordinate.dist(b.coordinate)) }
      }
      .sortedBy { it.distance }

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

data class Coordinate(val x: Int, val y: Int, val z: Int) {
  fun dist(coordinate: Coordinate): Double =
    sqrt((this.x - coordinate.x).sqr() + (this.y - coordinate.y).sqr() + (this.z - coordinate.z).sqr())

  private fun Int.sqr() =
    (this * this).toDouble()
}

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

  fun add(node: Node) {
    if (node.circuit === this) return

    if (node.circuit != null)
      node.circuit?.let { it -= node }

    this._nodes.add(node)
    node.circuit = this
  }

  fun remove(node: Node) {
    node.circuit = null
    this._nodes.remove(node)
  }
}

data class Node(val coordinate: Coordinate) {
  var circuit: Circuit? = null
}

data class Edge(val start: Node, val end: Node, val distance: Double)
