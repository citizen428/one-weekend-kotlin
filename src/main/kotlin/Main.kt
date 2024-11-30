package net.citizen428

import java.awt.image.BufferedImage
import java.io.File
import javax.imageio.ImageIO

const val imageWidth: Int = 256
const val imageHeight: Int = 256


fun main() {
    val image = BufferedImage(imageWidth, imageHeight, BufferedImage.TYPE_INT_RGB)
    for (x in 0 until imageWidth) {
        for (y in 0 until imageHeight) {
            val pixelColor = Color(
                x.toDouble() / (imageWidth - 1),
                y.toDouble() / (imageHeight - 1),
                0.0
            )
            image.setRGB(x, y, pixelColor.toInt())
        }
    }

    ImageIO.write(image, "PNG", File("images/chapter02.png"))
}
