package api.services.helpers

import java.io.File

import com.google.inject.Singleton
import play.api.Logger

@Singleton
class FileFinder {
  private val logger = Logger(this.getClass)

  def find(filePath: String) = {
    val file = new File(filePath)
    if (file.exists) file
    else {
      logger.error(s"Error: file ${file.getPath} not found")
      sys.error(s"Error: file ${file.getPath} not found")
    }
  }
}
