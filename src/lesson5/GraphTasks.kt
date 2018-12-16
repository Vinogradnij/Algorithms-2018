@file:Suppress("UNUSED_PARAMETER", "unused")

package lesson5

/**
 * Эйлеров цикл.
 * Средняя
 *
 * Дан граф (получатель). Найти по нему любой Эйлеров цикл.
 * Если в графе нет Эйлеровых циклов, вернуть пустой список.
 * Соседние дуги в списке-результате должны быть инцидентны друг другу,
 * а первая дуга в списке инцидентна последней.
 * Длина списка, если он не пуст, должна быть равна количеству дуг в графе.
 * Веса дуг никак не учитываются.
 *
 * Пример:
 *
 *      G -- H
 *      |    |
 * A -- B -- C -- D
 * |    |    |    |
 * E    F -- I    |
 * |              |
 * J ------------ K
 *
 * Вариант ответа: A, E, J, K, D, C, H, G, B, C, I, F, B, A
 *
 * Справка: Эйлеров цикл -- это цикл, проходящий через все рёбра
 * связного графа ровно по одному разу
 */
fun Graph.findEulerLoop(): List<Graph.Edge> {
    TODO()
}

/**
 * Минимальное остовное дерево.
 * Средняя
 *
 * Дан граф (получатель). Найти по нему минимальное остовное дерево.
 * Если есть несколько минимальных остовных деревьев с одинаковым числом дуг,
 * вернуть любое из них. Веса дуг не учитывать.
 *
 * Пример:
 *
 *      G -- H
 *      |    |
 * A -- B -- C -- D
 * |    |    |    |
 * E    F -- I    |
 * |              |
 * J ------------ K
 *
 * Ответ:
 *
 *      G    H
 *      |    |
 * A -- B -- C -- D
 * |    |    |
 * E    F    I
 * |
 * J ------------ K
 */
fun Graph.minimumSpanningTree(): Graph {
    TODO()
}

/**
 * Максимальное независимое множество вершин в графе без циклов.
 * Сложная
 *
 * Дан граф без циклов (получатель), например
 *
 *      G -- H -- J
 *      |
 * A -- B -- D
 * |         |
 * C -- F    I
 * |
 * E
 *
 * Найти в нём самое большое независимое множество вершин и вернуть его.
 * Никакая пара вершин в независимом множестве не должна быть связана ребром.
 *
 * Если самых больших множеств несколько, приоритет имеет то из них,
 * в котором вершины расположены раньше во множестве this.vertices (начиная с первых).
 *
 * В данном случае ответ (A, E, F, D, G, J)
 *
 * Эта задача может быть зачтена за пятый и шестой урок одновременно
 */
//Трудоемкость O(n^2)
//Ресурсоемкость O(n)
fun Graph.largestIndependentVertexSet(): Set<Graph.Vertex> {
    val allResults = mutableListOf<Set<Graph.Vertex>>()
    val candidates = mutableSetOf<Graph.Vertex>()
    val exists = mutableSetOf<Graph.Vertex>()

    for (vertex in this.vertices) {
        this.vertices
                .stream()
                .filter { anotherVertex ->
                    !this.getNeighbors(vertex).contains(anotherVertex) && !exists.contains(anotherVertex)
                }
                .forEachOrdered { anotherVertex ->
                    exists.addAll(this.getNeighbors(anotherVertex))
                    candidates.add(anotherVertex)
                }
        allResults.add(candidates)
    }
    var result = setOf<Graph.Vertex>()
    for (allResult in allResults) {
        if (result.size < allResult.size) {
            result = allResult
        }
    }
    return result
}

/**
 * Наидлиннейший простой путь.
 * Сложная
 *
 * Дан граф (получатель). Найти в нём простой путь, включающий максимальное количество рёбер.
 * Простым считается путь, вершины в котором не повторяются.
 * Если таких путей несколько, вернуть любой из них.
 *
 * Пример:
 *
 *      G -- H
 *      |    |
 * A -- B -- C -- D
 * |    |    |    |
 * E    F -- I    |
 * |              |
 * J ------------ K
 *
 * Ответ: A, E, J, K, D, C, H, G, B, F, I
 */
//Трудоемкость O(n)
//Ресурсоемкость O(n)
fun Graph.longestSimplePath(): Path {
    val res = mutableSetOf<Graph.Vertex>()
    find(this, this.get("A")!!, res)
    res.remove(this.get("A")!!)
    return Path(ArrayList<Graph.Vertex>(res), res.size)
}

fun find(graph: Graph, vertex: Graph.Vertex, result: MutableSet<Graph.Vertex>) {
    for (edge in graph.edges) {
        if (edge.begin.name == vertex.name) {
            graph.vertices.remove(edge.begin)
            graph.vertices.remove(edge.end)
            find(graph, edge.end, result)
        }
    }
    result.add(vertex)
}