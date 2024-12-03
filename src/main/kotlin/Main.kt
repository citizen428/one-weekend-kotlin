package net.citizen428

object RayTracer {
    @JvmStatic
    fun main(args: Array<String>) {
        val materialGround = Lambertian(Color(0.8, 0.8, 0.0))
        val materialCenter = Lambertian(Color(0.1, 0.2, 0.5))
        val materialLeft = Metal(Color(0.8, 0.8, 0.8), 0.3)
        val materialRight = Metal(Color(0.8, 0.6, 0.2), 1.0)

        val world = HittableList().apply {
            add(Sphere(Point(0.0, -100.5, -1.0), 100.0, materialGround))
            add(Sphere(Point(0.0, 0.0, -1.2), 0.5, materialCenter))
            add(Sphere(Point(-1.0, 0.0, -1.0), 0.5, materialLeft))
            add(Sphere(Point(1.0, 0.0, -1.0), 0.5, materialRight))
        }

        val cam = Camera(
            aspectRatio = 16.0 / 9.0,
            imageWidth = 800,
            samplesPerPixel = 100
        )
        cam.render(world, "images/chapter10b.png")
    }
}
