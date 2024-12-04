package net.citizen428

import kotlin.random.Random

object RayTracer {
    @JvmStatic
    fun main(args: Array<String>) {
        val world = HittableList()

        val groundMaterial = Lambertian(Color(0.5, 0.5, 0.5))
        world.add(Sphere(Point(0.0, -1000.0, 0.0), 1000.0, groundMaterial))

        for (a in -11 until 11) {
            for (b in -11 until 11) {
                val chooseMat = Random.nextDouble()
                val center = Point(a + Random.nextDouble(), 0.2, b + Random.nextDouble())

                if ((center - Point(4.0, 0.2, 0.9)).length > 0.9) {
                    val sphereMaterial = when {
                        chooseMat < 0.8 ->
                            Lambertian(Color.random(0.0, 1.0) * Color.random(0.0, 1.0))

                        chooseMat < 0.95 ->
                            Metal(Color.random(0.5, 1.0), Random.nextDouble(0.0, 0.5))

                        else -> Dielectric(1.5)
                    }
                    world.add(Sphere(center, 0.2, sphereMaterial))
                }
            }
        }

        val material1 = Dielectric(1.5)
        world.add(Sphere(Point(0.0, 1.0, 0.0), 1.0, material1))

        val material2 = Lambertian(Color(0.4, 0.2, 0.1))
        world.add(Sphere(Point(-4.0, 1.0, 0.0), 1.0, material2))

        val material3 = Metal(Color(0.7, 0.6, 0.5), 0.0)
        world.add(Sphere(Point(4.0, 1.0, 0.0), 1.0, material3))

        val cam = Camera(
            CameraOptions(
                aspectRatio = 16.0 / 9.0,
                imageWidth = 1200,
                samplesPerPixel = 500,

                vFieldOfView = 20.0,
                lookFrom = Point(13.0, 2.0, 3.0),
                lookAt = Point(0.0, 0.0, 0.0),

                defocusAngle = 0.6,
                focusDistance = 10.0,
            )
        )
        cam.render(world, "images/chapter14.png")
    }
}
