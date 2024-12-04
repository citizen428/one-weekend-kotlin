package net.citizen428

object RayTracer {
    @JvmStatic
    fun main(args: Array<String>) {
        val materialGround = Lambertian(Color(0.8, 0.8, 0.0))
        val materialCenter = Lambertian(Color(0.1, 0.2, 0.5))
        val materialLeft = Dielectric(1.5)
        val materialBubble = Dielectric(1.0 / 1.5)
        val materialRight = Metal(Color(0.8, 0.6, 0.2), 1.0)

        val world = HittableList().apply {
            add(Sphere(Point(0.0, -100.5, -1.0), 100.0, materialGround))
            add(Sphere(Point(0.0, 0.0, -1.2), 0.5, materialCenter))
            add(Sphere(Point(-1.0, 0.0, -1.0), 0.5, materialLeft))
            add(Sphere(Point(-1.0, 0.0, -1.0), 0.4, materialBubble))
            add(Sphere(Point(1.0, 0.0, -1.0), 0.5, materialRight))
        }

        val cam = Camera(
            CameraOptions(
                aspectRatio = 16.0 / 9.0,
                imageWidth = 800,
                lookFrom = Point(-2.0, 2.0, 1.0),
                samplesPerPixel = 100,
                vFieldOfView = 20.0,
                defocusAngle = 10.0,
                focusDistance = 3.4
            )
        )
        cam.render(world, "images/chapter13.png")
    }
}
