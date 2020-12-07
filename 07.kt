import java.io.File

fun expand(search: String, skip: List<String>, data: Map<String, Map<String, String>>) : Map<String, String>{
    var expanded: Map<String, String> = data[search]!!.toMutableMap()

    data[search]!!.keys.forEach { key ->
        if (key !in skip && key != "other") {
            expanded += expand(key, data[search]!!.keys.toList(), data)
        }
    }
    return expanded
}

fun count_bags(search: String, data: Map<String, Map<String, String>>) : Int {
    var sum = 1

    data[search]!!.forEach { (key, value) ->
        if (key != "other") {
            sum += count_bags(key, data) * value.toInt()
        }
    }
    return sum
}

fun main() {
    val bags = File("inputs/07.txt").readLines().associate { line ->
        val (bag, inside) = line.replace(Regex("""\.|bags?"""), "").split("contain")
        val bags = inside.split(",").associate {
            val (count, type) = it.trim().split(" ", limit=2)
            type to count
        }
        bag.trim() to bags
    }


    var expanded: MutableMap<String, Map<String, String>> = mutableMapOf()
    bags.keys.forEach { key ->
        expanded[key] = expand(key, listOf(), bags)
    }
    println("Part 1: ${ expanded.values.map { it.containsKey("shiny gold") }.filter { it }.count() }")
    println("Part 2: ${ count_bags("shiny gold", bags) - 1 }")
}