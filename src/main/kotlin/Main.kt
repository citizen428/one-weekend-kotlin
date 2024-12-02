package net.citizen428

object RayTracer {
    @JvmStatic
    fun main(args: Array<String>) {
        val world = HittableList().apply {
            add(Sphere(Point(0.0, 0.0, -1.0), 0.5))
            add(Sphere(Point(0.0, -100.5, -1.0), 100.0))
        }

        val cam = Camera(
            aspectRatio = 16.0 / 9.0,
            imageWidth = 800,
            samplesPerPixel = 100
        )
        cam.render(world, "images/chapter08.png")
    }
}
