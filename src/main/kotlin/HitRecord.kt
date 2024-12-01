package net.citizen428

// NOTE: the parameter `outwardNormal` is assumed to have unit length.
class HitRecord(val p: Point, val t: Double, outwardNormal: Vec3, r: Ray) {
    val frontFacing: Boolean
    val normal: Vec3

    init {
        frontFacing = r.direction.dot(outwardNormal) < 0
        normal = if (frontFacing) outwardNormal else -outwardNormal
    }
}
