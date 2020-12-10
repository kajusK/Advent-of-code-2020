import java.io.File

//
fun nextValid(first: Int, data: List<Int>, max: Int): Long {
    val variants = data.filter { it > first && it - first <= 3 }
    var sum = 0L
    if (variants.count() == 0) {
        if (first + 3 >= max) {
            return 1
        }
        return 0
    }

    variants.forEach { num ->
        sum += nextValid(num, data, max)
    }
    return sum
}

fun getCombinations(ones: Int): Long {
    //too lazy to think about better method now, using the ineffective function implemented in first try
    return nextValid(0, (1..ones).toList(), ones+3)
}

fun main() {
    val adapters = File("inputs/10.txt").readLines().map { it.toInt() }.sorted()
    val distances = adapters.mapIndexed { i, it ->
        if (i == 0) {
            it
        } else {
            it - adapters[i-1]
        }
    }

    println("Part 1: ${distances.filter { it == 1 }.count()*(distances.filter { it == 3 }.count() + 1)}")
    // First very ineffective solution, would take ages to complete on bigger sets of numbers
    //println("Part 2: ${nextValid(0, adapters, adapters.max()!! + 3)}")

    // parts with multiple 3 in row and sequences 313 cannot be connected in any other way, leading and trailing 3
    // won't also affect the result
    val sectors = distances.joinToString("").trim('3').split(Regex("3+")).map {
        it.length
    }
    val combinations = sectors.toSet().associateWith { getCombinations(it) }
    val res = sectors.map { combinations[it]!! }.fold(1L) { acc, it -> acc * it }
    println("Part2: $res")
}