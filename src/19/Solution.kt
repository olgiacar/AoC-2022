package `19`

import SolutionInterface
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking

abstract class Robot(open val ore: Int) {
    open fun canBeBuilt(materials: Materials) = ore <= materials.ore
}

class OreRobot(override val ore: Int) : Robot(ore)
class ClayRobot(override val ore: Int) : Robot(ore)
class ObsidianRobot(override val ore: Int, val clay: Int) : Robot(ore) {
    override fun canBeBuilt(materials: Materials) = super.canBeBuilt(materials) && clay <= materials.clay
}

class GeodeRobot(override val ore: Int, val obsidian: Int) : Robot(ore) {
    override fun canBeBuilt(materials: Materials) = super.canBeBuilt(materials) && obsidian <= materials.obsidian
}

data class Materials(val ore: Int, val clay: Int, val obsidian: Int, val geode: Int)
data class Robots(val ore: Int, val clay: Int, val obsidian: Int, val geode: Int)


class Blueprint(line: String) {
    val id: Int
    lateinit var oreRobot: OreRobot
    lateinit var clayRobot: ClayRobot
    lateinit var obsidianRobot: ObsidianRobot
    lateinit var geodeRobot: GeodeRobot

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
    }
}

class Solution : SolutionInterface(testSolutionOne = 33, testSolutionTwo = 5) {
    private val robotTypes = listOf("ORE", "CLAY", "OBSIDIAN", "GEODE")
    private val memo = mutableMapOf<Triple<Materials, Robots, Int>, Int>()

    override fun exerciseOne(input: List<String>): Int {
        val blueprints = input.map { Blueprint(it) }

        val obs = blueprints.map {
            runBlocking {
                it.id to launch {
                    getGeode(
                        it,
                        24,
                        Materials(0, 0, 0, 0),
                        Robots(1, 0, 0, 0)
                    )
                }
            }
        }

        return obs.sumOf { it.first * it.second }
    }

    override fun exerciseTwo(input: List<String>): Int {
        return 5
        val blueprints = input.map { Blueprint(it) }.take(3)

        val obs = blueprints.map {
            runBlocking {
                getGeode(
                    it,
                    32,
                    Materials(0, 0, 0, 0),
                    Robots(1, 0, 0, 0)
                )
            }

        }

        return obs.fold(1) { it, acc -> it * acc }
    }

    private suspend fun getGeode(blueprint: Blueprint, step: Int, materials: Materials, robots: Robots): Int {
        if (step == 0) return materials.geode
        var max = materials.geode

        if (blueprint.geodeRobot.canBeBuilt(materials)) {
            val newMaterials = getUpdatedMaterials(materials, robots)
            val m = Materials(
                newMaterials.ore - blueprint.geodeRobot.ore,
                newMaterials.clay,
                newMaterials.obsidian - blueprint.geodeRobot.obsidian,
                newMaterials.geode
            )
            val r = Robots(robots.ore, robots.clay, robots.obsidian, robots.geode + 1)
            getGeode(blueprint, step - 1, m, r).also { if (it > max) max = it }
        } else {
            for (type in robotTypes) {
                when (type) {
                    "ORE" -> {
                        if (blueprint.oreRobot.canBeBuilt(materials)) {
                            val newMaterials = getUpdatedMaterials(materials, robots)
                            val m = Materials(
                                newMaterials.ore - blueprint.oreRobot.ore,
                                newMaterials.clay,
                                newMaterials.obsidian,
                                newMaterials.geode
                            )
                            val r = Robots(robots.ore + 1, robots.clay, robots.obsidian, robots.geode)
                            getGeode(blueprint, step - 1, m, r).also { if (it > max) max = it }
                        }
                    }

                    "CLAY" -> {
                        if (blueprint.clayRobot.canBeBuilt(materials)) {
                            val newMaterials = getUpdatedMaterials(materials, robots)
                            val m = Materials(
                                newMaterials.ore - blueprint.clayRobot.ore,
                                newMaterials.clay,
                                newMaterials.obsidian,
                                newMaterials.geode
                            )
                            val r = Robots(robots.ore, robots.clay + 1, robots.obsidian, robots.geode)
                            getGeode(blueprint, step - 1, m, r).also { if (it > max) max = it }
                        }
                    }

                    "OBSIDIAN" -> {
                        if (blueprint.obsidianRobot.canBeBuilt(materials)) {
                            val newMaterials = getUpdatedMaterials(materials, robots)
                            val m = Materials(
                                newMaterials.ore - blueprint.obsidianRobot.ore,
                                newMaterials.clay - blueprint.obsidianRobot.clay,
                                newMaterials.obsidian,
                                newMaterials.geode
                            )
                            val r = Robots(robots.ore, robots.clay, robots.obsidian + 1, robots.geode)
                            getGeode(blueprint, step - 1, m, r).also { if (it > max) max = it }
                        }
                    }

                    else -> {
                        if (
                            !blueprint.oreRobot.canBeBuilt(materials) ||
                            !blueprint.clayRobot.canBeBuilt(materials) ||
                            !blueprint.obsidianRobot.canBeBuilt(materials)
                        )
                            getGeode(
                                blueprint,
                                step - 1,
                                getUpdatedMaterials(materials, robots),
                                robots
                            ).also { if (it > max) max = it }
                    }
                }
            }
        }

        return max
    }

    private fun getUpdatedMaterials(materials: Materials, robots: Robots): Materials {
        val (ore, clay, obsidian, geode) = materials
        val (oreRobots, clayRobots, obsidianRobots, geodeRobots) = robots
        return Materials(ore + oreRobots, clay + clayRobots, obsidian + obsidianRobots, geode + geodeRobots)
    }

}

private fun main() = Solution().run()