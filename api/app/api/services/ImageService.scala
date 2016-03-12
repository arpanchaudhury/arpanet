package api.services

import java.io.File

import api.models.{Photograph, PublicImage}
import api.services.constants.{ApplicationConstants, MongoConstants}
import api.services.helpers.{MongoConnectionApi, QueryBuilder, ResourceFinder}
import api.utils.Dimensions
import com.google.inject.{Inject, Singleton}
import com.sksamuel.scrimage.Image
import play.api.Logger
import play.api.libs.concurrent.Execution.Implicits._
import play.api.libs.json.Json
import reactivemongo.api.QueryOpts

import scala.async.Async._

@Singleton
class ImageService @Inject()(resourceFinder: ResourceFinder,
                             applicationConstants: ApplicationConstants,
                             mongoConnectionApi: MongoConnectionApi,
                             mongoConstants: MongoConstants,
                             queryBuilder: QueryBuilder) {

  private val logger = Logger(this.getClass)
  private implicit lazy val imageDatabaseF = mongoConnectionApi.getDatabase(mongoConstants.applicationDatabaseName)
  private lazy val publicImagesCollectionF = mongoConnectionApi.getCollection(mongoConstants.publicImagesCollectionName)
  private lazy val photographyCollectionF = mongoConnectionApi.getCollection(mongoConstants.photographyCollectionName)

  def getPublicImage(imageId: String, dimensions: Option[Dimensions]) = async {
    val eventualImageFile = await(publicImagesCollectionF).find(queryBuilder.findByIdQuery(imageId)).one[PublicImage]
    await(eventualImageFile) match {
      case Some(document) =>
        val image = await(resourceFinder.find(document.uri, document.extension))
        dimensions.map { d =>
          val resizedImageOutput = new File(s"${applicationConstants.TEMP_DIRECTORY}/$d-${image.getName}")
          Image.fromFile(image).max(d.maxWidth, d.maxHeight).output(resizedImageOutput)
        }.getOrElse(image)
      case None =>
        logger.error(s"Error: No image found in ${mongoConstants.publicImagesCollectionName} with ID : $imageId")
        sys.error(s"Error: No image found in ${mongoConstants.publicImagesCollectionName} with ID : $imageId")
    }
  }

  def getPhotograph(imageId: String, dimensions: Option[Dimensions]) = async {
    val eventualImageFile = await(photographyCollectionF).find(queryBuilder.findByIdQuery(imageId)).one[Photograph]
    await(eventualImageFile) match {
      case Some(document) =>
        val image = await(resourceFinder.find(document.uri, document.extension))
        dimensions.map { d =>
          val resizedImageOutput = new File(s"${applicationConstants.TEMP_DIRECTORY}/$d-${image.getName}")
          Image.fromFile(image).max(d.maxWidth, d.maxHeight).output(resizedImageOutput)
        }.getOrElse(image)
      case None =>
        logger.error(s"Error: No image found in ${mongoConstants.photographyCollectionName} with ID : $imageId")
        sys.error(s"Error: No image found in ${mongoConstants.photographyCollectionName} with ID : $imageId")
    }
  }

  def getPhotographDetails(pageStart: Int, pageLength: Int, tags: List[String]) = async {
    val query = if (tags.isEmpty) queryBuilder.emptyQuery else queryBuilder.findByTags(tags)
    val queryOptions = new QueryOpts(skipN = pageStart * pageLength, batchSizeN = pageLength, flagsN = 0)
    val documentsF = await(photographyCollectionF).find(query)
      .options(queryOptions).cursor[Photograph]().collect[List](pageLength).transform(identity, e => {
        logger.error(s"Error: Can not fetch data from ${mongoConstants.writeUpsCollectionName}")
        sys.error(s"Error: Can not fetch data from ${mongoConstants.writeUpsCollectionName}")
      }
    )
    Json.toJson(await(documentsF))
  }

  def getPhotographsCount(tags: List[String]) = async {
    val query = if (tags.isEmpty) queryBuilder.emptyQuery else queryBuilder.findByTags(tags)
    val countF = await(photographyCollectionF).count(Some(query)).transform(identity, e => {
        logger.error(s"Error: Can not fetch data from ${mongoConstants.writeUpsCollectionName}")
        sys.error(s"Error: Can not fetch data from ${mongoConstants.writeUpsCollectionName}")
      }
    )
    Json.toJson(await(countF))
  }
}