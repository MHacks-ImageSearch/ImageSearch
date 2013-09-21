package imagesearch.utilities

import com.mongodb.MongoClient

/**
 * Created with IntelliJ IDEA.
 * User: brendan
 * Date: 9/21/13
 * Time: 1:02 AM
 * To change this template use File | Settings | File Templates.
 */
object Utilities {
  def getClient = MongoClient("localhost", 27017)

}

abstract class Ingestor[From, To] {
  def ingest(from: From): To
}