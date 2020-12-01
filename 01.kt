import java.io.File

fun findTwo(nums: List<Int>): Int {
    for (a in nums) {
        for (b in nums) {
            if (a + b == 2020) {
                return a*b
            }
        }
    }
    return -1
}

fun findThree(nums: List<Int>): Int {
    for (a in nums) {
        for (b in nums) {
            for (c in nums) {
                if (a + b + c == 2020) {
                    return a*b*c
                }
            }
        }
    }
    return -1
}

fun main(args: Array<String>) {
    val nums = File("inputs/01.txt").readLines().map { it.toInt() }
    println("Two items: ${findTwo(nums)}")
    println("Three items: ${findThree(nums)}")
}
