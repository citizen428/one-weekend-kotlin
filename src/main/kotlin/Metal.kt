package net.citizen428

class Metal(val albedo: Color, val fuzz: Double) : Material {
    override fun scatter(rayIn: Ray, hitRecord: HitRecord): ScatterRecord? {
        val reflected = rayIn.direction.reflect(hitRecord.normal)
        val fuzzed = reflected.unit() + (fuzz * Vec3.randomUnitVector())
        val scattered = Ray(hitRecord.p, fuzzed)
        if (scattered.direction.dot(hitRecord.normal) <= 0)
            return null

        return ScatterRecord(
            ray = scattered,
            attenuation = albedo
        )
    }
}
