package `1`

import SolutionInterface

class Solution : SolutionInterface(testSolutionOne = 24000, testSolutionTwo = 45000) {

    override fun exerciseOne(input: List<String>): Int {
        return getOrderedTotals(input).first()
    }

    override fun exerciseTwo(input: List<String>): Int {
        return getOrderedTotals(input).take(3).sum()
    }

    private fun getOrderedTotals(input: List<String>): List<Int> {
        return input.joinToString(",")
            .split(",,")
            .map { it.split(",") }
            .map {
                it.map { value -> value.toInt() }
            }.map {
                it.sum()
            }.sortedDescending()
    }

}

private fun main() = Solution().run()