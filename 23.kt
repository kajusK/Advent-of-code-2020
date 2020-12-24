data class Cup(val id: Int, var next: Cup?, var prev: Cup?)

class Cups(private val start: List<Int>) {
    private val cupsCount = 1000000
    private val map = HashMap<Int, Cup>(cupsCount)
    private var cur: Cup? = null
    private var max = 0
    private var min = 0

    init {
        var prev: Cup? = null

        val initial = start.forEach {
            val new = Cup(it, null, prev)
            map[it] = new
            prev?.next = new
            prev = new
        }

        var id = start.max()!!
        (start.size until cupsCount).forEach {
            id++
            val new = Cup(id, null, prev)
            map[id] = new
            prev?.next = new
            prev = new
        }
        prev?.next = map[start[0]]
        map[start[0]]!!.prev = prev
        cur = map[start[0]]
        max = id
        min = start.min()!!
    }

    private fun nextId(pickup: Cup): Int {
        val ids = mutableListOf<Int>()
        var cup = pickup

        (0 until 3).forEach {
            ids.add(cup.id)
            cup = cup.next!!
        }

        var searchId = cur!!.id
        do {
            searchId = if (searchId <= min) max else searchId - 1
        } while(searchId in ids)

        return searchId
    }

    private fun round() {
        val pickup = cur!!.next!!
        val pickupLast = pickup.next!!.next!!

        cur!!.next = pickupLast.next
        pickupLast.next!!.prev = cur

        val search = map[nextId(pickup)]!!
        val next = search.next!!
        search.next = pickup
        next.prev = pickupLast
        pickupLast.next = next
        pickup.prev = search

        cur = cur?.next
    }

    fun run(rounds: Int): Long {
        (0 until rounds).forEach {
            round()
        }

        val one = map[1]!!
        return one.next!!.id.toLong() * one.next!!.next!!.id.toLong()
    }
}

fun part1(input: List<Int>) {
    var cups = input.toMutableList()
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

fun main() {
    val input = "952438716".map { it.toString().toInt() }
    part1(input)

    val cups = Cups(input)
    println("Part 2: ${cups.run(10000000)}")
}