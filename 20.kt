import java.io.File

open class Block(var block: List<List<Char>>) {
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
}

class Tile(val id: Long, block: List<List<Char>>) : Block(block) {
    val top: String
        get() = block.first().joinToString("")

    val right: String
        get() = block.map { it.last() }.joinToString("")

    val bottom: String
        get() = block.last().joinToString("")

    val left: String
        get() = block.map { it.first() }.joinToString("")


    fun isValidOrientation(pos: Pair<Int, Int>, image: Map<Pair<Int, Int>, Tile>): Boolean {
        val dirs = listOf(-1 to 0, 1 to 0, 0 to -1, 0 to 1)
        dirs.forEach { dir ->
            val neighborPos = pos.first + dir.first to pos.second + dir.second
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
        hflip()
        if (searchValidRotation(pos, image)) return true
        return false
    }

    fun getUnbordered(): List<List<Char>> {
        return block.subList(1, block.size - 1).map { it.subList(1, it.size - 1) }
    }
}

class Image(block: List<List<Char>>) : Block(block) {
    private fun countMonstersSingle(monster: List<List<Char>>) : Int {
        var monsters = 0

        for (y in 0..(block.size - monster.size)) {
            for (x in 0..(block[0].size-monster[0].size)) {
                val present = monster.mapIndexed { my, line ->
                    line.mapIndexed { mx, c -> c == '.' || c == block[y + my][x + mx] }.all { it }
                }.all { it }
                if (present) monsters++
            }
        }
        return monsters
    }

    private fun countMonstersRotated(monster: List<List<Char>>) : Int {
        var monsters = countMonstersSingle(monster)
        (0..2).forEach {
            rotate()
            monsters += countMonstersSingle(monster)
        }
        return monsters
    }

    fun countMonsters(monster: List<List<Char>>) : Int {
        var monsters = countMonstersRotated(monster)
        vflip()
        monsters += countMonstersRotated(monster)
        vflip()
        hflip()
        monsters += countMonstersRotated(monster)
        return monsters
    }
}

fun genMap(tiles: List<Tile>): Map<Pair<Int, Int>, Tile> {
    val dirs = listOf(-1 to 0, 1 to 0, 0 to 1, 0 to -1)
    val map = mutableMapOf((0 to 0) to tiles.first())
    val remaining = tiles.toMutableList()
    remaining.remove(map[0 to 0])
    val positions = dirs.toMutableList()

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

fun getRoughness(block: List<List<Char>>) : Int {
    return block.map { it.filter { it == '#' }.size }.sum()
}

fun main() {
    val tiles = File("inputs/20.txt").readText().split("\n\n").map { tile ->
        val spl = tile.trim().split('\n')
        val id = spl[0].removeSurrounding("Tile ", ":").toLong()
        val block = spl.subList(1, spl.size).map { it.toList() }
        Tile(id, block)
    }
    val monster = File("inputs/20_monster.txt").readLines().map { it.toList() }

    val map = genMap(tiles)
    var positions = map.map { (key, _) -> key }.sortedBy { it.second*100 + it.first }
    val edges = listOf(positions.first(), positions.last(),
            positions.first().first to positions.last().second,
            positions.last().first to positions.first().second)
    val part1 = edges.map { map[it]!!.id }.reduce { acc, l -> acc * l }
    println("Part1: $part1")

    val startX = positions.first().first
    val endX = positions.last().first
    val startY = positions.first().second
    val endY = positions.last().second
    val block = mutableListOf<List<Char>>()

    (startY..endY).reversed().forEach { y ->
        val row = (startX..endX).map { x->
            map[x to y]!!.getUnbordered() }.reduce { acc, l ->
            acc.mapIndexed { i, v -> v + l[i] }
        }
        block.addAll(row)
    }
    val image = Image(block)
    val monsters = image.countMonsters(monster)
    println("Part2: ${getRoughness(image.block) - getRoughness(monster)*monsters}")
}