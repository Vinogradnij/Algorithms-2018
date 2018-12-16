@file:Suppress("UNUSED_PARAMETER")

package lesson6

import java.io.File

/**
 * Наибольшая общая подпоследовательность.
 * Средняя
 *
 * Дано две строки, например "nematode knowledge" и "empty bottle".
 * Найти их самую длинную общую подпоследовательность -- в примере это "emt ole".
 * Подпоследовательность отличается от подстроки тем, что её символы не обязаны идти подряд
 * (но по-прежнему должны быть расположены в исходной строке в том же порядке).
 * Если общей подпоследовательности нет, вернуть пустую строку.
 * При сравнении подстрок, регистр символов *имеет* значение.
 */
fun longestCommonSubSequence(first: String, second: String): String {
    TODO()
}

/**
 * Наибольшая возрастающая подпоследовательность
 * Средняя
 *
 * Дан список целых чисел, например, [2 8 5 9 12 6].
 * Найти в нём самую длинную возрастающую подпоследовательность.
 * Элементы подпоследовательности не обязаны идти подряд,
 * но должны быть расположены в исходном списке в том же порядке.
 * Если самых длинных возрастающих подпоследовательностей несколько (как в примере),
 * то вернуть ту, в которой числа расположены раньше (приоритет имеют первые числа).
 * В примере ответами являются 2, 8, 9, 12 или 2, 5, 9, 12 -- выбираем первую из них.
 */
//Ресурсоемкость O(n)
//Трудоемкость O(n*logN)

fun findLength(minElementInLength: MutableList<Int>, startLeft: Int, startRight: Int, key: Int, maxIndex: Int,
               maxInt: Int): Int {
    var left = startLeft
    var right = startRight
    var middle = 0
    var wasFounded = false
    var index = 0

    while (left <= right && !wasFounded) {
        middle = (left + right) / 2
        when {
            minElementInLength[middle] > key -> {
                right = middle - 1
            }
            minElementInLength[middle] == key -> {
                if (key == maxInt) index = maxIndex + 1
                else index = middle
                wasFounded = true
            }
            minElementInLength[middle + 1] >= key && middle + 1 <= right -> {
                minElementInLength[middle + 1] = key
                index = middle + 1
                wasFounded = true
            }
            else -> {
                left = middle + 1
            }
        }
    }

    if (!wasFounded) {
        if (middle == left) {
            minElementInLength[middle] = key
            index = middle
        } else {
            minElementInLength[middle + 1] = key
            index = middle + 1
        }
    }
    return index
}


fun longestIncreasingSubSequence(list: List<Int>): List<Int> {
    if (list.size <= 1) return list
    val result = mutableListOf<Int>()
    val indexes = mutableListOf<Int>()
    val minElementInLength = mutableListOf<Int>()
    val maxInt = list.max()!!

    for (i in 0 until list.size) {
        indexes.add(i, maxInt)
        minElementInLength.add(i, maxInt)
    }

    indexes[0] = 0
    minElementInLength[0] = list[0]

    var maxIndex = 0
    for (i in 1 until list.size) {
        indexes[i] = findLength(minElementInLength, 0, i, list[i], maxIndex, maxInt)
        if (indexes[i] > maxIndex) maxIndex = indexes[i]
    }

    var index = maxIndex + 1
    var previousValue = 0
    for (i in list.size - 1 downTo 0) {
        when {
            indexes[i] == index - 1 -> {
                if (index != maxIndex + 1) previousValue = result[0]
                result.add(0, list[i])
                index--
            }
            indexes[i] == maxIndex -> {
                result.clear()
                result.add(0, list[i])
                index = maxIndex
            }
            indexes[i] == index && list[i] < previousValue -> {
                result[0] = list[i]
            }
        }
    }
    return result
}

/**
 * Самый короткий маршрут на прямоугольном поле.
 * Сложная
 *
 * В файле с именем inputName задано прямоугольное поле:
 *
 * 0 2 3 2 4 1
 * 1 5 3 4 6 2
 * 2 6 2 5 1 3
 * 1 4 3 2 6 2
 * 4 2 3 1 5 0
 *
 * Можно совершать шаги длиной в одну клетку вправо, вниз или по диагонали вправо-вниз.
 * В каждой клетке записано некоторое натуральное число или нуль.
 * Необходимо попасть из верхней левой клетки в правую нижнюю.
 * Вес маршрута вычисляется как сумма чисел со всех посещенных клеток.
 * Необходимо найти маршрут с минимальным весом и вернуть этот минимальный вес.
 *
 * Здесь ответ 2 + 3 + 4 + 1 + 2 = 12
 */
//Ресурсоемкость O(height*width)
//Трудоемкость O(height*width)

fun shortestPathOnField(inputName: String): Int {
    val list = mutableListOf<List<String>>()
    File(inputName).forEachLine { list.add(it.split(Regex("\\s"))) }
    val height = list.size
    val width = list[0].size
    val field = Array(height) {IntArray(width)}

    field[0][0] = list[0][0].toInt()

    for (i in 1 until height) {
        field[i][0] = field[i - 1][0] + list[i][0].toInt()
    }
    for (i in 1 until width) {
        field[0][i] = field[0][i - 1] + list[0][i].toInt()
    }

    var min: Int
    for (i in 1 until height) {
        for (j in 1 until width) {
            min = Math.min((Math.min(field[i - 1][j - 1], field[i][j - 1])), field[i - 1][j]) + list[i][j].toInt()
            field[i][j] += min
        }
    }
    return field[height - 1][width - 1]
}

// Задачу "Максимальное независимое множество вершин в графе без циклов"
// смотрите в уроке 5