package `18`

import SolutionInterface

class Solution : SolutionInterface(testSolutionOne = 64, testSolutionTwo = 58) {
    private val directions = listOf(
        Triple(1, 0, 0),
        Triple(-1, 0, 0),
        Triple(0, 1, 0),
        Triple(0, -1, 0),
        Triple(0, 0, 1),
        Triple(0, 0, -1)
    )

    override fun exerciseOne(input: List<String>): Int {
        val droplets = input.map { it.split(",") }.map { Triple(it[0].toInt(), it[1].toInt(), it[2].toInt()) }

        return getResult(droplets, droplets, false)
    }

    override fun exerciseTwo(input: List<String>): Int {
        val droplets = input.map { it.split(",") }.map { Triple(it[0].toInt(), it[1].toInt(), it[2].toInt()) }

        val maxX = droplets.maxBy { it.first }.first
        val maxY = droplets.maxBy { it.second }.second
        val maxZ = droplets.maxBy { it.third }.third

        val reachable = getReachable(droplets, maxX + 1, maxY + 1, maxZ + 1)

        return getResult(droplets, reachable, true)
    }

    private fun getResult(droplets: List<Triple<Int, Int, Int>>, s: Collection<Triple<Int, Int, Int>>, b: Boolean): Int {
        var result = 0

        for (droplet in droplets) {
            directions.forEach { if (droplet.add(it) in s == b) result++ }
        }

        return result
    }

    private fun getReachable(
        droplets: List<Triple<Int, Int, Int>>,
        x: Int,
        y: Int,
        z: Int
    ): Set<Triple<Int, Int, Int>> {
        val reachable = mutableSetOf<Triple<Int, Int, Int>>()
        val queue = mutableListOf(Triple(0, 0, 0))

        while (queue.isNotEmpty()) {
            val current = queue.removeFirst()
            for (direction in directions) {
                val new = current.add(direction)
                if (new !in reachable && new !in droplets) {
                    if (new.first in -1..x && new.second in -1..y && new.third in -1..z) {
                        reachable.add(new)
                        queue.add(new)
                    }
                }
            }
        }
        return reachable
    }
}

fun Triple<Int, Int, Int>.add(other: Triple<Int, Int, Int>) =
    Triple(this.first + other.first, this.second + other.second, this.third + other.third)


private fun main() = Solution().run()