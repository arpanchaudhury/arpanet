package api.services

import api.models.ImageDocument
import api.services.helpers.{FileFinder, MongoConnectionApi, QueryBuilder}
import com.google.inject.{Inject, Singleton}
import play.api.Logger
import play.api.libs.concurrent.Execution.Implicits._
import play.api.libs.json.Json
import reactivemongo.api.QueryOpts
import reactivemongo.api.collections.bson.BSONCollection
import reactivemongo.bson.{BSONDocument, Macros}

import scala.async.Async._
import scala.concurrent.Future
import scala.util.{Failure, Success}

@Singleton
class ImageService @Inject()(fileFinder: FileFinder,
                             queryBuilder: QueryBuilder,
                             mongoConnectionApi: MongoConnectionApi) {
  private val logger = Logger(this.getClass)

  private implicit lazy val imageDatabaseF = mongoConnectionApi.getDatabase("arpa(n)2et")

  private val publicImagesCollectionName = "public_images"
  private val photographyCollectionName = "photography"

  private lazy val publicImagesCollectionF = mongoConnectionApi.getCollection(publicImagesCollectionName)
  private lazy val photographCollectionF = mongoConnectionApi.getCollection(photographyCollectionName)

  val getPublicImage = getImageHelper(publicImagesCollectionName, publicImagesCollectionF) _

  val getPhotograph = getImageHelper(photographyCollectionName, photographCollectionF) _

  def getPhotographDetails(pageStart: Int, pageLength: Int) = async {
    implicit val documentFormat = Macros.handler[ImageDocument]
    val queryOptions = new QueryOpts(skipN = pageStart * pageLength, batchSizeN = pageLength, flagsN = 0)
    val documentsF = await(photographCollectionF).find(queryBuilder.emptyQuery).options(queryOptions).cursor[ImageDocument]().collect[List](pageLength)
    Json.toJson(await(documentsF))
  }

  private def getImageHelper(collectionName: String, collectionF: Future[BSONCollection])(imageId: String) = async {
    val eventualImageFile = await(collectionF).find(queryBuilder.findByIdQuery(imageId)).one[BSONDocument].map {
      case Some(document) =>
        val imagePath = document.getAsTry[String]("url")
        imagePath match {
          case Success(path) => fileFinder.find(path)
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

