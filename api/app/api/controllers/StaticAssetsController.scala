package api.controllers

import api.services.{DocumentService, ImageService, PdfService}
import com.google.inject.{Inject, Singleton}
import play.api.cache.CacheApi
import play.api.libs.concurrent.Execution.Implicits._
import play.api.libs.iteratee.Enumerator
import play.api.mvc.{Action, Controller, ResponseHeader, Result}

import scala.async.Async._
import scala.concurrent.Await
import scala.concurrent.duration._
import scala.language.postfixOps

@Singleton
class StaticAssetsController @Inject()(cache: CacheApi,
                                       imageService: ImageService,
                                       pdfService: PdfService,
                                       documentService: DocumentService) extends Controller {
  def getImage(imageId: String) = Action.async {
    implicit request =>
      async {
        Ok.sendFile(
          cache.getOrElse(s"public-images-$imageId") {
            val image = Await.result(imageService.getPublicImage(imageId), 10.seconds)
            cache.set(s"public-images-$imageId", image, 6.hours)
            image
          }
        ).as("image/jpeg")
      }
  }

  def getResume = Action.async {
    implicit request =>
      async {
        val resume = cache.getOrElse("resume") {
          val resume = Await.result(pdfService.getDocument("resume"), 30.seconds)
          cache.set("resume", resume, 7.days)
          resume
        }
        Result(
          header = ResponseHeader(200, Map(CONTENT_LENGTH -> resume.length.toString, CONTENT_TYPE -> "application/pdf")),
          body = Enumerator.fromFile(resume)
        )
      }
  }

  def getProfile = Action.async {
    implicit request =>
      async (Ok(await(documentService.getDocument("profile"))))
  }
}
