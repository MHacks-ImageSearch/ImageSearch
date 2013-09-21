package imagesearch.imageingestor.index

import imagesearch.imageingestor.ImageToken
import java.awt.Image
import imagesearch.utilities.Ingestor
import java.awt.image.BufferedImage

/**
 * Created with IntelliJ IDEA.
 * User: brendan
 * Date: 9/21/13
 * Time: 12:45 AM
 * To change this template use File | Settings | File Templates.
 */
class ImageIngestor extends Ingestor[Image, ImageToken] {

  override def ingest(image: BufferedImage) = ImageToken((image.getWidth, image.getHeight), histogram(image))

  def histogram(image: BufferedImage): (Array[Int], Array[Int], Array[Int]) = {
    val red = new Array[Int](256)
    val green = new Array[Int](256)
    val blue = new Array[Int](256)
    for(y <- 0 until image.getHeight) {
      for(x <- 0 until image.getWidth) {
        val pixel = image.getRGB(x, y)
        red(pixel.toByte) += 1
        green((pixel >> 8).toByte)
        blue((pixel >> 16).toByte)
      }
    }
  }
}
