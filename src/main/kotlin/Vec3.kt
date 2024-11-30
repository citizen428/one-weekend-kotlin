package net.citizen428

import kotlin.math.sqrt

class Vec3(val x: Double, val y: Double, val z: Double) {
    // GLSL-like component aliases
    val r: Double get() = x
    val g: Double get() = y
    val b: Double get() = z

    val lengthSquared = x * x + y * y + z * z
    val length = sqrt(lengthSquared)

    operator fun unaryMinus(): Vec3 = Vec3(-x, -y, -z)

    operator fun plus(other: Vec3): Vec3 =
        Vec3(x + other.x, y + other.y, z + other.z)

    operator fun minus(other: Vec3): Vec3 =
        Vec3(x - other.x, y - other.y, z - other.z)

    operator fun times(other: Vec3): Vec3 =
        Vec3(x * other.x, y * other.y, z * other.z)

    operator fun times(t: Double): Vec3 = Vec3(x * t, y * t, z * t)
    operator fun times(t: Int): Vec3 = this.times(t.toDouble())

    operator fun div(t: Double): Vec3 = Vec3(x / t, y / t, z / t)
    operator fun div(t: Int): Vec3 = this.div(t.toDouble())

    fun dot(other: Vec3): Double =
        x * other.x + y * other.y + z * other.z

    fun cross(other: Vec3): Vec3 =
        Vec3(
            y * other.z - z * other.y,
            z * other.x - x * other.z,
            x * other.y - y * other.x
        )

    fun unit(): Vec3 = this / length

    companion object {
        val origin = Vec3(0.0, 0.0, 0.0)
    }
}

operator fun Double.times(v: Vec3): Vec3 = v * this
operator fun Int.times(v: Vec3): Vec3 = v * this.toDouble()

fun Color.toInt(): Int {
    fun clamp(x: Double) = (255.999 * x).toInt()

    return (clamp(r) shl 16) or (clamp(g) shl 8) or clamp(b)
}

typealias Point = Vec3
typealias Color = Vec3
