package `10`

import SolutionInterface

class Solution : SolutionInterface(testSolutionOne = 13140, testSolutionTwo = 5) {

    override fun exerciseOne(input: List<String>): Int {
        val values = getXValues(input)
        var total = 0
        var x = 20
        while (x <= values.size) {
            total += values[x - 1] * x
            x += 40
        }
        return total
    }

    override fun exerciseTwo(input: List<String>): Int {
        val values = getXValues(input)

        repeat(6) { r ->
            var line = ""
            repeat(40) {
                val current = r * 40 + it
                val pos = values[current]
                line += if (it >= pos - 1 && it <= pos + 1) "#" else " "
            }
            println(line)
        }
        return 5
    }

    private fun getXValues(input: List<String>): List<Int> {
        val values = mutableListOf<Int>()
        var currentPosition = 1
        for (instruction in input) {
            if (instruction == "noop") {
                values.add(currentPosition)
            } else {
                values.add(currentPosition)
                values.add(currentPosition)
                currentPosition += instruction.drop(5).toInt()
            }
        }
        return values
    }

}

private fun main() = Solution().run()