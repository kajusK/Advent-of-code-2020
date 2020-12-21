import java.io.File

fun main() {
    val allergens = mutableMapOf<String, Set<String>>()

    val ingredients = File("inputs/21.txt").readLines().map { line ->
        val spl = line.split(" (")
        val ing = spl[0].split(' ').toSet()
        val algs = spl[1].removeSurrounding("contains ", ")").split(", ")
        algs.forEach { allergens[it] = if (it !in allergens) ing else allergens[it]!!.intersect(ing) }
        ing
    }.flatten()

    val withoutAllergens = ingredients.toSet() - allergens.values.flatten()
    println("Part 1: ${ingredients.filter { it in withoutAllergens }.size}")

    do {
        val single = allergens.values.filter { it.size == 1 }.flatten()
        allergens.forEach { (key, value) ->
            if (value.size != 1) {
                allergens[key] = value - single
            }
        }
    } while (single.size != allergens.size)

    println("Part 2: ${allergens.toList().sortedBy { (key, value) -> key }.joinToString(",") { (_, value) -> value.first() }}")
}