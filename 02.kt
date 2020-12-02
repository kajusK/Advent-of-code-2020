import java.io.File

fun policy1(lines: List<String>): Int {
    var valid = 0

    for (line in lines) {
        val spl = line.replace(":", "").split("-", " ")
        val count = spl[3].filter { it == spl[2][0] }.count()
        if (count >= spl[0].toInt() && count <= spl[1].toInt()) {
            valid++
        }
    }
    return valid
}

fun policy2(lines: List<String>): Int {
    var valid = 0
    for (line in lines) {
        val spl = line.replace(":", "").split("-", " ")
        val first = spl[3][spl[0].toInt() - 1]
        val second = spl[3][spl[1].toInt() - 1]
        val char = spl[2][0]
        if ((first == char && second != char) || (first != char && second == char)) {
            valid++
        }
    }
    return valid
}

fun main(args: Array<String>) {
    val lines = File("inputs/02.txt").readLines()
    println("Part 1: ${policy1(lines)}")
    println("Part 2: ${policy2(lines)}")
}