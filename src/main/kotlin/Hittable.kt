package net.citizen428

interface Hittable {
    fun hit(r: Ray, rayT: Interval): HitRecord?
}
