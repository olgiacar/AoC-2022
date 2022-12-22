package `21`

import SolutionInterface
import isNumeric
import java.math.BigInteger

abstract class Tree(val name: String) {
    abstract fun eval(): BigInteger

    abstract fun contains(name: String): Boolean
}

class Node(name: String, val left: Tree, val right: Tree, val operation: String) : Tree(name) {

    override fun eval(): BigInteger = when (operation) {
        "+" -> left.eval().plus(right.eval())
        "-" -> left.eval().minus(right.eval())
        "*" -> left.eval().times(right.eval())
        else -> left.eval().div(right.eval())
    }

    override fun contains(name: String) = this.name == name || left.contains(name) || right.contains(name)

}

class Leaf(name: String, private val value: BigInteger) : Tree(name) {
    override fun eval() = value

    override fun contains(name: String) = this.name == name
}

class Solution : SolutionInterface(testSolutionOne = 152, testSolutionTwo = 301) {
    private val inverseOperation = mapOf("+" to "-", "-" to "+", "*" to "/", "/" to "*")

    override fun exerciseOne(input: List<String>) = getTree(input).eval()

    override fun exerciseTwo(input: List<String>): BigInteger {
        val tree = getTree(input)
        if ((tree as Node).left.contains("humn"))
            return solve("humn", tree.left, tree.right)
        return solve("humn", tree.right, tree.left)
    }

    private fun getTree(input: List<String>) = getNode("root", getCommands(input))

    private fun getCommands(input: List<String>): Map<String, String> {
        return input.map { it.split(": ") }.associate { it.first() to it.last() }
    }

    private fun getNode(root: String, commands: Map<String, String>): Tree {
        val current = commands[root]!!
        if (current.isNumeric()) return Leaf(root, current.toBigInteger())
        val (left, op, right) = current.split(" ")
        return Node(root, getNode(left, commands), getNode(right, commands), op)
    }

    private fun solve(by: String, has: Tree, hasNot: Tree): BigInteger {
        if (has !is Node) return hasNot.eval()
        if (has.left.contains(by))
            return solve(by, has.left, Node(has.right.name, hasNot, has.right, inverseOperation[has.operation]!!))
        if (has.operation == "-" || has.operation == "/")
            return solve(by, has.right, Node(has.left.name, has.left, hasNot, has.operation))
        return solve(by, has.right, Node(has.left.name, hasNot, has.left, inverseOperation[has.operation]!!))
    }

}

private fun main() = Solution().run()