package `20`

import SolutionInterface
import java.math.BigInteger

private val DECRYPTION_KEY = BigInteger.valueOf(811589153)

class RefBigNumber(value: BigInteger) : RefNumber(value.times(DECRYPTION_KEY))

open class RefNumber(val value: BigInteger)

class Solution : SolutionInterface(testSolutionOne = 3, testSolutionTwo = 1623178306) {


    override fun exerciseOne(input: List<String>): Int {
        input.map { BigInteger.valueOf(it.toLong()) }.map { RefNumber(it) }.also {
            return getResult(it, 1)
        }
    }

    override fun exerciseTwo(input: List<String>): Int {
        input.map { BigInteger.valueOf(it.toLong()) }.map { RefBigNumber(it) }.also {
            return getResult(it, 10)
        }
    }

    private fun getResult(numbers: List<RefNumber>, rounds: Int): Int {
        val shuffled = shuffle(numbers, rounds)
        val startIndex = shuffled.indexOfFirst { it.value == BigInteger.ZERO }
        val values = (1..3).map { shuffled[(startIndex + 1000 * it) % shuffled.size].value }
        return values.fold(BigInteger.ZERO) { acc, it -> acc.plus(it) }
            .also { println(it) }.toInt()
    }

    private fun shuffle(numbers: List<RefNumber>, rounds: Int): List<RefNumber> {
        val shuffled = mutableListOf<RefNumber>().apply { addAll(numbers) }

        repeat(rounds) {
            for (number in numbers) {
                val index = shuffled.indexOf(number).also { shuffled.removeAt(it) }

                BigInteger.valueOf(index.toLong())
                    .plus(number.value.mod(BigInteger.valueOf(numbers.size.toLong() - 1)))
                    .mod(BigInteger.valueOf(numbers.size.toLong() - 1))
                    .also { shuffled.add(it.toInt(), number) }
            }
        }

        return shuffled
    }
}

private fun main() = Solution().run()