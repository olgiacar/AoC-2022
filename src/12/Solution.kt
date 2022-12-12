package `12`

import SolutionInterface

class Solution : SolutionInterface(testSolutionOne = 31, testSolutionTwo = 29) {

    override fun exerciseOne(input: List<String>): Int {
        val map = input.map { it.toCharArray() }
        val edges = mutableMapOf<Pair<Int, Int>, List<Pair<Int, Int>>>()
        var source: Pair<Int, Int> = 0 to 0
        var target: Pair<Int, Int> = 0 to 0

        map.forEachIndexed { x, row ->
            row.forEachIndexed { y, value ->
                edges[x to y] = getEdges(value, x, y, map)
                if (value == 'S') source = x to y
                if (value == 'E') target = x to y
            }
        }

        return getMinPath(source, target, edges, map)
    }

    override fun exerciseTwo(input: List<String>): Int {
        val map = input.map { it.toCharArray() }
        val edges = mutableMapOf<Pair<Int, Int>, List<Pair<Int, Int>>>()
        val sources = mutableListOf<Pair<Int, Int>>()
        var target: Pair<Int, Int> = 0 to 0

        map.forEachIndexed { x, row ->
            row.forEachIndexed { y, value ->
                edges[x to y] = getEdges(value, x, y, map)
                if (value == 'E') target = x to y
                if (value == 'a') sources.add(x to y)
            }
        }

        return sources.map { getMinPath(it, target, edges, map) }.filter { it > 0 }.min()
    }

    private fun getMinPath(
        source: Pair<Int, Int>,
        target: Pair<Int, Int>,
        edges: Map<Pair<Int, Int>, List<Pair<Int, Int>>>,
        map: List<CharArray>,
    ): Int {
        val queue = mutableMapOf<Pair<Int, Int>, Int>()
        map.forEachIndexed { x, row ->
            row.forEachIndexed { y, _ ->
                queue[x to y] = Int.MAX_VALUE
            }
        }

        queue[source] = 0
        val visited = mutableMapOf<Pair<Int, Int>, Int>()

        while (!visited.contains(target)) {
            val current = getMinVertex(queue)
            visited[current] = queue.remove(current)!!
            for (neighbour in edges[current]!!) {
                if (!visited.contains(neighbour)) queue[neighbour] = visited[current]!! + 1
            }
        }

        return if (visited[target]!! > 0) visited[target]!! else Int.MAX_VALUE
    }

    private fun getEdges(value: Char, x: Int, y: Int, map: List<CharArray>): List<Pair<Int, Int>> {
        val adjacent = mutableListOf<Pair<Int, Int>>()
        for (direction in listOf(-1 to 0, 1 to 0, 0 to -1, 0 to 1)) {
            val current = x + direction.first to y + direction.second
            if (current.first in map.indices && current.second in map[x].indices) {
                if (map[current.first][current.second] == 'E') {
                    if (value == 'z') adjacent.add(current)
                } else if (value >= map[current.first][current.second] - 1 || value == 'S') {
                    adjacent.add(current)
                }
            }
        }
        return adjacent
    }

    private fun getMinVertex(visited: Map<Pair<Int, Int>, Int>): Pair<Int, Int> {
        return visited.toList().minByOrNull { it.second }!!.first
    }

}

private fun main() = Solution().run()