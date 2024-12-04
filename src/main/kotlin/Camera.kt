package net.citizen428

import java.awt.image.BufferedImage
import java.io.File
import javax.imageio.ImageIO
import kotlin.math.PI
import kotlin.math.tan
import kotlin.random.Random

class Camera(
    val aspectRatio: Double = 1.0,
    val imageWidth: Int = 400,
    val samplesPerPixel: Int = 10,
    vFieldOfView: Double = 90.0
) {
    val imageHeight = (imageWidth / aspectRatio).toInt()

    private val lookFrom = Point(-2.0, 2.0, 1.0)   // Point camera is looking from
    private val lookAt = Point(0.0, 0.0, -1.0) // Point camera is looking at
    private val viewUp = Point(0.0, 1.0, 0.0) // Camera-relative "up" direction
    private val center = lookFrom
    private val pixel00Loc: Vec3          // Location of pixel 0, 0
    private val pixelDeltaU: Vec3         // Offset to pixel to the right
    private val pixelDeltaV: Vec3         // Offset to pixel below
    private val pixelSamplesScale: Double // Color scale factor for a sum of pixel samples
    private val maxDepth = 50             // Maximum number of ray bounces into scene
    private val u: Vec3
    private val v: Vec3
    private val w: Vec3

    init {
        // Determine viewport dimensions.
        val focalLength = (lookFrom - lookAt).length
        val theta = vFieldOfView * PI / 180.0
        val h = tan(theta / 2)
        val viewportHeight = 2 * h * focalLength
        val viewportWidth = viewportHeight * (imageWidth.toDouble() / imageHeight)

        // Calculate the u,v,w unit basis vectors for the camera coordinate frame.
        w = (lookFrom - lookAt).unit()
        u = viewUp.cross(w).unit()
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

        pixelSamplesScale = 1.0 / samplesPerPixel
    }

    fun render(world: Hittable, imagePath: String) {
        val image = BufferedImage(imageWidth, imageHeight, BufferedImage.TYPE_INT_RGB)
        for (x in 0 until imageWidth) {
            for (y in 0 until imageHeight) {
                var pixelColor = Color(0.0, 0.0, 0.0)
                repeat(samplesPerPixel) {
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
