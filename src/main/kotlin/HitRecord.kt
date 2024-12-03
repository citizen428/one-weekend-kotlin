package net.citizen428

// NOTE: the parameter `outwardNormal` is assumed to have unit length.
class HitRecord(
    val p: Point,
    val t: Double,
    outwardNormal: Vec3,
    r: Ray,
    val material: Material
) {
    val frontFacing: Boolean = r.direction.dot(outwardNormal) < 0
    val normal: Vec3 = if (frontFacing) outwardNormal else -outwardNormal
}
