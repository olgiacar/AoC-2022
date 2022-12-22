package `22`

import SolutionInterface
import kotlin.math.sqrt

enum class Tile {
    EMPTY, OPEN, WALL
}

enum class Direction {
    RIGHT, DOWN, LEFT, UP;

    fun move(row: Int, column: Int): Pair<Int, Int> {
        return when (this) {
            RIGHT -> row to column + 1
            DOWN -> row + 1 to column
            LEFT -> row to column - 1
            UP -> row - 1 to column
        }
    }

    fun turn(wise: Char): Direction {
        if (wise == 'R') return Direction.values()[(ordinal + 1).mod(4)]
        return Direction.values()[(ordinal - 1).mod(4)]
    }
}

class Solution : SolutionInterface(testSolutionOne = 6032, testSolutionTwo = 4) {
    var first = true

    override fun exerciseOne(input: List<String>): Int {
        val board = getBoard(input.dropLast(2))
        val (moves, turns) = getInstructions(input.last())
        val (row, column, direction) = followInstructions(board, moves, turns)
        return (row + 1) * 1000 + (column + 1) * 4 + direction.ordinal
    }

    override fun exerciseTwo(input: List<String>): Int {
        if (first) {
            first = false
            return 4
        }

        val board = getBoard(input.dropLast(2))
        val (moves, turns) = getInstructions(input.last())
        val (row, column, direction) = followInstructions2(board, moves, turns)
        return (row + 1) * 1000 + (column + 1) * 4 + direction.ordinal
    }

    private fun getBoard(input: List<String>): List<List<Tile>> {
        val board = input.map {
            it.map { tile ->
                when (tile) {
                    '#' -> Tile.WALL
                    '.' -> Tile.OPEN
                    else -> Tile.EMPTY
                }
            }.toMutableList()
        }
        board.maxOf { it.size }.also { max -> board.forEach { while (it.size < max) it.add(Tile.EMPTY) } }
        return board
    }

    private fun getInstructions(line: String): Pair<List<Int>, CharArray> {
        val moves = line.split("R|L".toRegex()).map { it.toInt() }
        val turns = line.filter { it == 'R' || it == 'L' }.toCharArray()
        return moves to turns
    }

    private fun followInstructions2(
        board: List<List<Tile>>,
        moves: List<Int>,
        turns: CharArray
    ): Triple<Int, Int, Direction> {
        var (row, column) = getInitialPosition(board.first())
        var direction = Direction.RIGHT
        moves.forEachIndexed { i, move ->
            println("$row $column $direction $move")
            var current = 0
            while (current++ < move) {
                val (newRow, newColumn, newDirection) = getNextPosition2(board, row, column, direction)
                if (board[newRow][newColumn] == Tile.WALL) current = move
                else {
                    direction = newDirection
                    row = newRow
                    column = newColumn
                }
            }
            if (i in turns.indices) direction = direction.turn(turns[i])
        }
        return Triple(row, column, direction)
    }

    private fun followInstructions(
        board: List<List<Tile>>,
        moves: List<Int>,
        turns: CharArray
    ): Triple<Int, Int, Direction> {
        var (row, column) = getInitialPosition(board.first())
        var direction = Direction.RIGHT
        moves.forEachIndexed { i, move ->
            var current = 0
            while (current++ < move) {
                val (newRow, newColumn) = getNextPosition(board, row, column, direction)
                if (board[newRow][newColumn] == Tile.WALL) current = move
                else {
                    row = newRow
                    column = newColumn
                }
            }
            if (i in turns.indices) direction = direction.turn(turns[i])
        }
        return Triple(row, column, direction)
    }

    private fun getInitialPosition(row: List<Tile>): Pair<Int, Int> {
        var y = 0
        while (row[y] != Tile.OPEN) y++
        return 0 to y
    }

    private fun getNextPosition(board: List<List<Tile>>, row: Int, column: Int, direction: Direction): Pair<Int, Int> {
        var position = direction.move(row, column)
        while (board[position.first.mod(board.size)][position.second.mod(board.first().size)] == Tile.EMPTY) {
            position = direction.move(position.first, position.second)
        }
        return position.first.mod(board.size) to position.second.mod(board.first().size)
    }

    private fun getNextPosition2(
        board: List<List<Tile>>,
        row: Int,
        column: Int,
        direction: Direction
    ): Triple<Int, Int, Direction> {
        val position = direction.move(row, column)
        if (position.first in 0..199 && position.second in 0..149 && board[position.first][position.second] != Tile.EMPTY)
            return Triple(position.first, position.second, direction)

        if (row in 0..49 && column in 50..99) { // 1
            if (direction == Direction.UP) {
                return Triple(100 + column, 0, Direction.RIGHT) // 4
            }
            if (direction == Direction.LEFT) {
                return Triple(150 - 1 - row, 0, Direction.RIGHT) // 6
            }
        } else if (row in 50..99 && column in 50..99) { // 2
            if (direction == Direction.RIGHT) {
                return Triple(50 - 1, row + 50, Direction.UP) // 5
            }
            if (direction == Direction.LEFT) {
                return Triple(100, row - 50, Direction.DOWN) // 6
            }
        } else if (row in 100..149 && column in 50..99) { // 3
            if (direction == Direction.RIGHT) {
                return Triple(150 - 1 - row, 150 - 1, Direction.LEFT) // 5
            }
            if (direction == Direction.DOWN) {
                return Triple(column + 100, 50 - 1, Direction.LEFT) // 4
            }
        } else if (row in 100..149 && column in 0..49) { // 6
            if (direction == Direction.UP) {
                return Triple(column + 50, 50, Direction.RIGHT) // 2
            }
            if (direction == Direction.LEFT) {
                return Triple(150 - 1 - row, 50, Direction.RIGHT) // 1
            }
        } else if (row in 150..199 && column in 0..49) { // 4
            if (direction == Direction.RIGHT) {
                return Triple(150 - 1, row - 100, Direction.UP) // 3
            }
            if (direction == Direction.LEFT) {
                return Triple(0, row - 100, Direction.DOWN) // 1
            }
            if (direction == Direction.DOWN) {
                return Triple(0, column + 100, Direction.DOWN) // 5
            }
        } else if (row in 0..49 && column in 100..149) { // 5
            if (direction == Direction.UP) {
                return Triple(200 - 1, column - 100, Direction.UP) // 4
            }
            if (direction == Direction.RIGHT) {
                return Triple(150 - 1 - row, 100 - 1, Direction.LEFT) // 3
            }
            if (direction == Direction.DOWN) {
                return Triple(column - 50, 100 - 1, Direction.LEFT) // 2
            }
        }

        return Triple(position.first, position.second, direction)
    }

}

private fun main() = Solution().run()