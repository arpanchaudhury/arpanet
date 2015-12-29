package api.configurations

import play.api.libs.concurrent.Execution.Implicits._
import play.api.mvc.{RequestHeader, Results}
import play.api.{Application, GlobalSettings, Logger}

import scala.concurrent.Future

object Global extends GlobalSettings {
  val logger = Logger(this.getClass)

  override def onStart(app: Application) = {
    logger.info("Application has started...")
  }

  override def onStop(app: Application) = {
    logger.info("Application is shutting down...")
  }

  override def onError(request: RequestHeader, ex: Throwable) = {
    logger.error(ex.getMessage)
    Future(Results.NotFound.as("text/html"))
  }
}
