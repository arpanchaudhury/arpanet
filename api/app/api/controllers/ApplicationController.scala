package api.controllers

import com.google.inject.{Inject, Singleton}
import play.api.cache.CacheApi
import play.api.libs.concurrent.Execution.Implicits._
import play.api.mvc.{Action, Controller}

import scala.async.Async._
import scala.concurrent.duration._
import scala.io.Source

@Singleton
class ApplicationController @Inject()(cache: CacheApi) extends Controller {
  private val ApiDocumentationCacheKey = "api-documentation"
  private val ApiDocumentationPath = "resources/api-documentation.txt"

  def home = Action.async {
    async {
      val responseContent = cache.getOrElse(ApiDocumentationCacheKey) {
        val documentation = Source.fromFile(ApiDocumentationPath).mkString
        cache.set(ApiDocumentationCacheKey, documentation, 12.hours)
        documentation
      }
      Ok(responseContent).as("text/plain")
    }
  }

  def other(whatever: String) = Action.async {
    async {
      val responseContent = cache.getOrElse(ApiDocumentationCacheKey) {
        val documentation = Source.fromFile(ApiDocumentationPath).mkString
        cache.set(ApiDocumentationCacheKey, documentation, 12.hours)
        documentation
      }
      BadRequest(
        s"""Route not available '/$whatever'
            |
            |
            |$responseContent
            |""".stripMargin
      ).as("text/plain")
    }
  }
}
