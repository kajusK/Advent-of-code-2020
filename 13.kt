import java.io.File

fun inverse(num: Long, modulo: Long) : Long {
    var x = 2L
    val a = num % modulo

    while (true) {
        if ((x * a) % modulo == 1L) {
            return x % modulo
        }
        x++
    }
}

fun main() {
    val content = File("inputs/13.txt").readLines()
    val start = content[0].toInt()
    val departures = content[1].split(',').mapIndexed { i, id ->
        id to i
    }.filterNot { it.first == "x" }.map { (id, off) ->
        id.toLong() to off
    }

    val soonest = departures.map { it.first }.map { it to it - start % it }.minBy { it.second }!!
    println("Part 1: ${soonest.first * soonest.second}")


    /* https://en.wikipedia.org/wiki/Chinese_remainder_theorem - many thanks to Antonin Haas who found this */
    /*
     * The each ID is assigned an offset based on it's position (from 0)
     * The equation for each id is: (t+off) mod id = 0
     * This can be simplified to: t mod id = - (off mod id) = id - (off mod id)
     */
    val offsets = departures.map { (id, off) -> id to id-(off % id) }
    val Z = offsets.map { it.first }.reduce { acc, id -> acc * id }

    /* Algorithm is taken from http://voho.eu/wiki/cinska-veta-o-zbytcich/ */
    val res = offsets.map { (id, off) ->
        /* Calculate q */
        val s = offsets.map { it.first }.filter { it != id }.reduce { acc, i -> acc*i }
        val t = inverse(s, id)
        off to ((s*t) % Z)
    }.map { (off, q) -> off*q }.sum() % Z

    println("Part 2: $res")
}