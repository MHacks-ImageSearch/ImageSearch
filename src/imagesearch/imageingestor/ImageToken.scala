package imagesearch.imageingestor

import imagesearch.utilities.Token
import java.awt.image.BufferedImage
import scala.collection.mutable
import imagesearch.imageingestor.ImageMask.Kernel

/**
 * Created with IntelliJ IDEA.
 * User: brendan
 * Date: 9/21/13
 * Time: 1:11 AM
 * To change this template use File | Settings | File Templates.
 */
case class ImageToken(val dim: (Int, Int), val fullHistogram: RGBHistogram, val segmentedHistograms: List[RGBHistogram]) extends Token

case class RGBHistogram(val red: Array[Int], val green: Array[Int], val blue: Array[Int])

case class Range(val left: Int, val peak: Int, val right: Int)

package object imageutils {
  type Histogram = Array[Int]
  type NormalizeHistogram = Array[Float]

  class Pixel(val value: Int) {
    def in(range: Range): Boolean = value >= range.left && value <= range.right
  }

  implicit def toPixel(value: Int) = new Pixel(value)

  def histogram(image: BufferedImage): RGBHistogram = {
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
    RGBHistogram(red, green, blue)
  }

  def histogram(image: BufferedImage, mask: ImageMask): RGBHistogram = {
    val red = new Array[Int](256)
    val green = new Array[Int](256)
    val blue = new Array[Int](256)
    for(y <- 0 until image.getHeight) {
      for(x <- 0 until image.getWidth) {
        if(mask(x, y)) {
          val pixel = image.getRGB(x, y)
          red(pixel.toByte) += 1
          green((pixel >> 8).toByte)
          blue((pixel >> 16).toByte)
        }
      }
    }
    RGBHistogram(red, green, blue)
  }

  def segmentionMask(image: BufferedImage, fullHistogram: RGBHistogram): ImageMask = {
    val (redChannel, greenChannel, blueChannel) = SingleChannelImage(image)
    val (redHistogram, greenHistogram, blueHistogram) = fullHistogram
    val redRanges = getTopPeakRanges(redHistogram)
    val greenRanges = getTopPeakRanges(greenHistogram)
    val blueRanges = getTopPeakRanges(blueHistogram)

    close(threshold(redChannel, redRanges) || threshold(greenChannel, greenRanges) || threshold(blueChannel, blueRanges))
  }

  def separateMaskByRegion(image: ImageMask): List[ImageMask] = {
    val maskCopy = new ImageMask(image)
    val maskList = mutable.MutableList[Option[ImageMask]]()
    val sizeThreshold = 0.2 // We only want to keep the region if it occupies more than 20% of the image
    val width = image.width
    val height = image.height

    def exploreRegion(image: ImageMask, start: (Int, Int)): Option[ImageMask] = {
      val width = image.width
      val height = image.height
      val newMask = new ImageMask(width, height)
      val toVisit = new mutable.Queue[(Int, Int)]() += start
      var count = 0
      while(!toVisit.isEmpty) {
        val (x, y) = toVisit.dequeue
        newMask(x, y) = true
        if(x - 1 >= 0 && image(x - 1, y)) {
          toVisit += (x - 1, y)
          image(x - 1, y) = false
        }
        if(y - 1 >= 0 && image(x, y - 1)) {
          toVisit += (x, y - 1)
          image(x, y - 1) = false
        }
        if(x + 1 < width && image(x + 1, y)) {
          toVisit += (x + 1, y)
          image(x + 1, y) = false
        }
        if(y + 1 < height && image(x, y + 1)) {
          toVisit += (x, y + 1)
          image(x, y + 1) = false
        }
        count += 1
      }
      if(count / (width * height) < sizeThreshold) {
        return None
      } else {
        return Option(newMask)
      }
    }

    for(y <- 0 until height) {
      for(x <- 0 until width) {
        if(maskCopy(x, y)) {
          maskList += exploreRegion(maskCopy, (x, y)) // This modifies maskCopy.
        }
      }
    }
    return maskList.flatten.toList
  }

  def close(image: ImageMask): ImageMask = {
    var newImage = image
    val kernel = new Kernel(5, 5, Array[Boolean](
      false, true, true, true, false,
      true,  true, true, true, true,
      true,  true, true, true, true,
      true,  true, true, true, true,
      false, true, true, true, false))

    for(i <- 0 until 5) {
      newImage = newImage.dilate(kernel)
    }
    for(i <- 0 until 5) {
      newImage = newImage.erode(kernel)
    }
    return newImage
  }

  def threshold(image: SingleChannelImage, ranges: List[Range]): ImageMask = {
    val head = ranges.head
    val tail = ranges.tail
    def threshold(range: Range): ImageMask = this.threshold(image, range)

    tail.foldLeft[ImageMask](threshold(head)) {
      case (mask, range) => mask || threshold(range)
    }
  }

  def threshold(image: SingleChannelImage, range: Range): ImageMask = {
    val width = image.width
    val height = image.height
    val mask = new ImageMask(width, height)

    for(y <- 0 until height) {
      for(x <- 0 until width) {
        mask(x, y) = image(x, y) in range
      }
    }
    mask
  }

  def getTopPeakRanges(histogram: Histogram, numberOfPeaks: Int = 3): List[Range] = {
    val rangeList = mutable.MutableList[Range]()
    val histogramCopy = new Histogram(histogram.length)
    System.arraycopy(histogram, 0, histogramCopy, 0, histogram.length)
    for(peakNumber <- 0 until numberOfPeaks) {
      val range: Range = getPeakRange(histogram)
      rangeList += range
      // We want to find the next peak, so remove this one.
      for(index <- range.left until range.right) {
        histogramCopy(index) = 0
      }
    }
    rangeList.toList
  }

  def getPeakRange(histogram: Histogram): Range = {
    var peak = 0
    for(index <- 0 until histogram.length) {
      if(histogram(index) > peak) {
        peak = index
      }
    }

    var index = peak - 1
    var previousValue = histogram(peak)
    while(index >= 0 && previousValue <= histogram(index)) {
      previousValue = histogram(index)
      index -= 1
    }
    val leftTrough = index + 1

    index = peak + 1
    previousValue = histogram(peak)
    while(index < histogram.length && previousValue >= histogram(index)) {
      previousValue = histogram(index)
      index += 1
    }
    val rightTrough = index - 1

    Range(leftTrough, peak, rightTrough)
  }
}