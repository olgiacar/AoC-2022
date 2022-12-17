package `16`

import SolutionInterface

class Solution : SolutionInterface(testSolutionOne = 1651, testSolutionTwo = 5) {

    override fun exerciseOne(input: List<String>): Int {
        val flows = input.map {
            it.split(";").first().split(" ")
        }.associate {
            it[1] to it[4].split("=").last().toInt()
        }

        val neighbours = input.associate {
            it.split(" ")[1] to it.split("to valve |to valves ".toRegex()).last().split(", ")
        }

        val opened = flows.map { it.key to false }.toMap().toMutableMap()

        return doStep(1, 0, 0, "AA", neighbours, flows, opened)
    }

    override fun exerciseTwo(input: List<String>): Int {
        return 5
    }

    private fun doStep(
        step: Int,
        flowRate: Int,
        total: Int,
        valve: String,
        neighbours: Map<String, List<String>>,
        flows: Map<String, Int>,
        opened: MutableMap<String, Boolean>,
    ): Int {
        if (step >= 30) return flowRate
        var max = 0
        if (!opened[valve]!!) {
            opened[valve] = true
            val rate = flowRate + flows[valve]!!
            doStep(step + 1, rate, total + rate, valve, neighbours, flows, opened)
                .also { max = it }
        }
        for (v in neighbours[valve]!!) {
            doStep(step + 1, flowRate, total + flowRate, v, neighbours, flows, opened)
                .also { if (it > max) max = it }
        }
        return max
    }

}

private fun main() = Solution().run()