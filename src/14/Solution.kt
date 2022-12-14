package `14`

import SolutionInterface
import toward

class Solution : SolutionInterface(testSolutionOne = 24, testSolutionTwo = 93) {

    override fun exerciseOne(input: List<String>): Int {
        val rocky = buildRocky(input)
        val stop = rocky.keys.maxOf { it.second }
        var count = 0
        while (dropSand(rocky, stop)) {
            count++
        }
        return count
    }

    override fun exerciseTwo(input: List<String>): Int {
        val rocky = buildRocky(input)
        val stop = rocky.keys.maxOf { it.second } + 2
        var count = 0
        while (dropSandTwo(rocky, stop)) {
            count++
        }
        return count + 1
    }

    private fun buildRocky(input: List<String>) = mutableMapOf<Pair<Int, Int>, Boolean>().apply {
        input.map {
            it.split(" -> ")
                .map { c -> c.split(",") }
                .map { c -> c.first().toInt() to c.last().toInt() }
        }.forEach {
            it.windowed(2).forEach { line ->
                for (x in line.first().first toward line.last().first) {
                    for (y in line.first().second toward line.last().second) {
                        this[x - 500 to y] = true
                    }
                }
            }
        }
    }


    private fun dropSand(rocky: MutableMap<Pair<Int, Int>, Boolean>, stop: Int): Boolean {
        var current = 0 to 0
        while (current.second < stop) {
            if (!rocky.contains(current.first to current.second + 1)) {
                current = current.first to current.second + 1
            } else if (!rocky.contains(current.first - 1 to current.second + 1)) {
                current = current.first - 1 to current.second + 1
            } else if (!rocky.contains(current.first + 1 to current.second + 1)) {
                current = current.first + 1 to current.second + 1
            } else {
                rocky[current] = true
                return true
            }
        }
        return false
    }

    private fun dropSandTwo(rocky: MutableMap<Pair<Int, Int>, Boolean>, stop: Int): Boolean {
        var current = 0 to 0
        while (current.second < stop - 1) {
            if (!rocky.contains(current.first to current.second + 1)) {
                current = current.first to current.second + 1
            } else if (!rocky.contains(current.first - 1 to current.second + 1)) {
                current = current.first - 1 to current.second + 1
            } else if (!rocky.contains(current.first + 1 to current.second + 1)) {
                current = current.first + 1 to current.second + 1
            } else {
                rocky[current] = true
                if (current == 0 to 0) return false
                return true
            }
        }
        rocky[current] = true
        return true
    }

}

private fun main() = Solution().run()