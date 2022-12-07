package `7`

import SolutionInterface
import isNumeric

abstract class Dir(open val name: String) {
    open val size get(): Int = 0

    open fun getFolders(): List<Folder> = listOf()
}

class Folder(
    override val name: String,
    val children: MutableList<Dir> = mutableListOf()
    val parent: Folder? = null
) : Dir(name) {
    override val size get(): Int = children.sumOf { it.size }

    fun getChild(n: String) = children.first { it.name == n } as Folder

    override fun getFolders(): List<Folder> = mutableListOf(this).apply {
        addAll(children.map { it.getFolders() }.flatten())
    }
}

class File(override val name: String, override val size: Int) : Dir(name)

class Solution : SolutionInterface(testSolutionOne = 95437, testSolutionTwo = 24933642) {

    override fun exerciseOne(input: List<String>) = buildFileSystem(input).getFolders()
        .map { it.size }
        .filter { it <= 100_000 }
        .sum()

    override fun exerciseTwo(input: List<String>): Int {
        val root = buildFileSystem(input)

        val needed = 30_000_000 - 70_000_000 + root.size

        return root.getFolders()
            .map { it.size }
            .filter { it >= needed }
            .min()
    }

    private fun buildFileSystem(input: List<String>): Folder {
        val root = Folder("/")
        var current = root
        input.forEach {
            if (it.startsWith("$ cd")) current = navigate(current, it.substring(5))
            else if (!it.startsWith("$ ls")) add(current, it.split(" "))
        }
        return root
    }

    private fun navigate(current: Folder, name: String): Folder {
        return when (name) {
            ".." -> current.parent!!
            "/" -> current
            else -> current.getChild(name)
        }
    }

    private fun add(current: Folder, tokens: List<String>) {
        if (tokens.first().isNumeric()) current.children.add(File(tokens.last(), tokens.first().toInt()))
        else current.children.add(Folder(tokens.last(), parent = current))
    }
}

private fun main() = Solution().run()