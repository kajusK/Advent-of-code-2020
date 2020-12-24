import java.io.File

fun getInitial(tiles: List<String>): List<Pair<Int, Double>> {
    var flipped = mutableListOf<Pair<Int, Double>>()

    tiles.map {tile ->
        var x = 0
        var y = 0.toDouble()

        tile.forEach { dir ->
            when (dir) {
                'e' -> y++
                'w' -> y--
                'S' -> { y += 0.5; x++ }
                's' -> { y -= 0.5; x++ }
                'N' -> { y += 0.5; x-- }
                'n' -> { y -= 0.5; x-- }
            }
        }
        val pos = x to y
        if (pos in flipped) {
            flipped.remove(pos)
        } else {
            flipped.add(pos)
        }
    }
    return flipped
}

fun countNeighborsBlack(pos: Pair<Int, Double>, black: List<Pair<Int, Double>>): Int {
    val dirs = getNeighbors(pos)
    return dirs.filter { it in black }.size
}

fun getNeighbors(pos: Pair<Int, Double>): List<Pair<Int, Double>> {
    val dirs = listOf(
            -1 to 0.5,
            -1 to -0.5,
            0 to 1.0,
            0 to -1.0,
            1 to 0.5,
            1 to -0.5
    )
    return dirs.map { pos.first + it.first to  pos.second + it.second }
}

fun main() {
    val input = File("inputs/24.txt").readLines().map { line ->
        line.replace("se", "S")
                .replace("sw", "s")
                .replace("ne", "N")
                .replace("nw", "n")
    }

    var tiles = getInitial(input)
    println("Part 1: ${tiles.size}")

    (0 until 100).forEach { _ ->
        val new = mutableListOf<Pair<Int, Double>>()
        val toTest = tiles.map { getNeighbors(it) + listOf(it) }.flatten().toSet()

        toTest.forEach { tile ->
            val neighbors = countNeighborsBlack(tile, tiles)

            if (neighbors == 2 && tile !in tiles) {
                new.add(tile)
            } else if (tile in tiles && neighbors in 1..2) {
                new.add(tile)
            }
        }
        tiles = new
    }

    println("Part 2: ${tiles.size}")
}