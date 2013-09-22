package imagesearch.imageingestor

import java.awt.image.BufferedImage

/**
 * Created with IntelliJ IDEA.
 * User: brendan
 * Date: 9/21/13
 * Time: 6:57 PM
 * To change this template use File | Settings | File Templates.
 */
class SingleChannelImage(val width: Int, val height: Int, private val raster: Array[Byte]) {
  def apply(x: Int, y: Int): Byte = get(x, y)

  def update(x: Int, y: Int, value: Byte) {
    set(x, y, value)
  }

  def get(x: Int, y: Int): Byte = raster(x + width * height)

  def set(x: Int, y: Int, value: Byte) { raster(x + width * height) = value }
}

object SingleChannelImage {
  def apply(image: BufferedImage): (SingleChannelImage, SingleChannelImage, SingleChannelImage) = {
    val width = image.getWidth
    val height = image.getHeight
    val redBuffer = new Array[Byte](width * height)
    val greenBuffer = new Array[Byte](width * height)
    val blueBuffer = new Array[Byte](width * height)
    for(y <- 0 until height) {
      for(x <- 0 until width) {
        val pixel = image.getRGB(x, y)
        val offset = x + y * width
        redBuffer(offset) = pixel.toByte
        greenBuffer(offset) = (pixel >> 8).toByte
        blueBuffer(offset) = (pixel >> 16).toByte
      }
    }
    (new SingleChannelImage(width, height, redBuffer), new SingleChannelImage(width, height, greenBuffer), new SingleChannelImage(width, height, blueBuffer))
  }
}
