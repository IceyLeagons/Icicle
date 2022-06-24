package net.iceyleagons.gradle.utils

internal class Version(version: String) : Comparable<Version?> {
    private val numbers: IntArray

    init {
        val split = version.split("-".toRegex()).toTypedArray()[0].split("\\.".toRegex()).toTypedArray()
        numbers = IntArray(split.size)
        for (i in split.indices) numbers[i] = split[i].toInt()
    }


    override fun compareTo(other: Version?): Int {
        val maxLength = other?.numbers?.let { numbers.size.coerceAtLeast(it.size) }
        for (i in 0 until maxLength!!) {
            val left = if (i < numbers.size) numbers[i] else 0
            val right = if (i < other.numbers.size) other.numbers[i] else 0
            if (left != right)
                return if (left < right) -1 else 1
        }
        return 0
    }
}