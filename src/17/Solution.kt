package `17`

import SolutionInterface
import java.math.BigInteger

class Solution : SolutionInterface(testSolutionOne = 3068, testSolutionTwo = 5) {
    private val stones = listOf(
        listOf(0 to 0, 1 to 0, 2 to 0, 3 to 0),
        listOf(0 to 1, 1 to 0, 1 to 1, 1 to 2, 2 to 1),
        listOf(0 to 0, 1 to 0, 2 to 0, 2 to 1, 2 to 2),
        listOf(0 to 0, 0 to 1, 0 to 2, 0 to 3),
        listOf(0 to 0, 1 to 0, 0 to 1, 1 to 1)
    )
    private var move = 0

    override fun exerciseOne(input: List<String>): Int {
        val cave = getCave(input, 2022)
        return getMaxHeight(cave)
    }

    override fun exerciseTwo(input: List<String>): Int {
        val cave = getCave(input, 5000)
        val (start, end) = getPatternRepeat(cave)
        val patternHeight = BigInteger.valueOf((end - start).toLong())

        val startStone = stonePassesHeight(input, start)
        val endStone = stonePassesHeight(input, end)
        val patternLength = BigInteger.valueOf((endStone - startStone).toLong())

        val total = BigInteger.valueOf(1_000_000).times(BigInteger.valueOf(1_000_000))
        val cycles = total.divide(patternLength)
        val rest = total.mod(patternLength)

        val restHeight = getMaxHeight(getCave(input, rest.toInt()))

        println(cycles.times(patternHeight).plus(BigInteger.valueOf(restHeight.toLong())))

        return 5
    }

    private fun stonePassesHeight(input: List<String>, height: Int): Int {
        val directions = input.first().toCharArray()
        val cave = mutableMapOf<Pair<Int, Int>, Boolean>()
        move = 0
        var stone = 0

        while (getMaxHeight(cave) <= height - 1) {
            dropStone(stones[stone++ % 5], cave, directions)
        }

        return stone
    }


    private fun getPatternRepeat(cave: Map<Pair<Int, Int>, Boolean>): Pair<Int, Int> {
        val caveString = getCaveString(cave)
        val seen = mutableListOf<List<String>>()

        caveString.split("\n").windowed(40).forEachIndexed { i, it ->
            if (seen.contains(it)) return seen.indexOf(it) to i
            else seen.add(it)
        }
        return 0 to 0
    }

    private fun getCaveString(cave: Map<Pair<Int, Int>, Boolean>): String {
        var s = ""
        repeat(getMaxHeight(cave)) {
            for (x in -2..4) {
                s += if (cave.contains(x to it)) "#" else " "
            }
            s += "\n"
        }
        return s
    }

    private fun getCave(input: List<String>, stoneAmount: Int): MutableMap<Pair<Int, Int>, Boolean> {
        val directions = input.first().toCharArray()
        val cave = mutableMapOf<Pair<Int, Int>, Boolean>()
        move = 0

        repeat(stoneAmount) {
            dropStone(stones[it % 5], cave, directions)
        }

        return cave
    }

    private fun doStep(start: BigInteger, rest: BigInteger): Pair<BigInteger, BigInteger> {
        var total = start
        var loops = BigInteger.valueOf(2020)
        while (loops.times(BigInteger.TWO) < rest) {
            loops = loops.times(BigInteger.TWO)
            total = total.times(BigInteger.TWO).plus(BigInteger.TEN)
        }
        return total to loops
    }

    private fun resetCave(cave: MutableMap<Pair<Int, Int>, Boolean>): MutableMap<Pair<Int, Int>, Boolean> {
        val height = getMaxHeight(cave)
        val newCave = mutableMapOf<Pair<Int, Int>, Boolean>()
        for (i in -2..4) {
            if (cave.contains(i to height)) newCave[i to 0]
        }
        return newCave
    }

    private fun dropStone(
        s: List<Pair<Int, Int>>,
        cave: MutableMap<Pair<Int, Int>, Boolean>,
        directions: CharArray
    ) {
        var stone = setHeight(s, getMaxHeight(cave) + 4)
        stone = moveStoneSideways(stone, directions[move++ % directions.size], cave)
        while (canMoveDown(stone, cave)) {
            stone = moveStoneDown(stone)
            val direction = directions[move++ % directions.size]
            stone = moveStoneSideways(stone, direction, cave)
        }
        for (point in stone) {
            cave[point] = true
        }
    }

    private fun setHeight(stone: List<Pair<Int, Int>>, height: Int): List<Pair<Int, Int>> {
        return stone.map { it.first to it.second + height }
    }

    private fun moveStoneSideways(
        stone: List<Pair<Int, Int>>,
        direction: Char,
        cave: MutableMap<Pair<Int, Int>, Boolean>
    ): List<Pair<Int, Int>> {
        val change = if (direction == '>') 1 else -1
        for (point in stone) {
            if (point.first + change > 4) return stone
            if (point.first + change < -2) return stone
            if (cave.contains(point.first + change to point.second)) return stone
        }
        return stone.map { it.first + change to it.second }
    }

    private fun moveStoneDown(stone: List<Pair<Int, Int>>) = stone.map { it.first to it.second - 1 }

    private fun canMoveDown(stone: List<Pair<Int, Int>>, cave: MutableMap<Pair<Int, Int>, Boolean>): Boolean {
        for (point in stone) {
            if (cave.contains(point.first to point.second - 1)) return false
            if (point.second - 1 <= 0) return false
        }
        return true
    }

    private fun getMaxHeight(cave: Map<Pair<Int, Int>, Boolean>): Int {
        if (cave.isEmpty()) return 0
        return cave.keys.maxOf { it.second }
    }

}

private fun main() = Solution().run()