package `2`

import SolutionInterface

class Solution : SolutionInterface(testSolutionOne = 15, testSolutionTwo = 12) {

    override fun exerciseOne(input: List<String>): Int = input.map { it.split(" ") }
        .map { Pair(it.first(), it.last()) }
        .sumOf { getPointsOne(it) }

    override fun exerciseTwo(input: List<String>): Int = input.map { it.split(" ") }
        .map { Pair(it.first(), it.last()) }
        .sumOf { getPointsTwo(it) }


    private fun getPointsTwo(moves: Pair<String, String>): Int = when (moves.second) {
        "X" -> 0 + when (moves.first) {
            "A" -> 3
            "B" -> 1
            else -> 2

        }

        "Y" -> 3 + when (moves.first) {
            "A" -> 1
            "B" -> 2
            else -> 3
        }

        else -> 6 + when (moves.first) {
            "A" -> 2
            "B" -> 3
            else -> 1
        }
    }


    private fun getPointsOne(moves: Pair<String, String>): Int = when (moves.second) {
        "X" -> 1 + when (moves.first) {
            "A" -> 3
            "B" -> 0
            else -> 6
        }

        "Y" -> 2 + when (moves.first) {
            "A" -> 6
            "B" -> 3
            else -> 0
        }

        else -> 3 + when (moves.first) {
            "A" -> 0
            "B" -> 6
            else -> 3
        }
    }
}

private fun main() = Solution().run()