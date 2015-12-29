package api.services

import java.io.File

import com.google.inject.{Inject, Singleton}
import play.api.Logger
import play.api.inject.ApplicationLifecycle
import play.api.libs.concurrent.Execution.Implicits._
import reactivemongo.api.collections.bson.BSONCollection
import reactivemongo.bson.BSONDocument

import scala.async.Async._
import scala.concurrent.Future
import scala.concurrent.duration._
import scala.util.{Failure, Success}

@Singleton
class DatabaseService @Inject()(lifecycle: ApplicationLifecycle, propertyService: PropertyService) {
  private val logger = Logger(this.getClass)

  private lazy val driver = new reactivemongo.api.MongoDriver
  private lazy val databaseUris = propertyService.mongoServers
  private lazy val connection = driver.connection(databaseUris)

  lifecycle.addStopHook(() => async {
    await(connection.askClose()(5.seconds))
    logger.info("Connection to database stopped...")
  })

  private lazy val databaseF = connection.database("arpa(n)2et")

  private val publicImageCollectionName = "public_images"
  private val photographyCollectionName = "photography"
  private lazy val publicImagesCollectionF = databaseF.map(_.collection[BSONCollection](publicImageCollectionName))
  private lazy val photographCollectionF = databaseF.map(_.collection[BSONCollection](photographyCollectionName))

  private def findByIdQuery(id: String) = BSONDocument("_id" -> id)

  def getPublicImage = getImageHelper(publicImageCollectionName, publicImagesCollectionF) _

  def getPhotograph = getImageHelper(photographyCollectionName, photographCollectionF) _

  private def getImageHelper(collectionName: String, collectionF: Future[BSONCollection])(imageId: String) = async {
    val imageFile = await(collectionF).find(findByIdQuery(imageId)).one[BSONDocument].map {
      case Some(document) =>
        val imagePath = document.getAsTry[String]("url")
        imagePath match {
          case Success(path) => new File(path)
          case Failure(exception) =>
            logger.error(urlFieldNotFoundErrorMessage(collectionName, imageId))
            sys.error(urlFieldNotFoundErrorMessage(collectionName, imageId))
        }
      case None =>
        logger.error(noImageFoundErrorMessage(collectionName, imageId))
        sys.error(noImageFoundErrorMessage(collectionName, imageId))
    }
    await(imageFile)
  }

  private def urlFieldNotFoundErrorMessage(collectionName: String, imageId: String): String = {
    s"Error: url field not available for document in $collectionName with ID : $imageId"
  }

  private def noImageFoundErrorMessage(collectionName: String, imageId: String): String = {
    s"Error: No image found in $collectionName with ID : $imageId"
  }
}
