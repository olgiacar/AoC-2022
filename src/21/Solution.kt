package `21`

import SolutionInterface
import isNumeric
import java.math.BigInteger

abstract class Tree(val name: String) {
    abstract fun eval(): BigInteger

    abstract fun has(name: String): Boolean
}

class Node(name: String, val left: Tree, val right: Tree, val operation: String) : Tree(name) {

    override fun eval(): BigInteger = when (operation) {
        "+" -> left.eval().add(right.eval())
        "-" -> left.eval().subtract(right.eval())
        "*" -> left.eval().multiply(right.eval())
        else -> left.eval().divide(right.eval())
    }

    override fun has(name: String) = this.name == name || left.has(name) || right.has(name)

}

class Leaf(name: String, private val value: BigInteger) : Tree(name) {
    override fun eval() = value

    override fun has(name: String) = this.name == name
}

class Solution : SolutionInterface(testSolutionOne = 152, testSolutionTwo = 301) {
    private val inverseOperation = mapOf("+" to "-", "-" to "+", "*" to "/", "/" to "*")

    override fun exerciseOne(input: List<String>) = getTree(input).eval()

    override fun exerciseTwo(input: List<String>): BigInteger {
        getTree(input).also { tree ->
            if ((tree as Node).left.has("humn"))
                return solve("humn", tree.left, tree.right).eval()
            return solve("humn", tree.right, tree.left).eval()
        }
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

    private fun solve(by: String, has: Tree, hasNot: Tree): Tree {
        if (has !is Node) return hasNot
        if (has.left.has(by))
            return solve(by, has.left, Node(has.right.name, hasNot, has.right, inverseOperation[has.operation]!!))
        if (has.operation == "-" || has.operation == "/")
            return solve(by, has.right, Node(has.left.name, has.left, hasNot, has.operation))
        return solve(by, has.right, Node(has.left.name, hasNot, has.left, inverseOperation[has.operation]!!))
    }

}

private fun main() = Solution().run()