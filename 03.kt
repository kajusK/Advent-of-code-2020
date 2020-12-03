import java.io.File

fun getTrees(grid: List<List<Boolean>>, shiftX: Int, shiftY: Int): Int {
    var x = 0
    var y = 0
    var trees = 0

    while (y < grid.count()) {
        if (grid[y][x]) {
            trees++
        }
        x += shiftX
        y += shiftY
        if (x >= grid[0].count()) {
            x -= grid[0].count()
        }
    }
    return trees
}

fun main(args: Array<String>) {
    val grid = File("inputs/03.txt").readLines().map { it.map { it == '#' } }
    println("Part 1: ${getTrees(grid, 3, 1)}")

    val dirs = listOf(Pair(1, 1), Pair(3, 1), Pair(5, 1), Pair(7, 1), Pair(1, 2))
    val res = dirs.map { (a, b) -> getTrees(grid, a, b).toLong() } .reduce { accu, el -> accu * el }
    println("Part 2: $res")
}