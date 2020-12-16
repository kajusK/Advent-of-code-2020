import java.io.File

fun main() {
    val spl = File("inputs/16.txt").readText().split("\n\n")
    val fields = spl[0].split('\n').associate { row ->
        val (name, range) = row.split(": ")
        val ranges = range.replace(" or ", "-").split('-').map { it.toInt() }
        name to listOf(ranges[0]..ranges[1], ranges[2]..ranges[3])
    }
    val myticket = spl[1].replace("your ticket:\n", "").split(',').map { it.toLong() }
    val tickets = spl[2].replace("nearby tickets:\n", "").split("\n").map { line ->
        line.split(',').map { it.toInt() }
    }

    val invalid = tickets.map { ticket ->
        ticket.filterNot { field ->
            fields.any { (_, value) -> field in value[0] || field in value[1] }
        }.sum()
    }.sum()
    println("Part 1: $invalid")

    // get possible fields names for columns
    var variants = tickets.map { ticket -> ticket.map { field ->
            // first get sets of possible field names for each field
            fields.filter { (_, value) -> field in value[0] || field in value[1] }.keys.toSet()
    }}.filter { ticket -> ticket.all { it.count() != 0 } }.reduce { acc, ticket ->
        // filter out invalid rows and intersect all sets in separate columns
        acc.mapIndexed { i, field -> field.intersect(ticket[i]) }
    }

    // remove single key in column from all other columns until there are columns only with single items
    do {
        val single = variants.filter { it.count() == 1 }.map { it.first() }
        variants = variants.map { field -> if (field.count() == 1) field else field - single }
    } while (single.count() != variants.count())

    val res = myticket.filterIndexed { i, _ -> "departure" in variants[i].first() }.reduce { acc, i -> acc * i}
    println("Part 2: $res")
}