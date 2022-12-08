package `8`

import SolutionInterface
import kotlin.math.max

class Solution : SolutionInterface(testSolutionOne = 21, testSolutionTwo = 8) {

    override fun exerciseOne(input: List<String>): Int {
        val forest = input.map { it.map { digit -> digit.digitToInt() } }
        var visible = 0

        for (i in 1..forest.size - 2) {
            for (j in 1..forest.size - 2) {
                if (isVisible(forest, i, j)) visible++
            }
        }

        return forest.size * 4 - 4 + visible
    }

    override fun exerciseTwo(input: List<String>): Int {
        val forest = input.map { it.map { digit -> digit.digitToInt() } }
        var max = 0

        for (i in forest.indices) {
            for (j in forest.indices) {
                getScenicScore(forest, i, j).also { max = max(it, max) }
            }
        }

        return max
    }

    private fun isVisible(forest: List<List<Int>>, i: Int, j: Int): Boolean {
        val value = forest[i][j]
        if (forest[i].take(j).max() < value) return true
        if (forest[i].takeLast(forest.size - j - 1).max() < value) return true
        val column = getColumn(forest, j)
        if (column.take(i).max() < value) return true
        return column.takeLast(forest.size - i - 1).max() < value
    }

    private fun getScenicScore(forest: List<List<Int>>, i: Int, j: Int): Int {
        val value = forest[i][j]
        val left = getDistance(forest[i].take(j).reversed(), value)
        val right = getDistance(forest[i].takeLast(forest.size - j - 1), value)
        val column = getColumn(forest, j)
        val top = getDistance(column.take(i).reversed(), value)
        val bottom = getDistance(column.takeLast(forest.size - i - 1), value)
        return left * right * bottom * top
    }

    private fun getDistance(trees: List<Int>, x: Int): Int {
        val nonBlockingCount = trees.takeWhile { it < x }.size
        if (nonBlockingCount == trees.size) return nonBlockingCount
        return nonBlockingCount + 1
    }

    private fun getColumn(forest: List<List<Int>>, column: Int) = forest.map { it[column] }

}

private fun main() = Solution().run()