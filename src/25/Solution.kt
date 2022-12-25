package `25`

import SolutionInterface
import java.math.BigInteger

class Solution : SolutionInterface(testSolutionOne = "2=-1=0", testSolutionTwo = 5) {
    private val base = 5.toBigInteger()
    private val digits = mapOf(
        '2' to BigInteger.TWO,
        '1' to BigInteger.ONE,
        '0' to BigInteger.ZERO,
        '-' to (-1).toBigInteger(),
        '=' to (-2).toBigInteger()
    )

    override fun exerciseOne(input: List<String>): String {
        return toSnafu(input.map { fromSnafu(it) }.fold(BigInteger.ZERO) { acc, it -> acc.add(it) })
    }

    override fun exerciseTwo(input: List<String>): Int {
        return 5
    }

    private fun toSnafu(decimal: BigInteger): String {
        var position = getFirstPosition(decimal)
        var rest = decimal
        var snafu = ""
        do {
            val (digit, newRest) = getNextDigit(rest, position--)
            snafu += digit
            rest = newRest
        } while (position >= 0)
        return snafu
    }

    private fun getNextDigit(rest: BigInteger, position: Int): Pair<Char, BigInteger> {
        val magnitude = base.pow(position)
        if (rest.abs() >= magnitude.multiply(BigInteger.TWO).subtract(magnitude.divide(BigInteger.TWO))) {
            return if (rest > BigInteger.ZERO) '2' to rest.subtract(magnitude.multiply(BigInteger.TWO))
            else '=' to rest.add(magnitude.multiply(BigInteger.TWO))
        }
        if (rest.abs() >= magnitude.subtract(magnitude.divide(BigInteger.TWO))) {
            return if (rest > BigInteger.ZERO) '1' to rest.subtract(magnitude)
            else '-' to rest.add(magnitude)
        }
        return '0' to rest
    }


    private fun getFirstPosition(decimal: BigInteger): Int {
        var exponent = 0
        while (base.pow(exponent).multiply(3.toBigInteger()) < decimal) exponent++
        return exponent
    }

    private fun fromSnafu(snafu: String): BigInteger {
        var decimal = BigInteger.ZERO
        snafu.forEach {
            decimal = decimal.multiply(5.toBigInteger())
            decimal = decimal.add(digits[it]!!)
        }
        return decimal
    }

}

private fun main() = Solution().run()