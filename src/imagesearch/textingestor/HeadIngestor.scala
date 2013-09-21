package imagesearch.textingestor

import org.jsoup.nodes.Document
import imagesearch.utilities.Ingestor
import com.mongodb.DBCollection
import com.mongodb.casbah.commons.MongoDBObject

/**
 * Created with IntelliJ IDEA.
 * User: brendan
 * Date: 9/21/13
 * Time: 2:25 AM
 * To change this template use File | Settings | File Templates.
 */
class HeadIngestor(private val unvisitedCol: DBCollection) extends Ingestor[Document, DocumentBundle] {
  override def ingest(doc: Document): DocumentBundle = {

  }

  def addUnvisitedURLs(urls: List[String]) {
    urls.map(url => MongoDBObject("url" -> url, "visited" -> false)).foldLeft(unvisitedCol) { case(col, obj) => col.insert(obj) }
  }
}