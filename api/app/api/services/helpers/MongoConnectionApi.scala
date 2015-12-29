package api.services.helpers

import api.services.PropertyService
import com.google.inject.{Inject, Singleton}
import play.api.Logger
import play.api.inject.ApplicationLifecycle
import play.api.libs.concurrent.Execution.Implicits._
import reactivemongo.api.DefaultDB
import reactivemongo.api.collections.bson.BSONCollection

import scala.async.Async._
import scala.concurrent.Future
import scala.concurrent.duration._

@Singleton
class MongoConnectionApi @Inject()(lifecycle: ApplicationLifecycle, propertyService: PropertyService) {
  private val logger = Logger(this.getClass)

  private lazy val driver = new reactivemongo.api.MongoDriver
  private lazy val databaseUris = propertyService.mongoServers
  private lazy val connection = driver.connection(databaseUris)

  lifecycle.addStopHook(() => async {
    await(connection.askClose()(5.seconds))
    logger.info("Connection to database stopped...")
  })

  def getDatabase(databaseName: String) = connection.database(databaseName)

  def getCollection(collectionName: String)(implicit database: Future[DefaultDB]) = {
    database.map(_.collection[BSONCollection](collectionName))
  }
}
