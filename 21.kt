import java.io.File

fun main() {
    val alergens = mutableMapOf<String, Set<String>>()
    val foods = mutableSetOf<String>()

    val allFood = File("inputs/21.txt").readLines().map { line ->
        val spl = line.split(" (")
        val ing = spl[0].split(' ').toSet()
        val algs = spl[1].removeSurrounding("contains ", ")").split(", ")
        algs.forEach { al ->
            if (al !in alergens) {
                alergens[al] = ing
            } else {
                alergens[al] = alergens[al]!!.intersect(ing)
            }
        }
        foods.addAll(ing)
        ing
    }.flatten()

    val withoutAlergens = foods - alergens.values.flatten()
    val part1 = allFood.filter { it in withoutAlergens }.size
    println("Part 1: $part1")

    do {
        val single = alergens.values.filter { it.size == 1 }.flatten()
        alergens.forEach { (key, value) ->
            if (value.size != 1) {
                alergens[key] = value - single
            }
        }
    } while (single.size != alergens.size)

    println("Part 2: ${alergens.toList().sortedBy { (key, value) -> key }.map { (_, value) -> value.first() }.joinToString(",")}")
}