fun main() {
    var cups = "952438716".map { it.toString().toInt() }.toMutableList()

    var curPos = 0

    (0 until 100).forEach {
        val cur = cups[curPos]
        var next = cur
        val pickup = (0 until 3).map { i -> cups[(curPos+1+i)%cups.size] }
        cups.removeAll(pickup)

        do {
            next--;
            if (next < cups.min()!!) {
                next = cups.max()!!
            }
        } while (next !in cups)
        cups.addAll(cups.indexOf(next)+1, pickup)
        curPos = (cups.indexOf(cur) + 1) % cups.size
    }

    val start = cups.indexOf(1)
    cups = (cups.subList(start, cups.size) + cups.subList(0, start)).toMutableList()
    println("Part 1: ${cups.subList(1, cups.size).joinToString("")}")
}