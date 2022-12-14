import java.math.BigInteger
import java.security.MessageDigest

/**
 * Converts string to md5 hash.
 */
fun String.md5(): String = BigInteger(1, MessageDigest.getInstance("MD5").digest(toByteArray())).toString(16)

fun String.isNumeric() = this.toIntOrNull() != null

/**
 * Source: https://stackoverflow.com/questions/9562605/in-kotlin-can-i-create-a-range-that-counts-backwards
 */
infix fun Int.toward(to: Int): IntProgression {
    val step = if (this > to) -1 else 1
    return IntProgression.fromClosedRange(this, to, step)
}