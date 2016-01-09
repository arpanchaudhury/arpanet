package api.services

import java.io.File

import api.services.helpers.{MongoConnectionApi, QueryBuilder}
import com.google.inject.{Inject, Singleton}
import play.api.Logger
import play.api.libs.concurrent.Execution.Implicits._
import reactivemongo.api.collections.bson.BSONCollection
import reactivemongo.bson.BSONDocument

import scala.async.Async._
import scala.concurrent.Future
import scala.util.{Failure, Success}

@Singleton
class ImageService @Inject()(mongoConnectionApi: MongoConnectionApi, queryBuilder: QueryBuilder) {
  private val logger = Logger(this.getClass)

  private implicit lazy val imageDatabaseF = mongoConnectionApi.getDatabase("arpa(n)2et")

  private val publicImagesCollectionName = "public_images"
  private val photographyCollectionName = "photography"

  private lazy val publicImagesCollectionF = mongoConnectionApi.getCollection(publicImagesCollectionName)
  private lazy val photographCollectionF = mongoConnectionApi.getCollection(photographyCollectionName)

  val getPublicImage = getImageHelper(publicImagesCollectionName, publicImagesCollectionF) _

  val getPhotograph = getImageHelper(photographyCollectionName, photographCollectionF) _

  private def getImageHelper(collectionName: String, collectionF: Future[BSONCollection])(imageId: String) = async {
    val eventualImageFile = await(collectionF).find(queryBuilder.findByIdQuery(imageId)).one[BSONDocument].map {
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
    await(eventualImageFile)
  }

  private def urlFieldNotFoundErrorMessage(collectionName: String, imageId: String): String = {
    s"Error: url field not available for document in $collectionName with ID : $imageId"
  }

  private def noImageFoundErrorMessage(collectionName: String, imageId: String): String = {
    s"Error: No image found in $collectionName with ID : $imageId"
  }
}
