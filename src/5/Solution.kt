package `5`

import SolutionInterface

class Solution : SolutionInterface(testSolutionOne = "CMZ", testSolutionTwo = "MCD") {
    private var stacks: MutableList<MutableList<Char>> = mutableListOf()

    override fun exerciseOne(input: List<String>) = calcSolution(input, ::makeMove)

    override fun exerciseTwo(input: List<String>) = calcSolution(input, ::makeMoveTwo)

    private fun calcSolution(
        input: List<String>,
        function: (move: Triple<Int, Int, Int>, stacks: MutableList<MutableList<Char>>) -> Unit
    ): String {
        initializeStacks(input)
        getMoves(input).forEach { function(it, stacks) }
            .also { return stacks.map { it.last() }.fold("") { acc, c -> "$acc$c" } }
    }

    private fun makeMove(move: Triple<Int, Int, Int>, stacks: MutableList<MutableList<Char>>) {
        repeat(move.first) {
            stacks[move.third - 1].add(stacks[move.second - 1].removeLast())
        }
    }

    private fun makeMoveTwo(move: Triple<Int, Int, Int>, stacks: MutableList<MutableList<Char>>) {
        (stacks[move.second - 1].size - move.first)
            .also { stacks[move.third - 1].addAll(stacks[move.second - 1].drop(it)) }
            .also { repeat(move.first) { stacks[move.second - 1].removeLast() } }
    }

    private fun initializeStacks(input: List<String>) {
        mutableListOf<MutableList<Char>>()
            .also { list ->
                repeat(input.first().length / 4 + 1) { list.add(mutableListOf()) }
            }.also { stacks = it }

        input.takeWhile { it.trim().startsWith("[") }
            .reversed()
            .forEach {
                it.chunked(4).forEachIndexed { i, chunk ->
                    if (chunk[1] != ' ') stacks[i].add(chunk[1])
                }
            }
    }

    private fun getMoves(input: List<String>): List<Triple<Int, Int, Int>> =
        input.dropWhile { !it.startsWith("move") }
            .map { it.split(" ") }
            .map { Triple(it[1].toInt(), it[3].toInt(), it[5].toInt()) }

}

private fun main() = Solution().run()