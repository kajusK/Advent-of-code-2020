import java.io.File

class Computer(var origInstructions: List<Pair<String, Int>>) {
    private var pc = 0
    private var acc = 0
    private var instructions = origInstructions.toMutableList()

    private fun runNext() {
        val instruction = instructions[pc].first
        val arg = instructions[pc].second
        when (instruction) {
            "nop" -> pc += 1
            "acc" -> {
                acc +=  arg
                pc += 1
            }
            "jmp" -> pc += arg
        }
    }

    fun restart() {
        pc = 0
        acc = 0
        instructions = origInstructions.toMutableList()
    }

    fun runInfinite(): Int {
        var alreadyRunned: MutableList<Int> = mutableListOf()

        while (pc !in alreadyRunned && pc < instructions.count()) {
            alreadyRunned.add(pc)
            runNext()
        }
        return acc
    }

    fun findEnd(): Int {
        var replacePos = 0
        while (pc < instructions.count()) {
            restart()
            if (instructions[replacePos].first == "nop") {
                instructions[replacePos] = "jmp" to instructions[replacePos].second
            } else if (instructions[replacePos].first == "jmp") {
                instructions[replacePos] = "nop" to instructions[replacePos].second
            }
            runInfinite()
            replacePos += 1
        }
        return acc
    }
}

fun main() {
    val instructions = File("inputs/08.txt").readLines().map { line ->
        val (inst, num) = line.split(" ")
        inst to num.toInt()
    }
    val comp = Computer(instructions)

    println("Part 1: ${comp.runInfinite()}")
    println("Part 2: ${comp.findEnd()}")
}