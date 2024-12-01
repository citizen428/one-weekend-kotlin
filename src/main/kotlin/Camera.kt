package net.citizen428

import java.awt.image.BufferedImage
import java.io.File
import javax.imageio.ImageIO

class Camera(val aspectRatio: Double = 1.0, val imageWidth: Int = 400) {
    val imageHeight = (imageWidth / aspectRatio).toInt()
    private val center = Point.origin
    private val pixel00Loc: Vec3   // Location of pixel 0, 0
    private val pixelDeltaU: Vec3  // Offset to pixel to the right
    private val pixelDeltaV: Vec3  // Offset to pixel below

    init {
        // Determine viewport dimensions.
        val focalLength = 1.0
        val viewportHeight = 2.0
        val viewportWidth = viewportHeight * (imageWidth.toDouble() / imageHeight)

        // Calculate the vectors across the horizontal and down the vertical viewport edges.
        val viewportU = Vec3(viewportWidth, 0.0, 0.0)
        val viewportV = Vec3(0.0, -viewportHeight, 0.0)

        // Calculate the horizontal and vertical delta vectors from pixel to pixel.
        pixelDeltaU = viewportU / imageWidth.toDouble()
        pixelDeltaV = viewportV / imageHeight.toDouble()

        // Calculate the location of the upper left pixel.
        val viewPortUpperLeft = center - Vec3(0.0, 0.0, focalLength) -
                viewportU / 2 - viewportV / 2
        pixel00Loc = viewPortUpperLeft + 0.5 * (pixelDeltaV + pixelDeltaV)
    }

    fun render(world: Hittable, imagePath: String) {
        val image = BufferedImage(imageWidth, imageHeight, BufferedImage.TYPE_INT_RGB)
        for (x in 0 until imageWidth) {
            for (y in 0 until imageHeight) {
                val pixelCenter = pixel00Loc + (x * pixelDeltaU) + (y * pixelDeltaV)
                val rayDirection = pixelCenter - center
                val ray = Ray(center, rayDirection)
                val pixelColor = rayColor(ray, world)

                image.setRGB(x, y, pixelColor.toInt())
            }
        }

        ImageIO.write(image, "PNG", File(imagePath))
    }

    private fun rayColor(r: Ray, world: Hittable): Color {
        val hitRecord = world.hit(r, Interval(0.0, Double.POSITIVE_INFINITY))
        if (hitRecord != null) {
            return 0.5 * (hitRecord.normal + Color(1.0, 1.0, 1.0))
        } else {
            val unitDirection = r.direction.unit()
            val a = 0.5 * (unitDirection.y + 1)
            return (1 - a) * Color(1.0, 1.0, 1.0) + a * Color(0.5, 0.7, 1.0)
        }
    }
}
