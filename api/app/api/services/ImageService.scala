package api.services

import api.models.ImageDocument
import api.services.helpers.{MongoConnectionApi, QueryBuilder, ResourceFinder}
import com.google.inject.{Inject, Singleton}
import play.api.Logger
import play.api.libs.concurrent.Execution.Implicits._
import play.api.libs.json.Json
import reactivemongo.api.QueryOpts
import reactivemongo.api.collections.bson.BSONCollection
import reactivemongo.bson.Macros

import scala.async.Async._
import scala.concurrent.Future

@Singleton
class ImageService @Inject()(resourceFinder: ResourceFinder,
                             queryBuilder: QueryBuilder,
                             mongoConnectionApi: MongoConnectionApi) {
  private val logger = Logger(this.getClass)

  private implicit lazy val imageDatabaseF = mongoConnectionApi.getDatabase("arpa(n)2et")

  private val publicImagesCollectionName = "public_images"
  private val photographyCollectionName = "photography"

  private lazy val publicImagesCollectionF = mongoConnectionApi.getCollection(publicImagesCollectionName)
  private lazy val photographCollectionF = mongoConnectionApi.getCollection(photographyCollectionName)

  private implicit val documentFormat = Macros.handler[ImageDocument]

  val getPublicImage = getImageHelper(publicImagesCollectionName, publicImagesCollectionF) _

  val getPhotograph = getImageHelper(photographyCollectionName, photographCollectionF) _

  def getPhotographDetails(pageStart: Int, pageLength: Int) = async {
    val queryOptions = new QueryOpts(skipN = pageStart * pageLength, batchSizeN = pageLength, flagsN = 0)
    val documentsF = await(photographCollectionF).find(queryBuilder.emptyQuery).options(queryOptions).cursor[ImageDocument]().collect[List](pageLength)
    Json.toJson(await(documentsF))
  }

  private def getImageHelper(collectionName: String, collectionF: Future[BSONCollection])(imageId: String) = async {
    val eventualImageFile = await(collectionF).find(queryBuilder.findByIdQuery(imageId)).one[ImageDocument]
    await(eventualImageFile) match {
      case Some(document) => await(resourceFinder.find(document.uri, document.extension))
      case None =>
        logger.error(s"Error: No image found in $collectionName with ID : $imageId")
        sys.error(s"Error: No image found in $collectionName with ID : $imageId")
    }
  }
}