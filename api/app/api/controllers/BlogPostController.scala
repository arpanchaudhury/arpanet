package api.controllers

import api.services.BlogPostService
import com.google.inject.{Inject, Singleton}
import play.api.cache.CacheApi
import play.api.libs.concurrent.Execution.Implicits._
import play.api.mvc.{Action, Controller}

import scala.async.Async._
import scala.language.postfixOps

@Singleton
class BlogPostController @Inject()(cache: CacheApi, blogPostService: BlogPostService) extends Controller {
  def getWriteUps(pageStart: Int, pageLength: Int, topics: List[String]) = Action.async {
    implicit request =>
      async {
        val writeUpDetails = await(blogPostService.getWriteUps(pageStart, pageLength, topics))
        Ok(writeUpDetails)
      }
  }
}
