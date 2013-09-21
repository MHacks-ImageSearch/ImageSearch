package imagesearch.textingestor

import org.jsoup.nodes.{Element, Document}
import imagesearch.utilities.Ingestor
import com.mongodb.DBCollection
import com.mongodb.casbah.commons.MongoDBObject
import java.io.{InputStream, OutputStream, FileOutputStream, BufferedOutputStream}
import java.net.HttpURLConnection
import java.awt.image.BufferedImage
import javax.imageio.ImageIO

/**
 * Created with IntelliJ IDEA.
 * User: brendan
 * Date: 9/21/13
 * Time: 2:25 AM
 * To change this template use File | Settings | File Templates.
 */
class HeadIngestor(private val unvisitedCol: DBCollection) extends Ingestor[Document, DocumentBundle] {
  override def ingest(doc: Document): DocumentBundle = {

    for(element: Element <- doc.select("img")) {
      try {
        val url = URL(element.absUrl("src"))
        val connection = url.openConnection().asInstanceOf[HttpURLConnection]
        connection.setRequestMethod("GET")
        val image = ImageIO.read(connection.getInputStream)
      } catch {
        case _: Throwable => println(_.printStackTrace())
      } finally {
        out.close
        in.close
      }
    }

    addUnvisitedURLs(doc.select("link"))
    addUnvisitedURLs(doc.select("nav"))
  }

  def addUnvisitedURLs(urls: List[String]) {
    urls.map(url => MongoDBObject("url" -> url, "visited" -> false)).foldLeft(unvisitedCol) { case(col, obj) => col.insert(obj) }
  }
}