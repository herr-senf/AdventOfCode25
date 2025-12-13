fun main() {
  Day07.run()
}

object Day07 {

  fun run() {
    val lines = Tools.readInputFile()
    val automata = Automata(lines)

    val splits = automata.run()

    automata.print()

    println("There are $splits splits")
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
 * public function [run] initiates this traversal and returns the total number of
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
    field = Array(height) { Array(width) { Cell.EMPTY } }

    for ((y, row) in lines.withIndex()) {
      for ((x, column) in row.withIndex()) {
        val cell = Cell.byChar(column)

        field[y][x] = cell

        if (cell == Cell.START) start = Coordinate(x, y + 1)
      }
    }
  }

  fun run() =
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
  fun trickle(coordinate: Coordinate): Int {
    if (coordinate.x !in 0..<width || coordinate.y !in 0..<height) return 0

    var splits = 0

    when (field[coordinate]) {
      Cell.EMPTY -> {
        field[coordinate] = Cell.BEAM
        return trickle(coordinate.copy(y = coordinate.y + 1))
      }

      Cell.SPLITTER -> {
        splits++
        splits += trickle(coordinate.copy(x = coordinate.x - 1))
        splits += trickle(coordinate.copy(x = coordinate.x + 1))
      }

      Cell.START,
      Cell.BEAM -> {
        // do nothing
      }
    }

    return splits
  }

  private operator fun Array<Array<Cell>>.get(coordinate: Coordinate) =
    this[coordinate.y][coordinate.x]

  private operator fun Array<Array<Cell>>.set(coordinate: Coordinate, cell: Cell) {
    this[coordinate.y][coordinate.x] = cell
  }

  fun print() {
    field.map { row -> row.map { it.char }.joinToString("") }
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
 * into its corresponding [Cell] value.
 */
enum class Cell(val char: Char) {
  EMPTY('.'),
  START('S'),
  SPLITTER('^'),
  BEAM('|');

  companion object {
    fun byChar(c: Char): Cell =
      Cell.entries.first { it.char == c }
  }
}
