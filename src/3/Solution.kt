package `3`

import SolutionInterface

class Solution : SolutionInterface(testSolutionOne = 157, testSolutionTwo = 70) {
    override fun exerciseOne(input: List<String>): Int {
        return input.map { it.take(it.length / 2) to it.drop(it.length / 2) }
            .map { getCommon(it) }
            .sumOf { getPriority(it) }
    }

    override fun exerciseTwo(input: List<String>): Int {
        return input.chunked(3).map { Triple(it.first(), it[1], it.last()) }
            .map { getCommon(it) }
            .sumOf { getPriority(it) }
    }

    private fun getCommon(rucksack: Pair<String, String>): Char {
        val x = rucksack.first.toSet()
        val y = rucksack.second.toSet()

        val common = x.intersect(y)
        return common.first();
    }

    private fun getCommon(rucksack: Triple<String, String, String>): Char {
        val x = rucksack.first.toSet()
        val y = rucksack.second.toSet()
        val z = rucksack.third.toSet()

        val common = x.intersect(y).intersect(z)
        return common.first()
    }

    private fun getPriority(letter: Char): Int {
        val offset = 'a'.code - 1
        for (c in 'a'..'z') {
            if (letter == c) return c.code - offset
            if (letter == c.uppercaseChar()) return c.code - offset + 26
        }
        return 0
    }
}

private fun main() = Solution().run()