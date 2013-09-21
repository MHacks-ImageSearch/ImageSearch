package imagesearch.textingestor

import org.jsoup.nodes.Document
import imagesearch.utilities.Ingestor

/**
 * Created with IntelliJ IDEA.
 * User: brendan
 * Date: 9/21/13
 * Time: 2:25 AM
 * To change this template use File | Settings | File Templates.
 */
class HeadIngestor extends Ingestor[Document, DocumentBundle] {
  override def ingest(doc: Document): DocumentBundle = {

  }
}