package api.controllers

import api.models.{Failure, IngestionStatus, Success}
import api.services.IngestionService
import com.google.inject.{Inject, Singleton}
import play.api.libs.concurrent.Execution.Implicits._
import play.api.libs.json.JsObject
import play.api.mvc.{Action, Controller}

import scala.async.Async._

@Singleton
class IngestionController @Inject()(ingestionService: IngestionService) extends Controller {

  def ingest = Action.async(parse.json) { request =>
    async {
      val data = request.body.as[JsObject]
      val ingestionStatuses = await(ingestionService.ingest(data))
      Ok(generateStatusReport(ingestionStatuses))
    }
  }

  private def generateStatusReport(ingestionStatuses: List[IngestionStatus]): String = {
    val (successes, failures) = (ingestionStatuses.filter(_.status == Success), ingestionStatuses.filter(_.status == Failure))
    s"""Success Count:    ${successes.size}
       |Failure Count:    ${failures.size}
       |Failed Documents: {
       |  ${failures.map(failure => s"${failure.documentId} in ${failure.documentCollection} collection").mkString(",\n  ")}
       |}""".stripMargin
  }
}
