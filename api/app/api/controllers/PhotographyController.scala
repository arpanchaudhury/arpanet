package api.controllers

import api.services.ImageService
import api.utils.Dimensions
import com.google.inject.{Inject, Singleton}
import play.api.cache.CacheApi
import play.api.libs.concurrent.Execution.Implicits._
import play.api.libs.json.Json
import play.api.mvc.{Action, Controller}

import scala.async.Async._
import scala.concurrent.Await
import scala.concurrent.duration._
import scala.language.postfixOps

@Singleton
class PhotographyController @Inject()(cache: CacheApi,
                                      imageService: ImageService) extends Controller {
  def getImage(imageId: String, dimensions: Option[Dimensions]) = Action.async {
    implicit request =>
      async {
        Ok.sendFile(
          cache.getOrElse(s"photography-$imageId-${dimensions.getOrElse("default")}") {
            val image = Await.result(imageService.getPhotograph(imageId, dimensions), 20.seconds)
            cache.set(s"photography-$imageId-${dimensions.getOrElse("default")}", image, 6.hours)
            image
          }
        ).as("image/png")
      }
  }

  def getImageDetails(pageStart: Int, pageLength: Int, tags: List[String]) = Action.async {
    implicit request =>
      async {
        val imageDetails = await(imageService.getPhotographDetails(pageStart, pageLength, tags))
        val imagesCount = await(imageService.getPhotographsCount(tags))
        Ok(Json.obj("count" -> imagesCount, "photographs" -> imageDetails))
      }
  }
}
