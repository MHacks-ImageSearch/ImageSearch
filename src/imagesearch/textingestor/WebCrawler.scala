package imagesearch.textingestor

import java.net.URL
import scala.actors.Future
import org.jsoup.nodes.Document
import org.jsoup.Jsoup
import scala.collection.parallel.mutable

/**
 * Created with IntelliJ IDEA.
 * User: brendan
 * Date: 9/21/13
 * Time: 3:31 AM
 * To change this template use File | Settings | File Templates.
 */
class WebCrawler {
  private val headIngestor = new HeadIngestor
  private val finalIngestor = new HeadIngestor
  private var documentQueue: mutable.Set[Future[Document]]
  private var urlsToSearch: Set[String]

  def crawl() {
    documentQueue = urlsToSearch.map{ url => future(Jsoup.connect(url).get()) }
    for(documentFuture: Future[Document] <- documentQueue if documentFuture.isSet) {
    }
  }
}
