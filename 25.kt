fun searchLoop(subject: Long, expected: Long): Int {
    var num = subject
    var loopSize = 1

    while (num != expected) {
        loopSize++
        num *= subject
        num %= 20201227
    }
    return loopSize
}

fun transform(subject: Long, loopSize: Int): Long {
    var num = subject
    (1 until loopSize).forEach {
        num *= subject
        num %= 20201227
    }
    return num
}

fun main() {
    val subject = 7.toLong()
    val pubDoor = 11349501.toLong()
    val pubCard = 5107328.toLong()
    val loopsDoor = searchLoop(subject, pubDoor)
    println("Part 1: ${transform(pubCard, loopsDoor)}")
}