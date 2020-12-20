import java.io.File

fun checkRules(part: List<String>, rules: Map<Int, List<List<String>>>, input: String): List<String>? {
    if (input.isEmpty()) {
        return null
    }
    if (part.size == 1 && part[0] in "a".."z") {
        return if (input.first() == part[0].first()) listOf(input.substring(1)) else null
    }

    var data = listOf(input)
    part.forEach { item ->
        val rule = rules[item.toInt()]
        val new = mutableListOf<String>()
        rule?.forEach { option ->
            data.forEach {
                checkRules(option, rules, it)?.let { res -> new += res }
            }
        }
        data = new
    }

    return data
}

fun isValid(rules: Map<Int, List<List<String>>>, input: String): Boolean {
    return checkRules(rules[0]!![0]!!, rules, input)?.any { it.isEmpty() } ?: return false
}

fun main() {
    val spl = File("inputs/19.txt").readText().split("\n\n")
    var rules = spl[0].split('\n').map { line ->
        val (id, rule) = line.split(": ")
        id.toInt() to rule.split(" | ").map { single ->
            single.split(' ').map { it.trim('"') }
        }
    }.toMap().toMutableMap()

    val res1 = spl[1].split('\n').filter { isValid(rules, it) }.count()
    println("Part 1: $res1")


    rules[8] = listOf(listOf("42"), listOf("42", "8"))
    rules[11] = listOf(listOf("42", "31"), listOf("42", "11", "31"))
    val res2 = spl[1].split('\n').filter { isValid(rules, it) }.count()
    println("Part 2: $res2")
}