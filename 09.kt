import java.io.File

fun inPrevious(num: Long, data: List<Long>) : Boolean {
    data.forEach { a ->
        data.forEach { b ->
            if (a + b == num) {
                return true
            }
        }
    }
    return false
}

fun firstNotValid(data: List<Long>, preambleLen: Int) : Long {
    data.subList(preambleLen, data.count()).forEachIndexed() { i, num ->
        if (!inPrevious(num, data.subList(i, i+preambleLen))) {
            return num
        }
    }
    return -1
}

fun continuousSet(data: List<Long>, number: Long) : List<Long> {
    val numPos = data.indexOf(number)
    for (a in 0..(numPos-1)) {
        for (b in 1..(numPos-a)) {
            val list = data.subList(a, a+b)
            if (list.sum() == number) {
                return list
            }
        }
    }
    return listOf()
}

fun main() {
    val preambleLen = 25
    val data = File("inputs/09.txt").readLines().map { it.toLong() }
    val notValid = firstNotValid(data, preambleLen)
    val continuous = continuousSet(data, notValid)

    println("Part1: $notValid")
    println("Part2: ${continuous.min()!! + continuous.max()!!}")
}