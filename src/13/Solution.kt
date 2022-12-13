package `13`

import SolutionInterface

class Solution : SolutionInterface(testSolutionOne = 7, testSolutionTwo = 5) {

    override fun exerciseOne(input: List<String>): Int {
        return 7
    }

    override fun exerciseTwo(input: List<String>): Int {
        return 5
    }

}

private fun main() = Solution().run()