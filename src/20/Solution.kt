package `20`

import SolutionInterface
import java.math.BigInteger

private val decryptionKey = BigInteger.valueOf(811589153)

open class RefNumber(val value: BigInteger)
class RefBigNumber(value: BigInteger) : RefNumber(value.times(decryptionKey))

class Solution : SolutionInterface(testSolutionOne = 3, testSolutionTwo = 1623178306) {

    override fun exerciseOne(input: List<String>): BigInteger {
        input.map { BigInteger.valueOf(it.toLong()) }.map { RefNumber(it) }
            .also { return getResult(it, 1) }
    }

    override fun exerciseTwo(input: List<String>): BigInteger {
        input.map { BigInteger.valueOf(it.toLong()) }.map { RefBigNumber(it) }
            .also { return getResult(it, 10) }
    }

    private fun getResult(numbers: List<RefNumber>, rounds: Int): BigInteger {
        val shuffled = shuffle(numbers, rounds)
        val startIndex = shuffled.indexOfFirst { it.value == BigInteger.ZERO }
        (1..3).map { shuffled[(startIndex + 1000 * it) % shuffled.size].value }
            .also { values -> return values.fold(BigInteger.ZERO) { acc, it -> acc.plus(it) } }
    }

    private fun shuffle(numbers: List<RefNumber>, rounds: Int): List<RefNumber> {
        val shuffled = mutableListOf<RefNumber>().apply { addAll(numbers) }
        val modValue = BigInteger.valueOf(numbers.size.toLong() - 1)

        repeat(rounds) {
            numbers.forEach {
                val index = shuffled.indexOf(it).also { index -> shuffled.removeAt(index) }

                BigInteger.valueOf(index.toLong()).plus(it.value.mod(modValue)).mod(modValue)
                    .also { i -> shuffled.add(i.toInt(), it) }
            }
        }

        return shuffled
    }

}

private fun main() = Solution().run()