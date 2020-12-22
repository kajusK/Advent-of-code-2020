import java.io.File

fun MutableList<Int>.pop(): Int {
    val res = this.first()
    this.removeAt(0)
    return res
}

fun getScore(decks: List<List<Int>>): Int {
    return decks.flatten().reversed().foldRightIndexed(0) { i, v, acc -> (i+1)*v + acc }
}

fun part1(decks: List<MutableList<Int>>) {
    while (decks[0].size > 0 && decks[1].size > 0) {
        val pl1 = decks[0].pop()
        val pl2 = decks[1].pop()
        val res = listOf(pl1, pl2).sorted().reversed()
        if (pl1 > pl2) {
            decks[0].addAll(res)
        } else {
            decks[1].addAll(res)
        }
    }

    println("Part 1: ${getScore(decks)}")
}

fun part2(decks: List<MutableList<Int>>): Int {
    val previous = mutableListOf<List<List<Int>>>()

    while (decks[0].size > 0 && decks[1].size > 0) {
        if (decks in previous) {
            return 0
        }
        previous.add(decks.map { it.toList() })

        val pl1 = decks[0].pop()
        val pl2 = decks[1].pop()
        if (decks[0].size < pl1 || decks[1].size < pl2) {
            val res = listOf(pl1, pl2).sorted().reversed()
            if (pl1 > pl2) {
                decks[0].addAll(res)
            } else {
                decks[1].addAll(res)
            }
        } else {
            val newDecks = decks.map { it.toMutableList() } //copy
            if (part2(listOf(newDecks[0].subList(0, pl1), newDecks[1].subList(0, pl2))) == 0) {
                decks[0].addAll(listOf(pl1, pl2))
            } else {
                decks[1].addAll(listOf(pl2, pl1))
            }
        }
    }

    return if (decks[0].size > decks[1].size) 0 else 1
}

fun main() {
    val decks = File("inputs/22.txt").readText().replace(Regex("""Player [0-9]:\n"""), "").split("\n\n").map { player ->
        player.split('\n').map { it.toInt() }.toMutableList()
    }

    part1(decks.map{ it.toMutableList() })
    part2(decks)
    println("Part 2: ${getScore(decks)}")
}