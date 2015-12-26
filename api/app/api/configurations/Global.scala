package api.configurations

import play.api.libs.concurrent.Execution.Implicits._
import play.api.mvc.{RequestHeader, Results}
import play.api.{Application, GlobalSettings, Logger}

import scala.concurrent.Future

object Global extends GlobalSettings {

  override def onStart(app: Application) = {
    Logger.info("Application has started...")
    // check if database is connected
  }

  override def onStop(app: Application) = {
    Logger.info("Application is shutting down...")
    // check if database connection is retired
  }

  override def onError(request: RequestHeader, ex: Throwable) = {
    Future(Results.InternalServerError.as("text/html"))
  }
}
