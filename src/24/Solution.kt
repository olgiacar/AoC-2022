package `24`

import SolutionInterface
import add

enum class Tile {
    WALL, OPEN, WIND
}

class Solution : SolutionInterface(testSolutionOne = 18, testSolutionTwo = 54) {
    val directions = listOf(0 to 0, 0 to 1, 0 to -1, 1 to 0, -1 to 0)

    override fun exerciseOne(input: List<String>): Int {
        val source = getSource(input)
        val target = getTarget(input)
        return getShortestPathLength(source, target, input, 0)
    }

    override fun exerciseTwo(input: List<String>): Int {
        val source = getSource(input)
        val target = getTarget(input)
        val first = getShortestPathLength(source, target, input, 0)
        val second = getShortestPathLength(target, source, input, first)
        return getShortestPathLength(source, target, input, second)
    }

    private fun getShortestPathLength(
        source: Pair<Int, Int>,
        target: Pair<Int, Int>,
        input: List<String>,
        initialStep: Int
    ): Int {
        var step = initialStep
        val lcm = (input.size - 2) * (input.first().length - 2)
        val xRange = input.indices
        val yRange = input.first().indices
        var positions = mutableSetOf(source)

        while (target !in positions) {
            val newPositions = mutableSetOf<Pair<Int, Int>>()
            val state = getState(input, (1 + step++) % lcm)

            for (position in positions) {
                for (direction in directions) {
                    val next = position add direction
                    if (next.first in xRange && next.second in yRange && state[next.first][next.second] == Tile.OPEN)
                        newPositions.add(next)
                }
            }

            positions = newPositions
        }

        return step
    }

    private fun getState(input: List<String>, step: Int): Array<Array<Tile>> {
        val state = Array(input.size) { i -> Array(input[i].length) { Tile.OPEN } }
        val height = input.size
        val width = input.first().length

        input.forEachIndexed { x, row ->
            row.forEachIndexed { y, it ->
                when (it) {
                    '#' -> state[x][y] = Tile.WALL
                    '>' -> state[x][(y + step - 1).mod(width - 2) + 1] = Tile.WIND
                    'v' -> state[(x + step - 1).mod(height - 2) + 1][y] = Tile.WIND
                    '<' -> state[x][(y - step - 1).mod(width - 2) + 1] = Tile.WIND
                    '^' -> state[(x - step - 1).mod(height - 2) + 1][y] = Tile.WIND
                }
            }
        }

        return state
    }

    private fun getSource(input: List<String>) = 0 to input.first().indexOf('.')

    private fun getTarget(input: List<String>) = input.size - 1 to input.last().indexOf('.')

}

private fun main() = Solution().run()