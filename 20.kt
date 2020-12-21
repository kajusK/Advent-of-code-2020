import java.io.File
import kotlin.math.sqrt

class Tile(val id: Long, var block: List<List<Char>>, var orientation: Int = 0) {
    val top: String
        get() = getBorder(0)

    val right: String
        get() = getBorder(1)

    val bottom: String
        get() = getBorder(2)

    val left: String
        get() = getBorder(3)

    val border by lazy {
        val top = block.first().joinToString("")
        val bottom = block.last().joinToString("")
        val left = block.map { it.first() }.joinToString("")
        val right = block.map { it.last() }.joinToString("")
        listOf(top, right, bottom, left,
            // flipped left to right
            top.reversed(), left, bottom.reversed(), right,
                // flipped to to bottom
            bottom, right.reversed(), top, left.reversed())
    }

    private fun getBorder(dir: Int): String = border[(dir + orientation) % border.size]

    fun isValidLocation(pos: Pair<Int, Int>, image: Map<Pair<Int, Int>, Tile>): Boolean {
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

    // Test if any of the orientations of self can be matched with any orientation or the tile
    fun isNeighbor(tile: Tile): Boolean {
        return border.any { it in tile.border }
    }

    override fun equals(other: Any?): Boolean = (other is Tile) && id == other.id
    fun copy(): Tile = Tile(id, block, orientation)
}

// find corner tiles
fun findCorners(tiles: List<Tile>): List<Tile> {
    return tiles.map { tile ->
        tile to tiles.count { test ->
            tile != test && tile.isNeighbor(test)
        }
    }.filter { (_, cnt) -> cnt == 2 }.map { (tile, _) -> tile }
}

fun main() {
    val tiles = File("inputs/20.txt").readText().split("\n\n").map { tile ->
        val spl = tile.trim().split('\n')
        val id = spl[0].removeSurrounding("Tile ", ":").toLong()
        val block = spl.subList(1, spl.size).map { it.toList() }
        Tile(id, block)
    }

    var map: Map<Pair<Int, Int>, Tile>? = null

    println(tiles.first().border)

    val corners = findCorners(tiles).map { it.id }.reduce { acc, l -> acc * l }
    println("Part 1: $corners")
}