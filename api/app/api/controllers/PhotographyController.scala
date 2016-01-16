package api.controllers

import api.services.ImageService
import com.google.inject.{Inject, Singleton}
import play.api.cache.CacheApi
import play.api.libs.concurrent.Execution.Implicits._
import play.api.mvc.{Action, Controller}

import scala.async.Async._
import scala.concurrent.Await
import scala.concurrent.duration._
import scala.language.postfixOps

@Singleton
class PhotographyController @Inject()(cache: CacheApi,
                                      imageService: ImageService) extends Controller {
  def getImage(imageId: String) = Action.async {
    implicit request =>
      async {
        Ok.sendFile(
          cache.getOrElse(s"photography-$imageId") {
            // async - await does not help here (by-name argument)
            val image = Await.result(imageService.getPhotograph(imageId), 10.seconds)
            cache.set(s"photography-$imageId", image, 6.hours)
            image
          }
        ).as("image/png")
      }
  }

  def getImageDetails = Action.async {
    implicit request =>
      async {
        val pageStart = request.getQueryString("page-start").getOrElse("0").toInt
        val pageLength = request.getQueryString("page-length").getOrElse("9").toInt
        val imageDetails = await(imageService.getPhotographDetails(pageStart, pageLength))
        Ok(imageDetails)
      }
  }
}
