package api.controllers

import api.services.BlogPostService
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
class BlogPostController @Inject()(cache: CacheApi, blogPostService: BlogPostService) extends Controller {
  def getWriteUp(id: String) = Action.async {
    implicit request =>
      async {
        val writeUp = await(blogPostService.getWriteUp(id))
        Ok(writeUp)
      }
  }

  def getMarkdown(id: String) = Action.async {
    implicit request =>
      async {
        Ok.sendFile(
          cache.getOrElse(s"writeup-$id-markdown") {
            val markdown = Await.result(blogPostService.getMarkdown(id), 10.seconds)
            cache.set(s"writeup-$id-markdown", markdown, 6.hours)
            markdown
          }
        )
      }
  }

  def getWriteUps(pageStart: Int, pageLength: Int, topics: List[String]) = Action.async {
    implicit request =>
      async {
        val writeUpsCount = await(blogPostService.getWriteUpsCount(topics))
        val writeUpDetails = await(blogPostService.getWriteUps(pageStart, pageLength, topics))
        Ok(Json.obj("count" -> writeUpsCount, "writeUps" -> writeUpDetails))
      }
  }

  def getTopics = Action.async {
    implicit request =>
      async {
        val topics = await(blogPostService.getTopics)
        Ok(topics)
      }
  }
}
