package `7`

import SolutionInterface
import isNumeric

class Directory(
    private val name: String,
    val children: MutableList<Directory> = mutableListOf(),
    val parent: Directory? = null
) {
    val directorySize get(): Int = children.sumOf { it.fileSize } + fileSize
    var fileSize = 0

    fun getChild(n: String) = children.first { it.name == n }

    fun getDirectories(): List<Directory> = mutableListOf(this).apply {
        addAll(children.map { it.getDirectories() }.flatten())
    }
}
class Solution : SolutionInterface(testSolutionOne = 95437, testSolutionTwo = 24933642) {

    override fun exerciseOne(input: List<String>) = buildFileSystem(input).getDirectories()
        .map { it.directorySize }
        .filter { it <= 100_000 }
        .sum()

    override fun exerciseTwo(input: List<String>): Int {
        val root = buildFileSystem(input)

        val needed = 30_000_000 - 70_000_000 + root.directorySize

        return root.getDirectories()
            .map { it.directorySize }
            .filter { it >= needed }
            .min()
    }

    private fun buildFileSystem(input: List<String>): Directory {
        val root = Directory("/")
        var current = root
        input.forEach {
            if (it.startsWith("$ cd")) current = navigate(current, it.substring(5))
            else if (!it.startsWith("$ ls")) add(current, it.split(" "))
        }
        return root
    }

    private fun navigate(current: Directory, name: String): Directory {
        return when (name) {
            ".." -> current.parent!!
            "/" -> current
            else -> current.getChild(name)
        }
    }

    private fun add(current: Directory, tokens: List<String>) {
        if (tokens.first().isNumeric()) current.fileSize += tokens.first().toInt()
        else current.children.add(Directory(tokens.last(), parent = current))
    }
}

private fun main() = Solution().run()