import java.io.File

fun main() {
    val answers = File("inputs/06.txt").readText().split("\n\n")
    val allYes = answers.map { it.replace("\n", "").toSet() }.map { it.count() }.sum()
    val commonYes = answers.map { group ->
        group.split('\n').map { it.toSet() }.reduce{ acc, v -> acc.intersect(v) }.count()
    }.sum()

    println("Part1: $allYes")
    println("Part2: $commonYes")
}