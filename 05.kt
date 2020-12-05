import java.io.File

fun searchPos(code: String, upper: Char): Int {
    return code.map { if (it == upper) 1 else 0 }.fold(0) { acc, value -> acc.shl(1) or value}
}

fun findSeat(seats: List<Int>): Int {
    val min = seats.min() ?: return 0
    val max = seats.max() ?: return 0
    return (min..max).toList().filter { it !in seats }[0]
}

fun main() {
    val seats = File("inputs/05.txt").readLines().map {
            searchPos(it.slice(0..6), 'B') to searchPos(it.slice(7..9), 'R')
        }
    val ids = seats.map { it.first*8 + it.second}
    println("Part1: ${ids.max()}")
    println("Part2: ${findSeat(ids)}")
}

