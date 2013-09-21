package imagesearch.textingestor

import imagesearch.utilities.Ingestor
import imagesearch.imageingestor.index.ImageIngestor
import java.awt.Image
import imagesearch.imageingestor.ImageToken
import org.jsoup.nodes.{Element, Node, Document}
import org.jsoup.select.NodeVisitor
import scala.collection.mutable

/**
 * Created with IntelliJ IDEA.
 * User: brendan
 * Date: 9/21/13
 * Time: 3:05 AM
 * To change this template use File | Settings | File Templates.
 */
class FinalIngestor extends Ingestor[DocumentBundle, TokenCluster] {
  private val imageIngestor = new ImageIngestor

  override def ingest(documentBundle: DocumentBundle): TokenCluster = {
    val imageTokenMap = documentBundle.images.map(image => (image, imageIngestor.ingest(image))).toMap[Image, List[ImageToken]]
    val documentTokens = digest(documentBundle.document)
    TokenCluster(documentTokens, imageTokenMap)
  }

  def digest(document: Document): Map[String, Int] = {
    val elementVisitor = new ElementVisitor
    document.traverse(elementVisitor)
    histogram(elementVisitor.textList)
  }

  def histogram(words: List[String]): mutable.Map[String, Int] = {
    val map = mutable.Map[String, Int]()
    words.foreach {
      if(map.contains(_)) {
        map(_) += 1
      } else {
        map(_) = 1
      }
    }
  }

  private class ElementVisitor extends NodeVisitor {
    // TODO: There is probably a official set of whitespace or something.
    private val whiteSpaceChars = Array(' ', '\t', '\n', '\v', '\r')
    val textList = mutable.List[String]()

    override def head(node: Node, depth: Int) {
      node match {
        case element: Element => if(element.tag == element.hasText) {
          val newText = element.ownText.split(whiteSpaceChars)
          textList ++= newText
        }
      }
    }

    override def tail(node: Node, depth: Int)
  }
}