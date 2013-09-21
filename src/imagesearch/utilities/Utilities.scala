package imagesearch.utilities

/**
 * Created with IntelliJ IDEA.
 * User: brendan
 * Date: 9/21/13
 * Time: 1:02 AM
 * To change this template use File | Settings | File Templates.
 */
class Utilities {

}

abstract class Ingestor[From, To] {
  abstract def ingest(from: From): To
}