package `15`

import SolutionInterface
import java.math.BigInteger
import kotlin.math.absoluteValue

class Solution : SolutionInterface(testSolutionOne = 26, testSolutionTwo = 56000011) {
    private var testOne = true
    private var testTwo = true

    override fun exerciseOne(input: List<String>): Int {
        val row = if (testOne) 10 else 2_000_000
        testOne = testOne.not()

        val notPossible = mutableSetOf<Int>()
        val sensors = getSensors(input)

        sensors.forEach { doPair(it, notPossible, row) }
        sensors.map { it.second }
            .filter { it.second == row }
            .forEach { notPossible.remove(it.first) }

        return notPossible.size
    }

    override fun exerciseTwo(input: List<String>): BigInteger {
        val maxRange = if (testTwo) 20 else 4_000_000
        testTwo = testTwo.not()

        val sensors = getSensors(input)
        sensors.forEach {
            val (sensor, beacon) = it
            val distance = getDistance(sensor, beacon) + 1
            for (i in -distance..distance) {
                var current = sensor.first + i to sensor.second + (distance + i)
                if (isNotCovered(current, sensors, maxRange))
                    return getTuningFrequency(current.first, current.second)
                current = sensor.first + i to sensor.second - (distance + i)
                if (isNotCovered(current, sensors, maxRange))
                    return getTuningFrequency(current.first, current.second)
            }
        }

        return BigInteger.ZERO
    }

    private fun getTuningFrequency(x: Int, y: Int) = BigInteger.valueOf(x.toLong())
        .multiply(BigInteger.valueOf(4_000_000.toLong()))
        .add(BigInteger.valueOf(y.toLong()))

    private fun getSensors(input: List<String>) = input.map {
        it.split(":").map { c ->
            c.substringAfter("at ").split(", ")
                .map { s -> s.substring(2).toInt() }
        }.map { s -> s.first() to s.last() }
    }.map { it.first() to it.last() }

    private fun isNotCovered(
        position: Pair<Int, Int>,
        sensors: List<Pair<Pair<Int, Int>, Pair<Int, Int>>>,
        max: Int
    ): Boolean {
        if (position.first !in 0..max || position.second !in 0..max) return false
        for (pair in sensors) {
            val (sensor, beacon) = pair
            if (getDistance(sensor, position) <= getDistance(sensor, beacon)) return false
        }
        return true
    }

    private fun doPair(pair: Pair<Pair<Int, Int>, Pair<Int, Int>>, notPossible: MutableSet<Int>, y: Int) {
        val (sensor, beacon) = pair
        val distance = getDistance(sensor, beacon)
        val range = (sensor.second - y).absoluteValue
        (-(distance - range)..(distance - range)).forEach { notPossible.add(sensor.first + it) }
    }

    private fun getDistance(source: Pair<Int, Int>, target: Pair<Int, Int>): Int {
        return (source.first - target.first).absoluteValue + (source.second - target.second).absoluteValue
    }

}

private fun main() = Solution().run()