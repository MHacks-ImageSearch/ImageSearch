package imagesearch.imageingestor.index

import imagesearch.imageingestor._
import imagesearch.utilities.Ingestor
import java.awt.image.BufferedImage
import imagesearch.imageingestor.imageutils._
import imagesearch.imageingestor.ImageToken

/**
 * Created with IntelliJ IDEA.
 * User: brendan
 * Date: 9/21/13
 * Time: 12:45 AM
 * To change this template use File | Settings | File Templates.
 */
class ImageIngestor extends Ingestor[BufferedImage, ImageToken] {

  override def ingest(image: BufferedImage): ImageToken = {
    val fullHistogram = imageutils.histogram(image)
    val smoothnessValue = computeSmoothnessValue(image)
    val segmentationMasks = separateMaskByRegion(segmentionMask(image, fullHistogram))

    def histogram(mask: ImageMask) = imageutils.histogram(image, mask)
    val size = image.getWidth * image.getHeight
    return ImageToken(fullHistogram.normalize(size), smoothnessValue, segmentationMasks.map(histogram).map(_.normalize(size)))
  }
}