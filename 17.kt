import java.io.File

const val ACTIVE = '#'

fun genDirections(dimension: Int): List<List<Int>> {
    if (dimension == 1) {
        return (-1..1).map { listOf(it) }
    }
    val res = mutableListOf<List<Int>>()
    (-1..1).forEach { i ->
        res += genDirections(dimension - 1).map { it + listOf(i)}
    }
    return res
}

/*
 * Get positions around the current one that are empty
 */
fun getNeighbours(
        pos: List<Int>,
        map: List<List<Int>>,
        dirs: List<List<Int>>): List<List<Int>> {

    var around = mutableListOf<List<Int>>()
    dirs.forEach { dir ->
        val newPos = dir.mapIndexed { i, m -> pos[i] + m }
        if (newPos !in map) {
            around.add(newPos)
        }
    }
    return around
}

fun getState(
        pos: List<Int>,
        map: List<List<Int>>,
        dirs: List<List<Int>>): Boolean {
    var neighbors = 0
    val positions = mutableListOf<MutableList<Int>>()

    dirs.forEach { dir ->
        val search = dir.mapIndexed { i, m -> pos[i] + m }
        if (search in map) {
            neighbors += 1
        }
    }

    return if (pos in map) {
        neighbors in 2..3
    } else {
        neighbors == 3
    }
}

fun solve(map: List<List<Int>>): Int {
    val dirs = genDirections(map[0].count()).filterNot { line -> line.all { it == 0 } }
    var cur = map

    (1..6).forEach {
        val new = mutableListOf<List<Int>>()
        val search = cur.toMutableList()
        cur.forEach { pos ->
            search += getNeighbours(pos, search, dirs)
        }

        search.forEach { pos ->
            if (getState(pos, cur, dirs)) {
                new.add(pos)
            }
        }
        cur = new
    }
    return cur.count()
}

fun main() {
    var map = File("inputs/17.txt").readLines().mapIndexed { i, line ->
        line.mapIndexed { j, v -> listOf(i, j, 0, 0) to (v == ACTIVE) }.filter { it.second }.map { it.first }
    }.flatten()

    println("Part 1: ${solve(map.map { it.subList(0, 3) })}")
    println("Part 2: ${solve(map)}")
}