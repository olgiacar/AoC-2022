package `16`

import SolutionInterface
import kotlin.math.pow

class Solution : SolutionInterface(testSolutionOne = 1651, testSolutionTwo = 1707) {

    override fun exerciseOne(input: List<String>): Int {
        val flows = getFlows(input)
        val positiveValves = getPositives(flows).apply { add("AA") }
        val neighbours = getNeighbours(input)
        val weights = getWeights(positiveValves, neighbours)

        return doStep(30, 0, "AA", flows, setOf(), weights)
    }

    override fun exerciseTwo(input: List<String>): Int {
        val flows = getFlows(input)
        val positiveValves = getPositives(flows)
        val neighbours = getNeighbours(input)

        var max = 0

        repeat(2.0.pow(positiveValves.size).toInt()) {
            val (human, elephant) = getSubsets(positiveValves, it)
            human.add("AA")
            elephant.add("AA")
            val weightsHuman = getWeights(human, neighbours)
            val weightsElephant = getWeights(elephant, neighbours)
            val maxHuman = doStep(26, 0, "AA", flows, setOf(), weightsHuman)
            val maxElephant = doStep(26, 0, "AA", flows, setOf(), weightsElephant)
            if (maxHuman + maxElephant > max) {
                max = maxHuman + maxElephant
            }
        }

        return max
    }

    private fun getNeighbours(input: List<String>): Map<String, List<String>> {
        return input.associate {
            it.split(" ")[1] to it.split("to valve |to valves ".toRegex()).last().split(", ")
        }
    }

    private fun getSubsets(positiveValves: List<String>, key: Int): Pair<MutableList<String>, MutableList<String>> {
        val human = mutableListOf<String>()
        val elephant = mutableListOf<String>()
        key.toString(2).forEachIndexed { i, it ->
            if (it == '1') human.add(positiveValves[i])
            else elephant.add(positiveValves[i])
        }
        return human to elephant
    }

    private fun getWeights(
        positiveValves: MutableList<String>,
        neighbours: Map<String, List<String>>
    ): MutableMap<String, Map<String, Int>> {
        val weights = mutableMapOf<String, Map<String, Int>>()
        for (start in positiveValves) {
            weights[start] = mutableMapOf<String, Int>().apply {
                positiveValves.forEach { this[it] = getDistance(start, it, neighbours) }
            }
        }
        return weights
    }

    private fun getFlows(input: List<String>) = input.map {
        it.split(";").first().split(" ")
    }.associate {
        it[1] to it[4].split("=").last().toInt()
    }

    private fun getPositives(flows: Map<String, Int>) = flows.filter { it.value > 0 }.keys.toMutableList()

    private fun doStep(
        step: Int,
        total: Int,
        valve: String,
        flows: Map<String, Int>,
        opened: Set<String>,
        weights: Map<String, Map<String, Int>>
    ): Int {
        if (step <= 0) return total
        val op = opened.toMutableSet().apply { add(valve) }
        var max = total
        for (neighbour in weights[valve]!!) {
            if (neighbour.key !in op) {
                if (neighbour.value + step > 0) {
                    val newStep = step - neighbour.value - 1
                    val rate = newStep * flows[neighbour.key]!!
                    doStep(newStep, total + rate, neighbour.key, flows, op, weights)
                        .also { if (it > max) max = it }
                }
            }
        }
        return max
    }

    private fun getDistance(start: String, end: String, neighbours: Map<String, List<String>>): Int {
        if (start == end) return 0
        val queue = mutableMapOf<String, Int>().apply {
            neighbours.keys.forEach { this[it] = Int.MAX_VALUE }
            this[start] = 0
        }
        val visited = mutableMapOf<String, Int>()

        while (!visited.contains(end)) {
            val current = queue.minBy { it.value }.key
            visited[current] = queue.remove(current)!!
            for (neighbour in neighbours[current]!!) {
                if (neighbour == end) return visited[current]!! + 1
                if (!visited.contains(neighbour)) queue[neighbour] = visited[current]!! + 1
            }
        }

        return Int.MAX_VALUE
    }

}

private fun main() = Solution().run()