package imagesearch.textingestor

import scala.actors.Future
import org.jsoup.nodes.Document
import org.jsoup.Jsoup
import scala.collection.parallel.mutable
import com.mongodb.casbah.commons.MongoDBObject
import com.mongodb.casbah.commons.MongoCollection
import com.mongodb.MongoClient
import com.mongodb.DB
import com.mongodb.DBCollection
import imagesearch.utilities.Utilities

/**
 * Created with IntelliJ IDEA.
 * User: brendan
 * Date: 9/21/13
 * Time: 3:31 AM
 * To change this template use File | Settings | File Templates.
 */
class WebCrawler {
  private val dbClient = Utilities.getClient
  private val db = dbClient("webcrawler")
  private val urlCollection = db("urls_collection")
  private val headIngestor = new HeadIngestor(urlCollection)
  private val finalIngestor = new HeadIngestor
  private var documentQueue: mutable.Set[Future[Document]]
  private var urlsToSearch: Set[String]

  def crawl() {
    documentQueue = urlsToSearch.map{ url => future(Jsoup.connect(url).get()) }
    for(documentFuture: Future[Document] <- documentQueue if documentFuture.isSet) {
    }
  }
}

object WebCrawler {
  val url = "url"
  val visited = "visited"
}
