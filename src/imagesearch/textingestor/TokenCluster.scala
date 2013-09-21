package imagesearch.textingestor

import imagesearch.imageingestor.ImageToken

/**
 * Created with IntelliJ IDEA.
 * User: brendan
 * Date: 9/21/13
 * Time: 2:53 AM
 * To change this template use File | Settings | File Templates.
 */
case class TokenCluster(val textTokens: List[TextToken], val imageToken: List[ImageToken])