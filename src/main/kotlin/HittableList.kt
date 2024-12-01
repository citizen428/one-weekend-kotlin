package net.citizen428

class HittableList() : Hittable {
    val objects = mutableListOf<Hittable>()

    constructor(obj: Hittable) : this() {
        add(obj)
    }

    fun clear() = objects.clear()

    fun add(obj: Hittable) = objects.add(obj)

    override fun hit(r: Ray, rayT: Interval): HitRecord? {
        var hitRecord: HitRecord? = null
        var closestSoFar = rayT.max

        for (obj in objects) {
            when (val hr = obj.hit(r, Interval(rayT.min, closestSoFar))) {
                null -> continue
                else -> {
                    closestSoFar = hr.t
                    hitRecord = hr
                }
            }
        }

        return hitRecord
    }
}
