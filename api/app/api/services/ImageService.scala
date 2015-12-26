package api.services

import com.google.inject.{Inject, Singleton}
import play.api.libs.concurrent.Execution.Implicits._

import scala.async.Async._

@Singleton
class ImageService @Inject() (databaseService: DatabaseService) {
  def getPublicImages(imageId: String) = async {
    await(databaseService.getPublicImage(imageId))
  }

  def getPhotograph(imageId: String) = async {
    await(databaseService.getPhotograph(imageId))
  }
}
