package net.citizen428

import kotlin.math.sqrt

class Sphere(val center: Point, r: Double, val mat: Material) : Hittable {
    val radius: Double

    init {
        require(r > 0) { "radius must be positive" }
        radius = r
    }

    override fun hit(r: Ray, rayT: Interval): HitRecord? {
        val oc = center - r.origin
        val a = r.direction.lengthSquared
        val h = r.direction.dot(oc)
        val c = oc.lengthSquared - radius * radius

        val discriminant = h * h - a * c
        if (discriminant <= 0)
            return null

        val sqrtd = sqrt(discriminant)

        // Find the nearest root that lies in the acceptable range.
        var root = (h - sqrtd) / a
        if (!rayT.surrounds(root)) {
            root = (h + sqrtd) / a
            if (!rayT.surrounds(root))
                return null

        }

        val p = r.at(root)
        return HitRecord(
            p = p,
            t = root,
            outwardNormal = (p - center) / radius,
            r = r,
            material = mat
        )
    }
}


