package net.citizen428

import kotlin.math.min
import kotlin.math.pow
import kotlin.math.sqrt
import kotlin.random.Random

class Dielectric(val refractionIndex: Double) : Material {
    override fun scatter(rayIn: Ray, hitRecord: HitRecord): ScatterRecord? {
        val ri = if (hitRecord.frontFacing) (1.0 / refractionIndex) else refractionIndex

        fun reflectance(cosine: Double): Double {
            val r0 = (1 - ri) / (1 + ri)
            val r0Squared = r0 * r0
            return r0Squared + (1 - r0Squared) * (1 - cosine).pow(5)
        }

        val unitDirection = rayIn.direction.unit()
        val cosTheta = min(-unitDirection.dot(hitRecord.normal), 1.0)
        val sinTheta = sqrt(1.0 - cosTheta * cosTheta)
        val cannotRefract = ri * sinTheta > 1.0

        val direction = if (cannotRefract || reflectance(cosTheta) > Random.nextDouble())
            unitDirection.reflect(hitRecord.normal)
        else
            unitDirection.refract(hitRecord.normal, ri)

        return ScatterRecord(
            ray = Ray(hitRecord.p, direction),
            attenuation = Color(1.0, 1.0, 1.0)
        )
    }
}
