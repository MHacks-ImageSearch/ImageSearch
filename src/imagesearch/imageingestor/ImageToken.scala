package imagesearch.imageingestor

import imagesearch.utilities.Token

/**
 * Created with IntelliJ IDEA.
 * User: brendan
 * Date: 9/21/13
 * Time: 1:11 AM
 * To change this template use File | Settings | File Templates.
 */
case class ImageToken(val dim: (Int, Int), val totalHistogram: (Array[Int], Array[Int], Array[Int])) extends Token