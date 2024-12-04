package net.citizen428

import kotlin.math.abs
import kotlin.math.min
import kotlin.math.sqrt
import kotlin.random.Random

const val EPSILON: Double = 1e-8

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

    fun isNearZero(): Boolean {
        fun withInDelta(d: Double) =
            abs(d) < EPSILON

        return withInDelta(r) && withInDelta(g) && withInDelta(b)
    }

    fun reflect(normal: Vec3): Vec3 =
        this - 2 * this.dot(normal) * normal

    fun refract(normal: Vec3, etaIOverEtaT: Double): Vec3 {
        val cosTheta = min(-this.dot(normal), 1.0)
        val rOutPerp = etaIOverEtaT * (this + cosTheta * normal)
        val rOutParallel = -sqrt(abs(1.0 - rOutPerp.lengthSquared)) * normal
        return rOutPerp + rOutParallel
    }

    companion object {
        val origin = Vec3(0.0, 0.0, 0.0)

        fun random(min: Double, max: Double): Vec3 =
            Vec3(
                Random.nextDouble(min, max),
                Random.nextDouble(min, max),
                Random.nextDouble(min, max)
            )

        fun randomUnitVector(): Vec3 {
            while (true) {
                val p = random(-1.0, 1.0)
                if (1e-160 < p.lengthSquared && p.lengthSquared <= 1)
                    return p.unit()
            }
        }

        fun randomInUnitDisk(): Vec3 {
            while (true) {
                val p = Vec3(
                    Random.nextDouble(-1.0, 1.0),
                    Random.nextDouble(-1.0, 1.0),
                    0.0
                )
                if (p.lengthSquared <= 1)
                    return p
            }
        }

        fun randomOnHemisphere(normal: Vec3): Vec3 {
            val onUnitSphere = randomUnitVector()
            return if (onUnitSphere.dot(normal) > 0)
                onUnitSphere
            else
                -onUnitSphere
        }
    }
}

operator fun Double.times(v: Vec3): Vec3 = v * this
operator fun Int.times(v: Vec3): Vec3 = v * this.toDouble()

fun Color.toInt(): Int {
    val validInterval = Interval(0.0, 0.999)

    fun clamp(x: Double) = (256 * validInterval.clamp(x)).toInt()
    fun linearToGamma(linear: Double): Double =
        if (linear > 0) sqrt(linear) else 0.0

    fun process(x: Double): Int = clamp(linearToGamma(x))

    return (process(r) shl 16) or (process(g) shl 8) or process(b)
}

typealias Point = Vec3
typealias Color = Vec3
