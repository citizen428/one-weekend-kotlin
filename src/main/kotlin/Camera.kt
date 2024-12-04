package net.citizen428

import java.awt.image.BufferedImage
import java.io.File
import javax.imageio.ImageIO
import kotlin.math.PI
import kotlin.math.tan
import kotlin.random.Random

data class CameraOptions(
    // Rendered image width in pixel count
    val imageWidth: Int = 400,
    // Ratio of image width over height
    val aspectRatio: Double = 1.0,
    // Count of random samples for each pixel
    val samplesPerPixel: Int = 10,
    // Point camera is looking from
    val lookFrom: Point = Point.origin,
    // Point camera is looking at
    val lookAt: Point = Point(0.0, 0.0, -1.0),
    // Camera-relative "up" direction
    val viewUp: Vec3 = Vec3(0.0, 1.0, 0.0),
    // Vertical view angle (field of view)
    val vFieldOfView: Double = 90.0,
)

class Camera(private val options: CameraOptions) {
    private val imageWidth = options.imageWidth

    // Rendered image height
    private val imageHeight = (options.imageWidth / options.aspectRatio).toInt()

    // Camera center
    private val center = options.lookFrom

    // Location of pixel 0, 0
    private val pixel00Loc: Vec3

    // Offset to pixel to the right
    private val pixelDeltaU: Vec3

    // Offset to pixel below
    private val pixelDeltaV: Vec3

    // Color scale factor for a sum of pixel samples
    private val pixelSamplesScale: Double

    // Maximum number of ray bounces into scene
    private val maxDepth = 50

    // Camera frame basis vectors
    private val u: Vec3
    private val v: Vec3
    private val w: Vec3

    init {
        // Determine viewport dimensions.
        val focalLength = (options.lookFrom - options.lookAt).length
        val theta = options.vFieldOfView * PI / 180.0
        val h = tan(theta / 2)
        val viewportHeight = 2 * h * focalLength
        val viewportWidth = viewportHeight * (imageWidth.toDouble() / imageHeight)

        // Calculate the u,v,w unit basis vectors for the camera coordinate frame.
        w = (options.lookFrom - options.lookAt).unit()
        u = options.viewUp.cross(w).unit()
        v = w.cross(u)

        // Calculate the vectors across the horizontal and down the vertical viewport edges.
        val viewportU = viewportWidth * u    // Vector across viewport horizontal edge
        val viewportV = viewportHeight * -v  // Vector down viewport vertical edge

        // Calculate the horizontal and vertical delta vectors from pixel to pixel.
        pixelDeltaU = viewportU / imageWidth.toDouble()
        pixelDeltaV = viewportV / imageHeight.toDouble()

        // Calculate the location of the upper left pixel.
        val viewPortUpperLeft = center - (focalLength * w) - viewportU / 2 - viewportV / 2
        pixel00Loc = viewPortUpperLeft + 0.5 * (pixelDeltaV + pixelDeltaV)

        pixelSamplesScale = 1.0 / options.samplesPerPixel
    }

    fun render(world: Hittable, imagePath: String) {
        val image = BufferedImage(imageWidth, imageHeight, BufferedImage.TYPE_INT_RGB)
        for (x in 0 until imageWidth) {
            for (y in 0 until imageHeight) {
                var pixelColor = Color(0.0, 0.0, 0.0)
                repeat(options.samplesPerPixel) {
                    val r = getRay(x, y)
                    pixelColor += rayColor(r, world)
                }

                val scaledPixelColor = pixelSamplesScale * pixelColor
                image.setRGB(x, y, scaledPixelColor.toInt())
            }
        }

        ImageIO.write(image, "PNG", File(imagePath))
    }

    fun getRay(x: Int, y: Int): Ray {
        // Construct a camera ray originating from the origin and directed at
        // randomly sampled point around the pixel location i, j.
        val offset = sampleSquare()
        val pixelSample = pixel00Loc + ((x + offset.x) * pixelDeltaU) + ((y + offset.y) * pixelDeltaV)
        val rayOrigin = center
        val rayDirection = pixelSample - rayOrigin
        return Ray(rayOrigin, rayDirection)
    }

    // Returns the vector to a random point in the [-.5,-.5]-[+.5,+.5] unit square.
    fun sampleSquare() = Vec3(
        Random.nextDouble() - 0.5,
        Random.nextDouble() - 0.5,
        0.0
    )

    private fun rayColor(r: Ray, world: Hittable, depth: Int = maxDepth): Color {
        if (depth <= 0)
            return Color(0.0, 0.0, 0.0)

        val hitRecord = world.hit(r, Interval(0.001, Double.POSITIVE_INFINITY))
        if (hitRecord != null) {
            return when (val sr = hitRecord.material.scatter(r, hitRecord)) {
                null -> Color(0.0, 0.0, 0.0)
                else -> sr.attenuation * rayColor(sr.ray, world, depth - 1)
            }
        } else {
            val unitDirection = r.direction.unit()
            val a = 0.5 * (unitDirection.y + 1)
            return (1 - a) * Color(1.0, 1.0, 1.0) + a * Color(0.5, 0.7, 1.0)
        }
    }
}
