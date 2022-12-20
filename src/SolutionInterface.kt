import java.io.File

abstract class SolutionInterface(
    private val testSolutionOne: Any,
    private val testSolutionTwo: Any
) {
    private val inputPath = "${javaClass.packageName}/.input"
    private val testPath = "${javaClass.packageName}/.test"

    abstract fun exerciseOne(input: List<String>): Any
    abstract fun exerciseTwo(input: List<String>): Any

    fun run() {
        runOne()
        runTwo()
    }

    private fun runOne() {
        val testOne = exerciseOne(readLines(testPath))
        println("Expected: $testSolutionOne, got: $testOne")
        check(testOne.toString() == testSolutionOne.toString())
        val solutionOne = exerciseOne(readLines(inputPath))
        println("Exercise one: $solutionOne")
    }

    private fun runTwo() {
        val testTwo = exerciseTwo(readLines(testPath))
        println("Expected: $testSolutionTwo, got: $testTwo")
        check(testTwo.toString() == testSolutionTwo.toString())
        val solutionTwo = exerciseTwo(readLines(inputPath))
        println("Exercise one: $solutionTwo")
    }

    private fun readLines(fileName: String) = File("src", fileName).readLines()

}