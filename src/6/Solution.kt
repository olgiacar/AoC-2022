package `6`

import SolutionInterface

class Solution : SolutionInterface(testSolutionOne = 7, testSolutionTwo = 19) {

    override fun exerciseOne(input: List<String>) = getFirstMarker(input.first(), 4)

    override fun exerciseTwo(input: List<String>) = getFirstMarker(input.first(), 14)

    private fun getFirstMarker(input: String, window: Int) = getFirstMarkerIndex(input.windowed(window)) + window

    private fun getFirstMarkerIndex(chunks: List<String>): Int = chunks.indexOfFirst { isMarker(it) }

    private fun isMarker(s: String) = s.toSet().size == s.length

}

private fun main() = Solution().run()