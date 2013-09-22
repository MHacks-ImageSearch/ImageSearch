package imagesearch.imageingestor

import imagesearch.imageingestor.ImageMask.Kernel

/**
 * Created with IntelliJ IDEA.
 * User: brendan
 * Date: 9/21/13
 * Time: 5:13 PM
 * To change this template use File | Settings | File Templates.
 */
class ImageMask(val width: Int, val height: Int) {
  type Histogram = Array[Int]
  type KernelOp = (Boolean, Boolean) => Boolean
  type ConvolutionOperation = (Kernel, Int, Int) => Boolean

  private val raster = new Array[Boolean](width * height)

  def this(image: ImageMask) {
    this(image.width, image.height)
    System.arraycopy(image.raster, 0, raster, 0, raster.length)
  }

  def apply(x: Int, y: Int): Boolean = get(x, y)

  def update(x: Int, y: Int, value: Boolean) {
    set(x, y, value)
  }

  def convolve(kernel: Kernel, operation: ConvolutionOperation): ImageMask = {
    val newMask = new ImageMask(width, height)
    val (xBoarder, yBoarder) = kernel.center
    for(y <- yBoarder until (height - yBoarder)) { // So that the kernel is in the image.
      for(x <- xBoarder until (width - xBoarder)) {
        newMask(x, y) = operation(kernel, x, y)
      }
    }
    return newMask
  }

  private def morphologicalOperation(kernel: Kernel, operation: KernelOp, x: Int, y: Int): Boolean = {
    var total = false
    var (xShift, yShift) = kernel.center
    for(y <- 0 until kernel.height) {
      for(x <- 0 until kernel.width) {
        if(kernel(x, y)) {
          val value = get(x - xShift, y - yShift)
          if(y == 0 && x == 0) {
            total = value
          } else {
            total = operation(total, value)
          }
        }
      }
    }
    return total
  }

  def dilationConvolutionOperation(kernel: Kernel, x: Int, y: Int): Boolean = morphologicalOperation(kernel, (left, right) => left || right, x, y)

  def erosionConvolutionOperation(kernel: Kernel, x: Int, y: Int): Boolean = morphologicalOperation(kernel, (left, right) => left && right, x, y)

  def dilate(kernel: Kernel): ImageMask = convolve(kernel, dilationConvolutionOperation)

  def erode(kernel: Kernel): ImageMask = convolve(kernel, erosionConvolutionOperation)

  def &&(mask: ImageMask): ImageMask = {
    val newMask = new ImageMask(width, height)
    for(y <- 0 until height) {
      for(x <- 0 until width) {
        newMask(x, y) = get(x, y) && mask(x, y)
      }
    }
    return newMask
  }

  def ||(mask: ImageMask): ImageMask = {
    val newMask = new ImageMask(width, height)
    for(y <- 0 until height) {
      for(x <- 0 until width) {
        newMask(x, y) = get(x, y) || mask(x, y)
      }
    }
    return newMask
  }

  def get(x: Int, y: Int): Boolean = raster(x + width * height)

  def set(x: Int, y: Int, value: Boolean) { raster(x + width * height) = value }
}

object ImageMask {
  class Kernel(val width: Int, val height: Int, private val raster: Array[Boolean]) {
    val center = (width / 2, height / 2)
    def apply(x: Int, y: Int): Boolean = get(x, y)

    def update(x: Int, y: Int, value: Boolean) {
      set(x, y, value)
    }

    def get(x: Int, y: Int): Boolean = raster(x + width * height)

    def set(x: Int, y: Int, value: Boolean) { raster(x + width * height) = value }
  }
}
