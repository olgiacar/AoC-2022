package `9`

import SolutionInterface
import kotlin.math.abs
import kotlin.math.sign

class Solution : SolutionInterface(testSolutionOne = 13, testSolutionTwo = 1) {
    private val visited = mutableSetOf<Pair<Int, Int>>()
    private val rope = mutableListOf<Pair<Int, Int>>()

    private fun initRope(length: Int) {
        visited.clear()
        visited.add(0 to 0)
        rope.clear()
        repeat(length) {
            rope.add(0 to 0)
        }
    }

    override fun exerciseOne(input: List<String>): Int {
        initRope(2)

        doThing(input)

        return visited.size
    }

    override fun exerciseTwo(input: List<String>): Int {
        initRope(10)

        doThing(input)

        return visited.size
    }

    private fun doThing(input: List<String>) {
        input.map { it.split(" ") }
            .map { it.first() to it.last().toInt() }
            .forEach {
                makeMove(it)
            }
    }

    private fun step(head: Pair<Int, Int>, tail: Pair<Int, Int>): Pair<Int, Int> {
        if (abs(head.first - tail.first) > 1 || abs(head.second - tail.second) > 1) {
            return tail.first + (head.first - tail.first).sign to tail.second + (head.second - tail.second).sign
        }
        return tail
    }

    private fun add(a: Pair<Int, Int>, b: Pair<Int, Int>) = a.first + b.first to a.second + b.second

    private fun makeMove(it: Pair<String, Int>) {
        for (i in 0 until it.second) {
            when (it.first) {
                "U" -> moveRope(0 to 1)
                "D" -> moveRope(0 to -1)
                "R" -> moveRope(1 to 0)
                "L" -> moveRope(-1 to 0)
            }
        }
    }

    private fun moveRope(direction: Pair<Int, Int>) {
        rope[0] = add(rope.first(), direction)
        for (i in 1 until rope.size)
            rope[i] = step(rope[i - 1], rope[i])
        visited.add(rope.last())
    }

}

private fun main() = Solution().run()