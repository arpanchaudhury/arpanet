package api.services

import api.models.{Image, Photograph}
import api.services.helpers.{MongoConnectionApi, QueryBuilder, ResourceFinder}
import com.google.inject.{Inject, Singleton}
import play.api.Logger
import play.api.libs.concurrent.Execution.Implicits._
import play.api.libs.json.Json
import reactivemongo.api.QueryOpts

import scala.async.Async._

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

  def getPublicImage(imageId: String) = async {
    val eventualImageFile = await(publicImagesCollectionF).find(queryBuilder.findByIdQuery(imageId)).one[Image]
    await(eventualImageFile) match {
      case Some(document) => await(resourceFinder.find(document.uri, document.extension))
      case None =>
        logger.error(s"Error: No image found in $publicImagesCollectionName with ID : $imageId")
        sys.error(s"Error: No image found in $publicImagesCollectionName with ID : $imageId")
    }
  }

  def getPhotograph(imageId: String) = async {
    val eventualImageFile = await(photographCollectionF).find(queryBuilder.findByIdQuery(imageId)).one[Photograph]
    await(eventualImageFile) match {
      case Some(document) => await(resourceFinder.find(document.uri, document.extension))
      case None =>
        logger.error(s"Error: No image found in $photographyCollectionName with ID : $imageId")
        sys.error(s"Error: No image found in $photographyCollectionName with ID : $imageId")
    }
  }

  def getPhotographDetails(pageStart: Int, pageLength: Int, tags: List[String]) = async {
    val query = if (tags.isEmpty) queryBuilder.emptyQuery else queryBuilder.findDocumentByTags(tags)
    val queryOptions = new QueryOpts(skipN = pageStart * pageLength, batchSizeN = pageLength, flagsN = 0)
    val documentsF = await(photographCollectionF).find(query).options(queryOptions).cursor[Photograph]().collect[List](pageLength)
    Json.toJson(await(documentsF))
  }

  def getPhotographsCount(tags: List[String]) = async {
    val query = if (tags.isEmpty) queryBuilder.emptyQuery else queryBuilder.findDocumentByTags(tags)
    val countF = await(photographCollectionF).count(Some(query))
    Json.toJson(await(countF))
  }
}