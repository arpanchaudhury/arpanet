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
class BlogPostService @Inject()(markdownService: MarkdownService,
                                mongoConnectionApi: MongoConnectionApi,
                                mongoConstants: MongoConstants,
                                queryBuilder: QueryBuilder) {

  private val logger = Logger(this.getClass)
  private implicit lazy val writeUpsDatabaseF = mongoConnectionApi.getDatabase(mongoConstants.applicationDatabaseName)
  private lazy val writeUpsCollectionF = mongoConnectionApi.getCollection(mongoConstants.writeUpsCollectionName)

  def getWriteUp(id: String) = async {
    val eventualWriteUpFile = await(writeUpsCollectionF).find(queryBuilder.findByIdQuery(id)).one[WriteUp]
    await(eventualWriteUpFile) match {
      case Some(document) => Json.toJson(document)
      case None =>
        logger.error(s"Error: No writeup found in ${mongoConstants.writeUpsCollectionName} with ID : $id")
        sys.error(s"Error: No writeup found in ${mongoConstants.writeUpsCollectionName} with ID : $id")
    }
  }

  def getMarkdown(id: String) = async {
    val eventualWriteUpFile = await(writeUpsCollectionF).find(queryBuilder.findByIdQuery(id)).one[WriteUp]
    await(eventualWriteUpFile) match {
      case Some(document) => await(markdownService.getMarkdown(document.markdown))
      case None =>
        logger.error(s"Error: No writeup found in ${mongoConstants.writeUpsCollectionName} with ID : $id")
        sys.error(s"Error: No writeup found in ${mongoConstants.writeUpsCollectionName} with ID : $id")
    }
  }

  def getWriteUps(pageStart: Int, pageLength: Int, topics: List[String]) = async {
    val query = if (topics.isEmpty) queryBuilder.emptyQuery else queryBuilder.findByTopics(topics)
    val queryOptions = new QueryOpts(skipN = pageStart * pageLength, batchSizeN = pageLength, flagsN = 0)
    val documentsF = await(writeUpsCollectionF).find(query).sort(queryBuilder.sortByQuery("timestamp", queryBuilder.Descending)).
        options(queryOptions).cursor[WriteUp]().collect[List](pageLength).transform(identity, e => {
        logger.error(s"Error: Can not fetch data from ${mongoConstants.writeUpsCollectionName}")
        sys.error(s"Error: Can not fetch data from ${mongoConstants.writeUpsCollectionName}")
      }
    )
    Json.toJson(await(documentsF))
  }

  def getWriteUpsCount(topics: List[String]) = async {
    val query = if (topics.isEmpty) queryBuilder.emptyQuery else queryBuilder.findByTopics(topics)
    val countF = await(writeUpsCollectionF).count(Some(query)).transform(identity, e => {
        logger.error(s"Error: Can not fetch data from ${mongoConstants.writeUpsCollectionName}")
        sys.error(s"Error: Can not fetch data from ${mongoConstants.writeUpsCollectionName}")
      }
    )
    Json.toJson(await(countF))
  }

  def getTopics = async {
    val topicsF = await(writeUpsCollectionF).distinct("topics").transform(identity, e => {
        logger.error(s"Error: Can not fetch data from ${mongoConstants.writeUpsCollectionName}")
        sys.error(s"Error: Can not fetch data from ${mongoConstants.writeUpsCollectionName}")
      }
    )
    val topics = await(topicsF).map { case topic: BSONString => topic.value }
    Json.toJson(topics)
  }
}
