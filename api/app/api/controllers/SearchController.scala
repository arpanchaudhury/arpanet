package api.controllers

import api.services.SearchService
import com.google.inject.{Inject, Singleton}
import play.api.libs.concurrent.Execution.Implicits._
import play.api.mvc.{Action, Controller}

import scala.async.Async._

@Singleton
class SearchController @Inject()(searchService: SearchService) extends Controller {

  def search(searchTerm: String) = Action.async {
    implicit request =>
      async {
        Ok(await(searchService.search(searchTerm)))
      }
  }
}