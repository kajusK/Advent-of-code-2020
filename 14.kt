import java.io.File

fun applyMask(data: Long, mask: String): Long {
    var res = String()
    val bits = data.toString(2).reversed()

    mask.reversed().forEachIndexed { i, mbit ->
        res += when (mbit) {
            'X' -> if (i < bits.count()) bits[i] else '0'
            '0' -> '0'
            '1' -> '1'
            else -> error("Unknown character")
        }
    }
    return res.reversed().toLong(2)
}

fun getNewMask(addr: Int, mask: String): String {
    var res = String()
    val bits = addr.toString(2).reversed()

    mask.reversed().forEachIndexed { i, mbit ->
        val bit = if (i < bits.count()) bits[i] else '0'
        res += when (mbit) {
            '0' -> bit
            '1' -> '1'
            'X' -> 'X'
            else -> error("Unknown character")
        }
    }
    return res.reversed()
}

fun getAllAddresses(mask: String) : List<Long> {
    if ('X' !in mask) {
        return listOf(mask.toLong(2))
    }
    val res = getAllAddresses(mask.replaceFirst('X', '1'))
    return res + getAllAddresses(mask.replaceFirst('X', '0'))
}

fun part1(data: List<Pair<String, List<Pair<Int, Long>>>>) {
    var mem = mutableMapOf<Int, Long>()
    data.forEach { (mask, payload) ->
         payload.forEach { (addr, value) ->
             mem[addr] = applyMask(value, mask)
         }
    }
    println("Part 1: ${mem.values.sum()}")
}

fun part2(data: List<Pair<String, List<Pair<Int, Long>>>>) {
    val mem = mutableMapOf<Long, Long>()
    data.forEach { (mask, payload) ->
        payload.forEach { (addr, value) ->
            getAllAddresses(getNewMask(addr, mask)).forEach {
                mem[it] = value
            }
        }
    }

    println("Part 2: ${mem.values.sum()}")
}

fun main() {
    val data = File("inputs/14.txt").readText().split("mask = ").map { block ->
        val spl = block.trim().split('\n')
        val mask = spl[0]
        val data = spl.subList(1, spl.count()).map { line ->
            val (addr, value) = line.replace(Regex("""mem\[|\]"""), "").split(" = ")
            addr.toInt() to value.toLong()
        }
        mask to data
    }

    part1(data)
    part2(data)
}

