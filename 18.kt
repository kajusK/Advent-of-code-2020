import java.io.File

fun simple(eq: String): Long {
    val items = eq.split(" ")
    var sum = items.first().toLong()

    for (n in 1 until items.count() step 2) {
        sum = when (items[n]) {
            "+" -> sum + items[n+1].toLong()
            "*" -> sum * items[n+1].toLong()
            else -> error("Unknown item")
        }
    }
    return sum
}

fun advanced(input: String): Long {
    var eq = input
    while ('+' in eq) {
        eq = eq.replace(Regex("""[0-9]+ \+ [0-9]+""")) {
            val spl = it.value.split(" + ")
            (spl[0].toLong() + spl[1].toLong()).toString()
        }
    }
    return eq.split(" * ").map { it.toLong() }.reduce { acc, l -> acc * l }
}

fun evaluate(input: String, solver: (String) -> Long): Long {
    var eq = input
    while ('(' in eq) {
        eq = eq.replace(Regex("""\([^\(\)]*\)""")) {
            evaluate(it.value.removeSurrounding("(", ")"), solver).toString()
        }
    }
    return solver(eq)
}

fun main() {
    val input = File("inputs/18.txt").readLines()

    println("Part 1: ${input.map { evaluate(it, ::simple) }.sum()}")
    println("Part 2: ${input.map { evaluate(it, ::advanced) }.sum()}")
}