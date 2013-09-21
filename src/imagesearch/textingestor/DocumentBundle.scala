package imagesearch.textingestor

import java.awt.Image
import org.jsoup.nodes.Document

/**
 * Created with IntelliJ IDEA.
 * User: brendan
 * Date: 9/21/13
 * Time: 2:59 AM
 * To change this template use File | Settings | File Templates.
 */
case class DocumentBundle(val document: Document, val images: List[Image])
