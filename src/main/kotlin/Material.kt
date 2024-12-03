package net.citizen428

data class ScatterRecord(val ray: Ray, val attenuation: Color)

interface Material {
    fun scatter(rayIn: Ray, hitRecord: HitRecord): ScatterRecord?
}
