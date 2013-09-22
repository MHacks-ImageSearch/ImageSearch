package imagesearch.searchengine

import imagesearch.imageingestor.NormalizedRGBHistogram
import imagesearch.imageingestor.imageutils.NormalizedHistogram
import Math.abs

/**
 * Created with IntelliJ IDEA.
 * User: brendan
 * Date: 9/21/13
 * Time: 12:53 AM
 * To change this template use File | Settings | File Templates.
 */
class SearchEngine {

}

object SearchEngine {
  def compareHistograms(leftHist: NormalizedHistogram, rightHist: NormalizedHistogram): Float = {
    leftHist.zip(rightHist).map{ case(left, right) => abs(left - right) }.reduce{ case(left, right) => left + right }
  }

  def compareHistograms(leftHist: NormalizedRGBHistogram, rightHist: NormalizedRGBHistogram): Float = {
    val (leftRed: NormalizedHistogram, leftGreen: NormalizedHistogram, leftBlue: NormalizedHistogram) = leftHist
    val (rightRed: NormalizedHistogram, rightGreen: NormalizedHistogram, rightBlue: NormalizedHistogram) = rightHist
    return (compareHistograms(leftRed, rightRed) + compareHistograms(leftGreen, rightGreen) + compareHistograms(leftBlue, rightBlue)) / 3
  }

  def maxIndex(hist: NormalizedHistogram): Int = {
    var max = 0
    for(index <- 0 until hist.length) {
      if(hist(index) > hist(max)) {
        max = index
      }
    }
    return max
  }
}
