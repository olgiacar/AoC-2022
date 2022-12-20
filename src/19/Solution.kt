package `19`

import SolutionInterface

abstract class Robot {
    abstract val price: Materials
    abstract val amount: Robots
    open fun canBeBuilt(materials: Materials) =
        price.ore <= materials.ore &&
                price.clay <= materials.clay &&
                price.obsidian <= materials.obsidian &&
                price.geode <= materials.geode
}

class NoRobot : Robot() {
    override val price = Materials(0, 0, 0, 0)
    override val amount = Materials(0, 0, 0, 0)
}

class OreRobot(ore: Int) : Robot() {
    override val price = Materials(ore, 0, 0, 0)
    override val amount = Materials(1, 0, 0, 0)
}

class ClayRobot(ore: Int) : Robot() {
    override val price = Materials(ore, 0, 0, 0)
    override val amount = Materials(0, 1, 0, 0)
}

class ObsidianRobot(ore: Int, clay: Int) : Robot() {
    override val price = Materials(ore, clay, 0, 0)
    override val amount = Materials(0, 0, 1, 0)
}

class GeodeRobot(ore: Int, obsidian: Int) : Robot() {
    override val price = Materials(ore, 0, obsidian, 0)
    override val amount = Materials(0, 0, 0, 1)
}

class Resource(val ore: Int, val clay: Int, val obsidian: Int, val geode: Int) : Comparable<Resource> {
    override operator fun compareTo(other: Resource): Int {
        if (other.geode.compareTo(geode) != 0) return other.geode.compareTo(geode)
        if (other.obsidian.compareTo(obsidian) != 0) return other.obsidian.compareTo(obsidian)
        if (other.clay.compareTo(clay) != 0) return other.clay.compareTo(clay)
        return other.ore.compareTo(ore)
    }

    fun add(other: Resource) = Resource(
        ore + other.ore,
        clay + other.clay,
        obsidian + other.obsidian,
        geode + other.geode,
    )

    fun sub(other: Resource) = Resource(
        ore - other.ore,
        clay - other.clay,
        obsidian - other.obsidian,
        geode - other.geode,
    )
}

typealias Materials = Resource
typealias Robots = Resource


data class Status(val materials: Materials, val robots: Robots) : Comparable<Status> {
    override operator fun compareTo(other: Status): Int {
        return materials.add(robots).compareTo(other.materials.add(other.robots))
    }
}


class Blueprint(line: String) {
    val id: Int
    private lateinit var oreRobot: OreRobot
    private lateinit var clayRobot: ClayRobot
    private lateinit var obsidianRobot: ObsidianRobot
    private lateinit var geodeRobot: GeodeRobot
    private val noRobot = NoRobot()
    val robots = mutableListOf<Robot>()

    init {
        id = line.split(": ").first().split(" ").last().toInt()
        line.split(": ").last().split(".").filter { it.isNotBlank() }.forEachIndexed { index, it ->
            val x = it.split("costs ").last()
            val ore = x.split(" ore").first().toInt()
            when (index) {
                0 -> oreRobot = OreRobot(ore)
                1 -> clayRobot = ClayRobot(ore)
                2 -> {
                    val clay = x.split(" and ").last().split(" clay").first().toInt()
                    obsidianRobot = ObsidianRobot(ore, clay)
                }

                3 -> {
                    val obsidian = x.split(" and ").last().split(" obsidian").first().toInt()
                    geodeRobot = GeodeRobot(ore, obsidian)
                }
            }
        }
        robots.addAll(listOf(oreRobot, clayRobot, obsidianRobot, geodeRobot, noRobot))
    }
}

class Solution : SolutionInterface(testSolutionOne = 33, testSolutionTwo = 3472) {
    private val initialState = Status(Materials(0, 0, 0, 0), Robots(1, 0, 0, 0))

    override fun exerciseOne(input: List<String>): Int {
        val blueprints = input.map { Blueprint(it) }

        return blueprints.map { it.id to getMaxGeode(it, 24, initialState) }.sumOf { it.first * it.second }
    }

    override fun exerciseTwo(input: List<String>): Int {
        val blueprints = input.map { Blueprint(it) }.take(3)

        val obs = blueprints.map { getMaxGeode(it, 32, initialState) }

        return obs.fold(1) { it, acc -> it * acc }
    }

    private fun getMaxGeode(blueprint: Blueprint, step: Int, initialState: Status): Int {
        val queue = mutableListOf(initialState)
        repeat(step) {
            val nextQueue = mutableSetOf<Status>()
            for (state in queue) {
                for (robot in blueprint.robots) {
                    addToQueue(robot, state, nextQueue)
                }
            }
            queue.clear()
            queue.addAll(nextQueue.toList().sorted().take(50_000))
        }
        return queue.maxOf { it.materials.geode }
    }

    private fun addToQueue(robot: Robot, state: Status, queue: MutableSet<Status>) {
        if (robot.canBeBuilt(state.materials)) {
            val newMaterials: Materials = state.materials
                .add(state.robots)
                .sub(robot.price)
            val newRobots = state.robots.add(robot.amount)
            queue.add(Status(newMaterials, newRobots))
        }
    }

}

private fun main() = Solution().run()