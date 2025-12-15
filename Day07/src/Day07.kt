import kotlin.time.measureTime

fun main() {
  Day07.run()
}

object Day07 {

  fun run() {
    val lines = Tools.readInputFile()
    val automata = Automata(lines)

    val timelines: Long
    measureTime {
      timelines = automata.countTimelines()
    }.also { println("Finding the timelines took ${it.inWholeNanoseconds / 1_000_000.0} ms.") }
    val splits = automata.solveTrickles()

    automata.print()

    println("There are $splits splits and $timelines timelines")
  }
}

/**
 * Simulates a simple beam‑tracing automaton on a two‑dimensional grid.
 *
 * The automaton is constructed from a list of strings, each representing a row of the grid.
 * The characters in the strings correspond to the following cell types:
 *
 * * `.` – an empty cell
 * * `S` – the start cell from which the beam originates
 * * `^` – a splitter cell that splits an incoming beam into two beams
 * * `|` – a beam cell that marks a cell already traversed by a beam
 *
 * After construction, the field is fully populated with the appropriate `Cell` values.
 * The start coordinate is stored internally and used as the entry point for beam traversal.
 *
 * Beam traversal proceeds downward from the start cell.  For each visited cell the
 * following rules apply:
 *
 *  * Empty cells become beam cells and the beam continues one step downward.
 *  * Splitter cells increment the split counter, send a left‑moving beam one step
 *    to the left, and a right‑moving beam one step to the right.  Both sub‑beams
 *    continue in the same manner.
 *  * Start and beam cells are considered already visited; the beam simply continues
 *    downward without further action.
 *
 * The traversal stops when the beam moves outside the bounds of the field.  The
 * public function [solveTrickles] initiates this traversal and returns the total number of
 * splits that were performed.  The [print] function may be used to display the
 * final state of the grid, showing empty cells, the start, splitter, and beam
 * cells with their corresponding characters.
 *
 * @param lines a list of strings that describe the initial grid layout
 */
class Automata(lines: List<String>) {

  private val field: Array<Array<Cell>>
  private val width: Int
  private val height: Int = lines.size
  private var start: Coordinate = Coordinate(0, 0)

  init {
    width = lines[0].length
    field = Array(height) { Array(width) { EmptyCell } }

    for ((y, row) in lines.withIndex()) {
      for ((x, column) in row.withIndex()) {
        val cellType = CellType.byChar(column)
        val cell = Cell(cellType)

        field[y][x] = cell

        if (cellType == CellType.START) start = Coordinate(x, y + 1)
      }
    }
  }

  fun solveTrickles() =
    trickle(start)

  /**
   * Simulates the propagation of a beam starting from the given coordinate.
   *
   * The method moves the beam one cell downwards, marks the current cell as a beam,
   * and handles interactions with different cell types:
   *
   * * **EMPTY** – The beam continues moving downwards.
   * * **SPLITTER** – The beam splits into two beams moving left and right; the total number
   *   of split events is accumulated.
   * * **START** or **BEAM** – No further action is taken; the beam simply passes through.
   *
   * If the provided coordinate lies outside the bounds of the field, the method
   * immediately returns `0` without affecting the field.
   *
   * @param coordinate the starting coordinate of the beam
   * @return the total number of split events that occurred while the beam propagated
   */
  private fun trickle(coordinate: Coordinate): Int {
    if (coordinate.x !in 0..<width || coordinate.y !in 0..<height) return 0

    var splits = 0

    when (field[coordinate].type) {
      CellType.EMPTY -> {
        field[coordinate] = BeamCell
        splits += trickle(coordinate.copy(y = coordinate.y + 1))
      }

      CellType.SPLITTER -> {
        splits++
        splits += trickle(coordinate.copy(x = coordinate.x - 1))
        splits += trickle(coordinate.copy(x = coordinate.x + 1))
      }

      CellType.START,
      CellType.BEAM -> Unit
    }

    return splits
  }

  fun countTimelines() =
    timelines(start)

  /**
   * Calculates the total number of timelines that can originate from the given coordinate.
   *
   * The method proceeds downwards until it encounters a non‑empty cell or goes out of bounds.
   * For a cell of type `SPLITTER`, it recursively computes the timelines for the cells to the
   * left and right of the splitter, sums the results, and stores the computed value in
   * the cell’s `value` field to avoid repeated work. If the coordinate lies outside the
   * bounds of the field, the method returns `1`, treating an exit from the grid as a
   * single timeline.
   *
   * @param coordinate the coordinate from which to start counting timelines
   * @return the total number of timelines that can be produced from this coordinate
   */
  private fun timelines(coordinate: Coordinate): Long {
    if (coordinate.x !in 0..<width || coordinate.y !in 0..<height) return 1

    val cell = field[coordinate]
    return when (cell.type) {
      CellType.EMPTY -> timelines(coordinate.copy(y = coordinate.y + 1))

      CellType.SPLITTER -> {
        val result: Long

        if (cell.value == -1L) {
          result = timelines(coordinate.copy(x = coordinate.x - 1)) +
            timelines(coordinate.copy(x = coordinate.x + 1))
          cell.value = result
        } else
          result = cell.value

        result
      }

      else -> 0 // irrelevant for this problem
    }
  }

  private operator fun Array<Array<Cell>>.get(coordinate: Coordinate) =
    this[coordinate.y][coordinate.x]

  private operator fun Array<Array<Cell>>.set(coordinate: Coordinate, cell: Cell) {
    this[coordinate.y][coordinate.x] = cell
  }

  fun print() {
    field
      .map { row ->
        row
          .map { it.type.char }
          .joinToString("")
      }
      .forEach { println(it) }
  }
}

data class Coordinate(val x: Int, val y: Int)

/**
 * A set of distinct cell types used in the beam propagation grid.
 *
 * Each cell type corresponds to a single character that can appear in the input
 * representation of the grid. The grid is interpreted by reading each character
 * and mapping it to the appropriate cell type.
 *
 * The enum constants are:
 * - **EMPTY** – represents a free space where a beam may travel.
 * - **START** – the origin point from which a beam is emitted.
 * - **SPLITTER** – a special cell that splits an incoming beam into two new beams.
 * - **BEAM** – a cell that is used internally to represent a beam during
 *   processing of the grid.
 *
 * The companion object contains a helper function that converts a character
 * into its corresponding [CellType] value.
 */
enum class CellType(val char: Char) {
  EMPTY('.'),
  START('S'),
  SPLITTER('^'),
  BEAM('|');

  companion object {

    /**
     * Returns the [CellType] that corresponds to the specified character.
     *
     * The method searches the enum's entries for a constant whose `char` value matches
     * the provided character and returns that constant. If no matching cell type
     * is found, a `NoSuchElementException` is thrown.
     *
     * @param c the character representing a cell type
     * @return the matching [CellType] instance
     * @throws NoSuchElementException if the character does not correspond to any [CellType]
     */
    fun byChar(c: Char) =
      CellType.entries.first { it.char == c }
  }
}

/**
 * Represents a single cell within the beam propagation grid.
 *
 * The cell contains a [CellType] that defines its role in the simulation and an optional
 * numeric value. The default value of `-1` signifies that the cell has not yet been
 * assigned a specific value during processing.
 *
 * The type information is derived from the input representation where each cell is
 * encoded by a single character. The value field can be updated dynamically as the
 * algorithm traverses the grid, for example, to record the number of beams that have
 * passed through the cell or other state information.
 */
data class Cell(val type: CellType, var value: Long = -1)

val EmptyCell = Cell(CellType.EMPTY)
val BeamCell = Cell(CellType.BEAM)
