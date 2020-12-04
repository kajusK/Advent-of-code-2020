import java.io.File

fun isValidSimple(fields: Map<String, String>) : Boolean {
    val required = listOf("byr", "iyr", "eyr", "hgt", "hcl", "ecl", "pid")
    for (field in required) {
        if (field !in fields) {
            return false
        }
    }
    return true
}

fun isValid(fields: Map<String, String>) : Boolean {
    val colors = listOf("amb", "blu", "brn", "gry", "grn", "hzl", "oth")

    val byr = fields["byr"]?.toInt() ?: return false
    val iyr = fields["iyr"]?.toInt() ?: return false
    val eyr = fields["eyr"]?.toInt() ?: return false
    val hgt = fields["hgt"] ?: return false
    val hcl = fields["hcl"] ?: return false
    val ecl = fields["ecl"] ?: return false
    val pid = fields["pid"] ?: return false

    if (byr !in 1920..2002) {
        return false
    }
    if (iyr !in 2010..2020) {
        return false
    }
    if (eyr !in 2020..2030) {
        return false
    }

    if (hgt.endsWith("in")) {
        val num = hgt.replace("in", "").toInt()
        if (num !in 59..76) {
            return false
        }
    } else if (hgt.endsWith("cm")) {
        val num = hgt.replace("cm", "").toInt()
        if (num !in 150..193) {
            return false
        }
    } else {
        return false
    }

    if (!Regex("""#[0-9a-f]{6}""").matches(hcl)) {
        return false
    }
    if (ecl !in colors) {
        return false
    }
    if (!Regex("""[0-9]{9}""").matches(pid)) {
        return false
    }

    return true
}

fun main(args: Array<String>) {
    val passports = File("inputs/04.txt").readText().split("\n\n")
    val fields = passports.map {
        it.split("\n", " ").associate {
                val (key, value) = it.split(':')
                key to value
            }
        }

    println("Part1: ${fields.filter { isValidSimple(it) }.count()}")
    println("Part2: ${fields.filter { isValid(it) }.count()}")
}