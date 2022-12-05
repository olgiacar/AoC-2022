package `5`

import SolutionInterface

class Solution : SolutionInterface(testSolutionOne = 7, testSolutionTwo = 5) {
    private var stacks: MutableList<MutableList<Char>> = mutableListOf()

    override fun exerciseOne(input: List<String>): Int {
        initializeStacks(input)
        getMoves(input)
            .map { it.split(" ") }
            .map { Triple(it[1].toInt(), it[3].toInt(), it[5].toInt()) }
            .forEach { makeMove(it, stacks) }

        printSolution()

        return 7
    }

    override fun exerciseTwo(input: List<String>): Int {
        initializeStacks(input)
        getMoves(input)
            .map { it.split(" ") }
            .map { Triple(it[1].toInt(), it[3].toInt(), it[5].toInt()) }
            .forEach { makeMoveTwo(it, stacks) }

        printSolution()

        return 5
    }

    private fun printSolution() {
        val solution = stacks.map { it.last() }
            .fold("") { acc, c -> "$acc$c" }
        println(solution)
    }

    private fun makeMove(move: Triple<Int, Int, Int>, stacks: MutableList<MutableList<Char>>) {
        repeat(move.first) {
            stacks[move.third - 1].add(stacks[move.second - 1].removeLast())
        }
    }

    private fun makeMoveTwo(move: Triple<Int, Int, Int>, stacks: MutableList<MutableList<Char>>) {
        val amount = stacks[move.second - 1].size - move.first
        stacks[move.third - 1].addAll(stacks[move.second - 1].drop(amount))
        repeat(move.first) { stacks[move.second - 1].removeLast() }
    }

    private fun initializeStacks(input: List<String>) {
        stacks = mutableListOf()
        repeat(input.first().length / 4 + 1) { stacks.add(mutableListOf()) }

        val stackData = input.takeWhile { it.trim().startsWith("[") }
            .reversed()

        stackData.forEach {
            stacks.forEachIndexed { i, stack ->
                val crate = it[i * 4 + 1]
                if (crate != ' ') stack.add(crate)
            }
        }
    }

    private fun getMoves(input: List<String>): List<String> {
        for (i in input.indices) {
            if (input[i] == "") {
                return input.drop(i + 1)
            }
        }
        return input
    }

}

private fun main() = Solution().run()