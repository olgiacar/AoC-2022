package `4`

import SolutionInterface

class Solution : SolutionInterface(testSolutionOne = 2, testSolutionTwo = 4) {

    override fun exerciseOne(input: List<String>): Int {
        return getRangePairs(input)
            .count { isInRange(it.first, it.second) }
    }

    override fun exerciseTwo(input: List<String>): Int {
        return getRangePairs(input)
            .count { overlaps(it.first, it.second) }
    }

    private fun getRangePairs(input: List<String>): List<Pair<Pair<Int, Int>, Pair<Int, Int>>> {
        return input.asSequence()
            .map { toRangePair(it) }
            .toList()
    }

    private fun toRangePair(ranges: String): Pair<Pair<Int, Int>, Pair<Int, Int>> {
        return ranges.split(",").asSequence()
            .map { it.split("-") }
            .map { it.map { x -> x.toInt() } }
            .map {
                it.first() to it.last()
            }.zipWithNext().first()
    }

    private fun isInRange(first: Pair<Int, Int>, second: Pair<Int, Int>): Boolean =
        second.first in first.first..first.second &&
                second.second in first.first..first.second ||
                first.first in second.first..second.second &&
                first.second in second.first..second.second

    private fun overlaps(first: Pair<Int, Int>, second: Pair<Int, Int>): Boolean {
        return first.second >= second.first && second.second >= first.first
    }

}

private fun main() = Solution().run()