package net.citizen428

import java.awt.image.BufferedImage
import java.io.File
import javax.imageio.ImageIO


fun main() {
    // Image

    val aspectRatio = 16 / 9.0
    val imageWidth = 800
    val imageHeight = (imageWidth / aspectRatio).toInt()

    // World

    val world = HittableList().apply {
        add(Sphere(Point(0.0, 0.0, -1.0), 0.5))
        add(Sphere(Point(0.0, -100.5, -1.0), 100.0))
    }

    // Camera

    val focalLength = 1.0
    val viewportHeight = 2.0
    val viewportWidth = viewportHeight * (imageWidth.toDouble() / imageHeight)
    val cameraCenter = Point.origin

    // Calculate the vectors across the horizontal and down the vertical viewport edges.
    val viewportU = Vec3(viewportWidth, 0.0, 0.0)
    val viewportV = Vec3(0.0, -viewportHeight, 0.0)

    // Calculate the horizontal and vertical delta vectors from pixel to pixel.
    val pixelDeltaU = viewportU / imageWidth.toDouble()
    val pixelDeltaV = viewportV / imageHeight.toDouble()

    // Calculate the location of the upper left pixel.
    val viewPortUpperLeft = cameraCenter - Vec3(0.0, 0.0, focalLength) -
            viewportU / 2 - viewportV / 2
    val pixel00Loc = viewPortUpperLeft + 0.5 * (pixelDeltaV + pixelDeltaV)

    // Render

    // Simple linear gradient, blending white and blue based on y
    fun rayColor(r: Ray, world: Hittable): Color {
        val hitRecord = world.hit(r, Interval(0.0, Double.POSITIVE_INFINITY))
        if (hitRecord != null) {
            return 0.5 * (hitRecord.normal + Color(1.0, 1.0, 1.0))
        } else {
            val unitDirection = r.direction.unit()
            val a = 0.5 * (unitDirection.y + 1)
            return (1 - a) * Color(1.0, 1.0, 1.0) + a * Color(0.5, 0.7, 1.0)
        }
    }

    val image = BufferedImage(imageWidth, imageHeight, BufferedImage.TYPE_INT_RGB)
    for (x in 0 until imageWidth) {
        for (y in 0 until imageHeight) {
            val pixelCenter = pixel00Loc + (x * pixelDeltaU) + (y * pixelDeltaV)
            val rayDirection = pixelCenter - cameraCenter
            val ray = Ray(cameraCenter, rayDirection)
            val pixelColor = rayColor(ray, world)

            image.setRGB(x, y, pixelColor.toInt())
        }
    }

    ImageIO.write(image, "PNG", File("images/chapter06b.png"))
}
