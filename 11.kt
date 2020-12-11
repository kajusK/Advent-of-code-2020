import java.io.File

private const val FLOOR = '.'
private const val OCCUPIED = '#'
private const val FREE = 'L'

fun checkDir(pos: Pair<Int, Int>, dir: Pair<Int, Int>, map: List<String>) : Int {
    var (y, x) = pos
    //skip self
    y += dir.first
    x += dir.second

    while (y >= 0 && x >= 0 && y < map.count() && x < map[0].count()) {
        when (map[y][x]) {
            FREE -> return 0
            OCCUPIED -> return 1
        }
        y += dir.first
        x += dir.second
    }
    return 0
}

fun checkSeats(pos: Pair<Int, Int>, map: List<String>) : Int {
    var occupied = 0
    for (y in -1..1) {
        for (x in -1..1) {
            if (y == 0 && x == 0) {
                continue
            }
            occupied += checkDir(pos, y to x, map)
        }
    }
    return occupied
}

fun getNext(pos: Pair<Int, Int>, map: List<String>) : Char {
    val self = map[pos.first][pos.second]
    val occupied = checkSeats(pos, map)

    return when (self) {
        FREE -> if (occupied == 0) OCCUPIED else FREE
        OCCUPIED -> if (occupied >= 5) FREE else OCCUPIED
        else -> self
    }
}

fun getNextClassic(pos: Pair<Int, Int>, map: List<String>) : Char {
    val self = map[pos.first][pos.second]
    val y1 = if (pos.first == 0) 0 else pos.first - 1
    val x1 = if (pos.second == 0) 0 else pos.second - 1
    val y2 = if (pos.first + 1 >= map.lastIndex) map.lastIndex else pos.first + 1
    val x2 = if (pos.second + 1 >= map[0].lastIndex) map[0].lastIndex else pos.second + 1

    val occupied = map.subList(y1, y2+1).map { line ->
        line.subSequence(x1, x2+1).count { it == OCCUPIED }
    }.sum()

    return when (self) {
        FREE -> if (occupied == 0) OCCUPIED else FREE
        OCCUPIED -> if (occupied > 4) FREE else OCCUPIED
        else -> self
    }
}

fun printMap(map: List<String>) {
    map.forEach {
        println(it)
    }
    println()
}


fun run(content: List<String>, getNext: (pos: Pair<Int, Int>, map: List<String>) -> Char): Int {
    var map = content

    var changed = true
    while (changed) {
        changed = false

        var next = MutableList(map.count()) { "" }
        map.forEachIndexed { y, line ->
            var newLine = ""
            line.forEachIndexed { x, item ->
                val chr = getNext(y to x, map)
                newLine += chr
                if (chr != item) {
                    changed = true
                }
            }
            next[y] = newLine
        }
        map = next
    }

    return map.map { line ->
        line.count { it == OCCUPIED}
    }.sum()
}

fun main() {
    var map = File("inputs/11.txt").readLines()

    println("Part 1: ${run(map, ::getNextClassic)}")
    println("Part 2: ${run(map, ::getNext)}")
}