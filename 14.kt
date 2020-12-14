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

fun getVariants(mask: String): Int {
    val res = mask.reversed().map { bit -> if (bit == 'X') 2 else 0 }
    if (res.all { it == 0 }) {
        return 1
    }
    return res.filterNot { it == 0 }.reduce { acc, value -> acc * value }
}

fun getSingleConflictsCount(mask: String, filter: String): Int {
    val res = mask.mapIndexed { i, bit ->
        when (bit) {
            'X' -> if (filter[i] == 'X') 2 else 1
            filter[i] -> 0
            else -> if (filter[i] == 'X') 0 else return 0
        }
    }.filterNot { it == 0 }

    if (res.count() == 0 || res.all { it == 2 }) {
        /* filter is exactly same or filters all possible values */
        return getVariants(mask)
    }
    return res.reduce { acc, i -> acc*i }
}

/* Count occurrences of the mask in already processed masks */
fun getConflictsCount(mask: String, filters: List<String>): Int {
    // remove all filters that do not apply on mask
    val filtered = filters.filter { item ->
        item.mapIndexed { i, v ->
            !(v != mask[i] && v != 'X' && mask[i] != 'X')
        }.all { it }
    }
    if (filtered.count() == 0) {
        return 0
    }
    // merge filters together
    var filter = filtered[0].toMutableList()
    if (filtered.count() > 1) {
        filtered.subList(1, filtered.count()).forEach { item ->
            item.forEachIndexed { i, bit ->
                filter[i] = when (bit) {
                    filter[i] -> bit
                    else -> 'X'
                }
            }
        }
    }
    /* Make sure the data have common addresses */
    return getSingleConflictsCount(mask, filter.joinToString(""))
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
    val addresses = mutableListOf<Pair<String, Long>>()
    data.forEach { (mask, payload) ->
        addresses += payload.map { (addr, value) ->
            getNewMask(addr, mask) to value
        }
    }

    val masks = addresses.reversed().map { it.first }
    /* loop backwards, last data will remain in memory */
    val res = addresses.reversed().mapIndexed { i, (mask, value) ->
        val variants = getVariants(mask) - getConflictsCount(mask, masks.subList(0, i))
        variants*value
    }.sum()

    println("Part 2: $res")
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

