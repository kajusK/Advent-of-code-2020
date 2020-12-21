import java.io.File
import kotlin.math.sqrt

class Tile(val id: Long, var block: List<List<Char>>) {
    var neighbors = listOf<Tile>()

    val top: String
        get() = block.first().joinToString("")

    val right: String
        get() = block.map { it.last() }.joinToString("")

    val bottom: String
        get() = block.last().joinToString("")

    val left: String
        get() = block.map { it.first() }.joinToString("")

    fun rotate() {
        block = block.mapIndexed { i, _ ->
            block.map { row -> row.filterIndexed { j, _ -> j == i }}.flatten().reversed()
        }
    }

    fun vflip() {
        block = block.map { it.reversed() }
    }

    fun hflip() {
        block = block.reversed()
    }

    fun isValidOrientation(pos: Pair<Int, Int>, image: Map<Pair<Int, Int>, Tile>): Boolean {
        val dirs = listOf(-1 to 0, 1 to 0, 0 to -1, 0 to 1)
        dirs.forEach { dir ->
            val neighborPos = pos.first + dir.first to pos.second + pos.second
            image[neighborPos]?.let {
                when (dir) {
                    -1 to 0 -> if (left != it.right) return false
                    1 to 0 -> if (right != it.left) return false
                    0 to 1 -> if (top != it.bottom) return false
                    0 to -1 -> if (bottom != it.top) return false
                    else -> error("Invalid direction")
                }
            }
        }
        return true
    }

    fun searchValidRotation(pos: Pair<Int, Int>, image: Map<Pair<Int, Int>, Tile>): Boolean {
        if (isValidOrientation(pos, image)) return true
        (0..2).forEach {
            rotate()
            if (isValidOrientation(pos, image)) return true
        }
        return false
    }

    fun isValidLocation(pos: Pair<Int, Int>, image: Map<Pair<Int, Int>, Tile>): Boolean {
        if (searchValidRotation(pos, image)) return true
        vflip()
        if (searchValidRotation(pos, image)) return true
        vflip()
        hflip()
        if (searchValidRotation(pos, image)) return true
        return false
    }

    override fun equals(other: Any?): Boolean = (other is Tile) && id == other.id
}

fun genMap(tiles: List<Tile>): Map<Pair<Int, Int>, Tile> {
    val dirs = listOf(-1 to 0, 1 to 0, 0 to 1, 0 to -1)
    val map = mutableMapOf((0 to 0) to tiles.first())
    val remaining = tiles.toMutableList()
    remaining.remove(map[0 to 0])
    val positions = dirs.toMutableList()
    val repeated = mutableListOf<Pair<Int, Int>>()

    while (positions.isNotEmpty()) {
        val pos = positions.first()
        positions.remove(pos)

        val options = remaining.filter { it.isValidLocation(pos, map) }
        if (options.isEmpty()) {
            // found edge
            continue
        }
        if (options.size > 1) {
            // unable to decide now, put at the end of the search list
            positions.add(pos)
            if (pos in repeated) {
                error("Infinite loop")
            }
            repeated.add(pos)
            continue
        }
        map[pos] = options[0]
        remaining.remove(options[0])
        dirs.forEach { (x, y) ->
            val newPos = pos.first + x to pos.second + y
            if (newPos !in positions && newPos !in map) positions.add(newPos)
        }
    }

    return map
}

fun main() {
    val tiles = File("inputs/20.txt").readText().split("\n\n").map { tile ->
        val spl = tile.trim().split('\n')
        val id = spl[0].removeSurrounding("Tile ", ":").toLong()
        val block = spl.subList(1, spl.size).map { it.toList() }
        Tile(id, block)
    }

    val map = genMap(tiles)
    val positions = map.map { (key, _) -> key }.sortedBy { it.first*100 + it.second }
    println(positions)
    val edges = listOf(positions.first(), positions.last(),
            positions.first().first to positions.last().second,
            positions.last().first to positions.first().second)
    println(edges.map { map[it]?.id })
    val part1 = edges.map { map[it]!!.id }.reduce { acc, l -> acc * l }
    println("Part1: $part1")
}