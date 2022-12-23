package `23`

import SolutionInterface

enum class Direction(private val x: Int, private val y: Int) {
    NORTH(0, -1),
    NORTH_WEST(-1, -1),
    WEST(-1, 0),
    SOUTH_WEST(-1, 1),
    SOUTH(0, 1),
    SOUTH_EAST(1, 1),
    EAST(1, 0),
    NORTH_EAST(1, -1);

    fun add(position: Pair<Int, Int>): Pair<Int, Int> {
        return position.first + x to position.second + y
    }
}

val directionMap = mapOf(
    Direction.NORTH to listOf(Direction.NORTH, Direction.NORTH_WEST, Direction.NORTH_EAST),
    Direction.SOUTH to listOf(Direction.SOUTH, Direction.SOUTH_WEST, Direction.SOUTH_EAST),
    Direction.EAST to listOf(Direction.EAST, Direction.SOUTH_EAST, Direction.NORTH_EAST),
    Direction.WEST to listOf(Direction.WEST, Direction.NORTH_WEST, Direction.SOUTH_WEST),
)

class Elf(var position: Pair<Int, Int>) {

    fun proposePosition(elves: List<Elf>, preferredDirections: List<Direction>): Pair<Int, Int> {
        if (emptyAround(elves)) return position
        for (direction in preferredDirections) {
            if (directionIsFree(elves, direction)) return direction.add(position)
        }
        return position
    }

    private fun emptyAround(elves: List<Elf>) = isFree(elves, Direction.values().asList())

    private fun directionIsFree(elves: List<Elf>, direction: Direction) = isFree(elves, directionMap[direction]!!)

    private fun isFree(elves: List<Elf>, directions: List<Direction>): Boolean {
        directions.forEach { direction ->
            if (elves.any { direction.add(position) == it.position }) return false
        }
        return true
    }
}


class Solution : SolutionInterface(testSolutionOne = 110, testSolutionTwo = 20) {
    private val preferredDirections = mutableListOf(Direction.NORTH, Direction.SOUTH, Direction.WEST, Direction.EAST)
    override fun exerciseOne(input: List<String>): Int {
        val elves = getElves(input)
        doMoves(elves)
        val minX = elves.minOf { it.position.first }
        val maxX = elves.maxOf { it.position.first }
        val minY = elves.minOf { it.position.second }
        val maxY = elves.maxOf { it.position.second }
        return (maxX - minX + 1) * (maxY - minY + 1) - elves.size
    }

    override fun exerciseTwo(input: List<String>): Int {
        val elves = getElves(input)
        return moveUntilStatic(elves)
    }

    private fun moveUntilStatic(elves: List<Elf>): Int {
        var count = 1
        while (doMove(elves)) count++
        return count
    }

    private fun doMoves(elves: List<Elf>) = repeat(10) { doMove(elves) }

    private fun doMove(elves: List<Elf>): Boolean {
        val propositions = elves.associateWith { it.proposePosition(elves, preferredDirections) }
        if (propositions.toList().none { it.first.position != it.second })
            return false

        val duplicated = propositions.map { it.value }
            .groupingBy { it }.eachCount().filter { it.value > 1 }.map { it.key }
        for (elf in elves) {
            val proposition = propositions[elf]!!
            if (proposition !in duplicated) {
                elf.position = proposition
            }
        }

        preferredDirections.add(preferredDirections.removeFirst())
        return true
    }

    private fun getElves(input: List<String>): List<Elf> {
        val elves = mutableListOf<Elf>()
        for (y in input.indices) {
            for (x in input.indices) {
                if (input[y][x] == '#') {
                    elves.add(Elf(x to y))
                }
            }
        }
        return elves
    }

}

private fun main() = Solution().run()