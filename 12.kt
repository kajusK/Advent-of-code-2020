import java.io.File
import kotlin.math.abs

fun moveShip(data: List<Pair<Char, Int>>) : Int {
    var x = 0
    var y = 0
    var angle = 90

    data.forEach { (cmd, arg) ->
        when (cmd) {
            'N' -> y -= arg
            'S' -> y += arg
            'E' -> x += arg
            'W' -> x -= arg
            'L' -> angle -= arg
            'R' -> angle += arg
            'F' -> {
                when (angle) {
                    0 -> y -= arg
                    90 -> x += arg
                    180 -> y += arg
                    270 -> x -= arg
                    else -> error("Invalid angle $angle")
                }
            }
        }
        angle %= 360
        if (angle < 0) {
            angle += 360
        }
    }

    return abs(x) + abs(y)
}

fun waypointRotate(wx: Int, wy: Int, angle: Int) : Pair<Int, Int> {

    return when (if (angle < 0) 360 + angle else angle) {
        90 -> -wy to wx
        180 -> -wx to -wy
        270 -> wy to -wx
        else -> error("Undefined angle $angle")
    }
}

fun moveWaypoint(data: List<Pair<Char, Int>>) : Int {
    var wx = 10
    var wy = -1
    var x = 0
    var y = 0
    var angle = 90

    data.forEach { (cmd, arg) ->
        when (cmd) {
            'N' -> wy -= arg
            'S' -> wy += arg
            'E' -> wx += arg
            'W' -> wx -= arg
            'L' -> {
                val new = waypointRotate(wx, wy, -arg)
                wx = new.first
                wy= new.second
            }
            'R' -> {
                val new = waypointRotate(wx, wy, arg)
                wx = new.first
                wy= new.second
            }
            'F' -> {
                x += arg*wx
                y += arg*wy
            }
        }
    }

    return abs(x) + abs(y)
}


fun main() {
    val data = File("inputs/12.txt").readLines().map {
        val instruction = it[0]
        val argument = it.substring(1, it.length).toInt()
        instruction to argument
    }

    println("Part 1: ${moveShip(data)}")
    println("Part 2: ${moveWaypoint(data)}")
}