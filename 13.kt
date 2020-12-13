import java.io.File

fun inverse(num: Long, modulo: Int) : Long {
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
    val departures = content[1].split(',').map {
        if (it == "x") 0 else it.toInt()
    }

    val soonest = departures.filter { it != 0 }. map { it to it - start % it }.minBy { it.second }!!
    println("Part 1: ${soonest.first * soonest.second}")


    /* https://en.wikipedia.org/wiki/Chinese_remainder_theorem - many thanks to Antonin Haas who found this */
    /*
     * The each ID is assigned an offset based on it's position (from 0)
     * The equation for each id is: (t+off) mod id = 0
     * This can be simplified to: t mod id = - (off mod id) = id - (off mod id)
     */
    val offsets = departures.mapIndexed { i, id -> if (id != 0) id to id-(i % id) else 0 to 0 }.filter { it.first != 0 }
    val Z = offsets.fold(1L) { acc, (id, off) -> acc * id }

    /* Algorithm is taken from http://voho.eu/wiki/cinska-veta-o-zbytcich/ */
    val res = offsets.map { (id, off) ->
        /* Calculate q */
        val s = offsets.filter { it.first != id }.fold(1L) { acc, i -> acc*i.first }
        val t = inverse(s, id)
        off to ((s*t) % Z)
    }.map { (off, q) -> off*q }.sum() % Z

    println("Part 2: $res")
}