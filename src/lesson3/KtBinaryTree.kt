package lesson3

import java.util.*
import kotlin.NoSuchElementException
import java.util.Stack



// Attention: comparable supported but comparator is not
class KtBinaryTree<T : Comparable<T>> : AbstractMutableSet<T>(), CheckableSortedSet<T> {

    private var root: Node<T>? = null

    override var size = 0
        private set

    private class Node<T>(val value: T) {

        var left: Node<T>? = null

        var right: Node<T>? = null
    }

    override fun add(element: T): Boolean {
        val closest = find(element)
        val comparison = if (closest == null) -1 else element.compareTo(closest.value)
        if (comparison == 0) {
            return false
        }
        val newNode = Node(element)
        when {
            closest == null -> root = newNode
            comparison < 0 -> {
                assert(closest.left == null)
                closest.left = newNode
            }
            else -> {
                assert(closest.right == null)
                closest.right = newNode
            }
        }
        size++
        return true
    }

    override fun checkInvariant(): Boolean =
            root?.let { checkInvariant(it) } ?: true

    private fun checkInvariant(node: Node<T>): Boolean {
        val left = node.left
        if (left != null && (left.value >= node.value || !checkInvariant(left))) return false
        val right = node.right
        return right == null || right.value > node.value && checkInvariant(right)
    }

    /**
     * Удаление элемента в дереве
     * Средняя
     */

    //Ресурсоемкость O(1)
    //Трудоемкость O(logN), в худшем случае O(N)

    private fun findNodeAndFather(value: T): Pair<Node<T>, Node<T>?> {
        if (!contains(value)) throw IllegalAccessError("Element not found")
        var father: Node<T>? = null
        var node = root!!
        var comparison: Int
        while (node.value != value) {
            father = node
            comparison = value.compareTo(node.value)
            node = if (comparison < 0) node.left!!
            else node.right!!
        }
        return node to father
    }

    private fun removeIfWeHaveChildren(nodeToRemove: Node<T>, father: Node<T>?) {
        var minNodeInRight = nodeToRemove.right!!
        var fatherOfMin = nodeToRemove

        while (minNodeInRight.left != null) {
            fatherOfMin = minNodeInRight
            minNodeInRight = minNodeInRight.left!!
        }

        when {
            nodeToRemove.right != minNodeInRight && minNodeInRight.right != null -> {
                fatherOfMin.left = minNodeInRight.right
                minNodeInRight.right = nodeToRemove.right
            }
            nodeToRemove.right != minNodeInRight && minNodeInRight.right == null -> {
                fatherOfMin.left = null
                minNodeInRight.right = nodeToRemove.right
            }
        }

        minNodeInRight.left = nodeToRemove.left

        if (father != null) {
            if (nodeToRemove == father.left) father.left = minNodeInRight
            else father.right = minNodeInRight
        }
        else {
            root = minNodeInRight
        }
    }

    private fun removeRoot() {
        when {
            root!!.left != null && root!!.right != null-> {
                removeIfWeHaveChildren(root!!, null)
            }
            root!!.left == null && root!!.right != null -> {
                root = root!!.right
            }
            root!!.left != null && root!!.right == null -> {
                root = root!!.left
            }
            root!!.left == null && root!!.right == null -> {
                root = null
            }
        }
    }

    override fun remove(element: T): Boolean {
        val nodeToRemove = findNodeAndFather(element).first
        val father = findNodeAndFather(element).second
        if (father == null) {
            removeRoot()
        }
        else {
            when {
                nodeToRemove.left == null && nodeToRemove.right == null -> {
                    if (nodeToRemove == father.left) father.left = null
                    else father.right = null
                }

                nodeToRemove.left != null && nodeToRemove.right == null -> {
                    if (nodeToRemove == father.left) father.left = nodeToRemove.left
                    else father.right = nodeToRemove.left
                }

                nodeToRemove.left == null && nodeToRemove.right != null -> {
                    if (nodeToRemove == father.left) father.left = nodeToRemove.right
                    else father.right = nodeToRemove.right
                }

                nodeToRemove.left != null && nodeToRemove.right != null -> {
                    removeIfWeHaveChildren(nodeToRemove, father)
                }
            }
        }
        size--
        return true
    }

    override operator fun contains(element: T): Boolean {
        val closest = find(element)
        return closest != null && element.compareTo(closest.value) == 0
    }

    private fun find(value: T): Node<T>? =
            root?.let { find(it, value) }

    private fun find(start: Node<T>, value: T): Node<T> {
        val comparison = value.compareTo(start.value)
        return when {
            comparison == 0 -> start
            comparison < 0 -> start.left?.let { find(it, value) } ?: start
            else -> start.right?.let { find(it, value) } ?: start
        }
    }

    inner class BinaryTreeIterator : MutableIterator<T> {

        private var current: Node<T>? = null
        private val stack = Stack<Node<T>>()
        private var hasNextNode = false

        /**
         * Поиск следующего элемента
         * Средняя
         */

        //Ресурсоемкость O(N)
        //Трудоемкость O(logN), в худшем случае O(N)

        private fun findMinNode(node: Node<T>): Node<T> {
            current = node
            while (current!!.left != null) {
                stack.push(current)
                current = current!!.left
            }
            return current!!
        }

        private fun findNext(): Node<T>? {
            when {
                root == null || (current != null && current!!.value == last()) -> return null
                current == null -> return findMinNode(root!!)
                current!!.right == null -> {
                    current = stack.peek()
                    return stack.pop()
                }
                current!!.right != null -> return findMinNode(current!!.right!!)
            }
            return null
        }

        override fun hasNext(): Boolean {
            hasNextNode = true
            return findNext() != null
        }

        override fun next(): T {
            if (hasNextNode) {
                hasNextNode = false
                return current!!.value
            }
            current = findNext()
            return (current ?: throw NoSuchElementException()).value
        }

        /**
         * Удаление следующего элемента
         * Сложная
         */
        override fun remove() {
            TODO()
        }
    }

    override fun iterator(): MutableIterator<T> = BinaryTreeIterator()

    override fun comparator(): Comparator<in T>? = null

    /**
     * Для этой задачи нет тестов (есть только заготовка subSetTest), но её тоже можно решить и их написать
     * Очень сложная
     */
    override fun subSet(fromElement: T, toElement: T): SortedSet<T> {
        TODO()
    }

    /**
     * Найти множество всех элементов меньше заданного
     * Сложная
     */
    override fun headSet(toElement: T): SortedSet<T> {
        TODO()
    }

    /**
     * Найти множество всех элементов больше или равных заданного
     * Сложная
     */
    override fun tailSet(fromElement: T): SortedSet<T> {
        TODO()
    }

    override fun first(): T {
        var current: Node<T> = root ?: throw NoSuchElementException()
        while (current.left != null) {
            current = current.left!!
        }
        return current.value
    }

    override fun last(): T {
        var current: Node<T> = root ?: throw NoSuchElementException()
        while (current.right != null) {
            current = current.right!!
        }
        return current.value
    }
}
