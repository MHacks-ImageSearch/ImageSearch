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
  	var out: OutputStream = null;
  	var in: InputStream = null;
  	for each doc.getElementsMatchingText("<img "){ //maybe src='instead
  		try{
  			val url = URL(doc.getgetElementsMatchingText("<img").after)
  			val connection = url.openConnection().asInstanceOf[HttpURLConnection]
     		connection.setRequestMethod("GET")
      		in = connection.getInputStream
      		//need to save image in data structure
     		out = new BufferedOutputStream(new FileOutputStream(localfile))
      		val byteArray = Stream.continually(in.read).takeWhile(-1 !=).map(_.toByte).toArray

      		out.write(byteArray)
    		} catch {
      			case e: Exception => println(e.printStackTrace()) 
    		} finally {
      			out.close
      			in.close
      		}
      	}

  	for each doc.getElementsMatchingText("<link"){
  		/*add the link to the databasE

  		*/

  	}

  	doc.getElementsMatchingText("<nav"){
  		/*add the link to the database

  		*/
  	}

  }
}