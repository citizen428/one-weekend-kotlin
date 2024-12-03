package net.citizen428

class Lambertian(val albedo: Color) : Material {
    override fun scatter(rayIn: Ray, hitRecord: HitRecord): ScatterRecord? {
        val scatterDirection = hitRecord.normal + Vec3.randomUnitVector()

        return ScatterRecord(
            Ray(
                hitRecord.p,
                // Prevent degenerate scatter direction
                if (scatterDirection.isNearZero()) hitRecord.normal else scatterDirection
            ),
            albedo
        )
    }
}
