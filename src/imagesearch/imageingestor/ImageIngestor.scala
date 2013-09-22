package imagesearch.imageingestor.index

import imagesearch.imageingestor._
import java.awt.Image
import imagesearch.utilities.Ingestor
import java.awt.image.BufferedImage
import imagesearch.imageingestor.imageutils._
import scala.collection.mutable
import imagesearch.imageingestor.RGBHistogram
import imagesearch.imageingestor.Range
import imagesearch.imageingestor.ImageToken

/**
 * Created with IntelliJ IDEA.
 * User: brendan
 * Date: 9/21/13
 * Time: 12:45 AM
 * To change this template use File | Settings | File Templates.
 */
class ImageIngestor extends Ingestor[BufferedImage, ImageToken] {

  override def ingest(image: BufferedImage) = {
    val fullHistogram = imageutils.histogram(image)
    val segmentationMasks = separateMaskByRegion(segmentionMask(image, fullHistogram))

    def histogram(mask: ImageMask) = imageutils.histogram(image, mask)
    ImageToken((image.getWidth, image.getHeight), fullHistogram, segmentationMasks.map(histogram))
  }
}