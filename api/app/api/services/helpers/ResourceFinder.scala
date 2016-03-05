package api.services.helpers

import java.io.{File, FileOutputStream}
import java.net.URI

import com.google.inject.{Inject, Singleton}
import play.api.Logger
import play.api.libs.concurrent.Execution.Implicits._
import play.api.libs.iteratee.Iteratee
import play.api.libs.ws.WSClient

import scala.async.Async._

@Singleton
class ResourceFinder @Inject()(ws: WSClient) {
  private val logger = Logger(this.getClass)

  def find(resourcePath: String, extension: String) = async {
    val resourceUri = new URI(resourcePath)
    val uriScheme = resourceUri.getScheme
    val eventualFile = uriScheme match {
      case "file" => findLocalFile(resourceUri)
      case "http" | "https" => findRemoteFile(resourceUri, extension)
      case _ =>
        logger.error(s"Error: URI scheme $uriScheme not found")
        sys.error(s"Error: URI scheme $uriScheme not found")
    }
    await(eventualFile)
  }

  private def findRemoteFile(resourceUri: URI, extension: String) = async {
    val eventualResponse = ws.url(resourceUri.toString).getStream()
    val eventualFile = eventualResponse.flatMap {
      case (headers, source) =>
        val desiredFile = new File(s"file.$extension")
        val outputStream = new FileOutputStream(desiredFile)
        val sink = Iteratee.foreach[Array[Byte]] { bytes => outputStream.write(bytes) }
        (source |>>> sink).andThen {
          case result =>
            outputStream.close()
            result.get
        }.map(_ => desiredFile)
    } transform (identity, e => {
      logger.error(s"Error: file $resourceUri not found")
      sys.error(s"Error: file $resourceUri not found")
    })
    await(eventualFile)
  }

  private def findLocalFile(fileUri: URI) = async {
    val file = new File(fileUri)
    if (file.exists) file
    else {
      logger.error(s"Error: file ${file.getPath} not found")
      sys.error(s"Error: file ${file.getPath} not found")
    }
  }
}
