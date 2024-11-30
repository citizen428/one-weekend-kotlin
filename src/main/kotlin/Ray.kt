package net.citizen428

class Ray(val origin: Point, val direction: Vec3) {
    fun at(t: Double): Point = origin + t * direction
}
