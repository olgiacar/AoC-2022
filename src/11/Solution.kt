package `11`

import SolutionInterface
import java.math.BigInteger


class Monkey(
    startingItems: String,
    operation: String,
    val test: Int,
    private val trueCase: Int,
    private val falseCase: Int
) {
    var inspected = 0
    val items: MutableList<Int> = startingItems.split(", ").map { it.toInt() }.toMutableList()
    val op: (Int, Int) -> Int

    init {
        val tokens = operation.split(" ").take(3)
        op = { it: Int, gcd: Int ->
            val first = if (tokens.first() == "old") it else tokens.first().toInt()
            val second = if (tokens.last() == "old") it else tokens.last().toInt()
            if (tokens[1] == "+") {
                (first + second) % gcd
            } else {
                first.toBigInteger().multiply(second.toBigInteger()).mod(gcd.toBigInteger()).toInt()
            }
        }
    }

    fun inspect(it: Int): Int {
        inspected++
        return if (isDivisible(it)) trueCase else falseCase
    }

    private fun isDivisible(it: Int) = it % test == 0

}

class Solution : SolutionInterface(testSolutionOne = 10605, testSolutionTwo = 2713310158) {

    override fun exerciseOne(input: List<String>): Int {
        val monkeys = getMonkeys(input)
        doRounds(monkeys, 20, 3)

        return monkeys.map { it.inspected }.sortedDescending().take(2).fold(1) { it, acc -> it * acc }
    }

    override fun exerciseTwo(input: List<String>): BigInteger {
        val monkeys = getMonkeys(input)
        doRounds(monkeys, 10_000, 1)

        monkeys.map { it.inspected }.sortedDescending().take(2)
            .also { return it.first().toBigInteger().times(it.last().toBigInteger()) }
    }

    private fun getMonkeys(input: List<String>): List<Monkey> {
        return input.chunked(7).map {
            Monkey(
                it[1].split(": ").last(),
                it[2].split("= ").last(),
                it[3].split("by ").last().toInt(),
                it[4].last().digitToInt(),
                it[5].last().digitToInt()
            )
        }
    }

    private fun doRounds(monkeys: List<Monkey>, amount: Int, worryDivider: Int) {
        val gcd = monkeys.map { it.test }.fold(1) { it, acc -> it * acc }

        repeat(amount) {
            for (monkey in monkeys) {
                while (monkey.items.isNotEmpty()) {
                    val item = monkey.items.removeFirst()
                    val newItem = monkey.op(item, gcd) / worryDivider
                    val toMonkey = monkey.inspect(newItem)
                    monkeys[toMonkey].items.add(newItem)
                }
            }
        }
    }

}

private fun main() = Solution().run()