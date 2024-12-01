package net.citizen428

class Interval(val min: Double, val max: Double = Double.NEGATIVE_INFINITY) {
    val size = max - min

    fun contains(x: Double): Boolean = min <= x && x <= max

    fun surrounds(x: Double): Boolean = min < x && x < max

    companion object {
        val empty: Interval by lazy(LazyThreadSafetyMode.SYNCHRONIZED) {
            Interval(Double.POSITIVE_INFINITY, Double.NEGATIVE_INFINITY)
        }

        val universe: Interval by lazy(LazyThreadSafetyMode.SYNCHRONIZED) {
            Interval(Double.NEGATIVE_INFINITY, Double.POSITIVE_INFINITY)
        }
    }
}
