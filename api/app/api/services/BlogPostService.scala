package api.services

import api.models.WriteUp
import api.services.constants.MongoConstants
import api.services.helpers.{MongoConnectionApi, QueryBuilder}
import com.google.inject.{Inject, Singleton}
import play.api.Logger
import play.api.libs.concurrent.Execution.Implicits._
import play.api.libs.json.Json
import reactivemongo.api.QueryOpts
import reactivemongo.bson.BSONString

import scala.async.Async._

@Singleton
class BlogPostService @Inject()(mongoConnectionApi: MongoConnectionApi,
                                mongoConstants: MongoConstants,
                                queryBuilder: QueryBuilder) {

  private val logger = Logger(this.getClass)
  private implicit lazy val writeUpsDatabaseF = mongoConnectionApi.getDatabase(mongoConstants.applicationDatabaseName)
  private lazy val writeUpsCollectionF = mongoConnectionApi.getCollection(mongoConstants.writeUpsCollectionName)

  def getWriteUp(id: String) = async {
    val eventualImageFile = await(writeUpsCollectionF).find(queryBuilder.findByIdQuery(id)).one[WriteUp]
    await(eventualImageFile) match {
      case Some(document) => Json.toJson(document)
      case None =>
        logger.error(s"Error: No writeup found in ${mongoConstants.writeUpsCollectionName} with ID : $id")
        sys.error(s"Error: No writeup found in ${mongoConstants.writeUpsCollectionName} with ID : $id")
    }
  }

  def getWriteUps(pageStart: Int, pageLength: Int, topics: List[String]) = async {
    val query = if (topics.isEmpty) queryBuilder.emptyQuery else queryBuilder.findDocumentByTopics(topics)
    val queryOptions = new QueryOpts(skipN = pageStart * pageLength, batchSizeN = pageLength, flagsN = 0)
    val documentsF = await(writeUpsCollectionF).find(query).sort(queryBuilder.sortByQuery("timestamp", queryBuilder.Descending)).options(queryOptions).cursor[WriteUp]().collect[List](pageLength)
    Json.toJson(await(documentsF))
  }

  def getWriteUpsCount(topics: List[String]) = async {
    val query = if (topics.isEmpty) queryBuilder.emptyQuery else queryBuilder.findDocumentByTopics(topics)
    val countF = await(writeUpsCollectionF).count(Some(query))
    Json.toJson(await(countF))
  }

  def getTopics = async {
    val topicsF = await(writeUpsCollectionF).distinct("topics")
    val topics = await(topicsF).map { case topic: BSONString => topic.value }
    Json.toJson(topics)
  }
}
