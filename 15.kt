fun getResult(data: List<Int>, rounds: Int) : Int {
    var cur = data.last()
    var round = data.count() - 1
    val map = data.mapIndexed { i, v -> v to i+1 }.toMap().toMutableMap()

    while (true) {
        round++
        if (round == rounds) {
            return cur
        }
        if (cur !in map) {
            map[cur] = round
            cur = 0
        } else {
            val new = round - map[cur]!!
            map[cur] = round
            cur = new
        }
    }
}

fun main() {
    val data = "2,0,1,7,4,14,18".split(",").map { it.toInt() }
    println("Part 1: ${getResult(data, 2020)}")
    println("Part 2: ${getResult(data, 30000000)}")
}