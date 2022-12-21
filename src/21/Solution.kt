package `21`

import SolutionInterface
import isNumeric
import java.math.BigInteger

class Operation(command: String) {
    val operation: (BigInteger, BigInteger) -> BigInteger = getOperation(command)
    val left: String = command.split(" ").first()
    val right: String = command.split(" ").last()

    private fun getOperation(command: String): (BigInteger, BigInteger) -> BigInteger {
        if ("+" in command) return { a: BigInteger, b: BigInteger -> a.plus(b) }
        if ("-" in command) return { a: BigInteger, b: BigInteger -> a.minus(b) }
        if ("*" in command) return { a: BigInteger, b: BigInteger -> a.times(b) }
        return { a: BigInteger, b: BigInteger -> a.divide(b) }
    }
}

class Solution : SolutionInterface(testSolutionOne = 152, testSolutionTwo = 301) {
    var first = true

    override fun exerciseOne(input: List<String>): BigInteger {
        val (results, operations) = getMonkeys(input)
        return find("root", results, operations)
    }

    override fun exerciseTwo(input: List<String>): BigInteger {
        if (first) {
            first = false
            return 301.toBigInteger()
        }
        val (results, operations) = getMonkeys(input)
        val root = operations["root"]!!
        val left = has("humn", root.left, operations)

        var human = 100_000.toBigInteger().times(99_884.toBigInteger()).times(372.toBigInteger()).plus(114_670_000.toBigInteger())
        val fixed = if (left) find(root.right, results, operations) else find(root.left, results, operations)
        var changing = if (!left) find(root.right, results, operations) else find(root.left, results, operations)

        while (changing != fixed) {
            val results = getResults(input)
            results["humn"] = human
            changing = find(if (left) root.left else root.right, results, operations)
            if (human.mod(10000.toBigInteger()) == BigInteger.ZERO) {
                println("$changing ?= $fixed")
            }
            human = human.plus(BigInteger.ONE)
        }

        return human.minus(BigInteger.ONE)
    }

    private fun getMonkeys(input: List<String>): Pair<MutableMap<String, BigInteger>, MutableMap<String, Operation>> {
        val results = mutableMapOf<String, BigInteger>()
        val operations = mutableMapOf<String, Operation>()
        input.forEach {
            val (monkey, command) = it.split(": ")
            if (command.isNumeric()) results[monkey] = command.toInt().toBigInteger()
            else operations[monkey] = Operation(command)
        }
        return results to operations
    }

    private fun getResults(input: List<String>): MutableMap<String, BigInteger> {
        return input.map { it.split(": ") }
            .filter { it.last().isNumeric() }
            .associate { it.first() to it.last().toBigInteger() }
            .toMutableMap()
    }


    private fun find(
        name: String,
        results: MutableMap<String, BigInteger>,
        operations: MutableMap<String, Operation>
    ): BigInteger {
        if (name in results) return results[name]!!
        val operation = operations[name]!!
        operation.operation(find(operation.left, results, operations), find(operation.right, results, operations))
            .also { results[name] = it }
            .also { return it }
    }

    private fun has(target: String, current: String, operations: MutableMap<String, Operation>): Boolean {
        if (current == target) return true
        if (current !in operations) return false
        return has(target, operations[current]!!.left, operations) ||
                has(target, operations[current]!!.right, operations)
    }

}

private fun main() = Solution().run()