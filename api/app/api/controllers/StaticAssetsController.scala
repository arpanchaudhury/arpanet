package api.controllers

import api.services.{PdfService, ImageService}
import com.google.inject.{Inject, Singleton}
import play.api.cache.CacheApi
import play.api.libs.concurrent.Execution.Implicits._
import play.api.libs.iteratee.Enumerator
import play.api.mvc.{Result, ResponseHeader, Action, Controller}

import scala.async.Async._
import scala.concurrent.Await
import scala.concurrent.duration._
import scala.language.postfixOps

@Singleton
class StaticAssetsController @Inject()(cache: CacheApi,
                                       imageService: ImageService,
                                       pdfService: PdfService) extends Controller {
  def getImage(imageType: String, imageId: String) = Action.async {
    implicit request =>
      async {
        imageType match {
          case "public-image" =>
            Ok.sendFile(
              cache.getOrElse(s"$imageType-$imageId") {
                // async - await does not help here (by-name argument)
                val image = Await.result(imageService.getPublicImage(imageId), 10.seconds)
                cache.set(s"$imageType-$imageId", image, 6.hours)
                image
              }
            ).as("image/jpeg")
          case "photography" =>
            Ok.sendFile(
              cache.getOrElse(s"$imageType-$imageId") {
                // async - await does not help here (by-name argument)
                val image = Await.result(imageService.getPhotograph(imageId), 10.seconds)
                cache.set(s"$imageType-$imageId", image, 6.hours)
                image
              }
            ).as("image/png")
        }
      }
  }

  def getResume = Action.async {
    implicit request =>
      pdfService.getDocument("resume").map {
        resume => Result(
          header = ResponseHeader(200, Map(CONTENT_LENGTH -> resume.length.toString, CONTENT_TYPE -> "application/pdf")),
          body = Enumerator.fromFile(resume)
        )
      }
  }
}
