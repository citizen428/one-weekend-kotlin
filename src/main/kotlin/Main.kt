package net.citizen428

import java.awt.image.BufferedImage
import java.io.File
import javax.imageio.ImageIO

const val imageWidth: Int = 256
const val imageHeight: Int = 256

fun main() {
    fun clamp(x: Float) = (255.999 * x).toInt()

    val image = BufferedImage(imageWidth, imageHeight, BufferedImage.TYPE_INT_RGB)
    for (x in 0 until imageWidth) {
        for (y in 0 until imageHeight) {
            val r = clamp(x / (imageWidth - 1).toFloat())
            val g = clamp(y / (imageHeight - 1).toFloat())
            val b = 0
            val color = (r shl 16) or (g shl 8) or b
            image.setRGB(x, y, color)
        }
    }

    ImageIO.write(image, "PNG", File("images/chapter02.png"))
}
