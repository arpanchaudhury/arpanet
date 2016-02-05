package api.services

import api.models.WriteUp
import api.services.helpers.{MongoConnectionApi, QueryBuilder}
import com.google.inject.{Inject, Singleton}
import play.api.libs.concurrent.Execution.Implicits._
import play.api.libs.json.Json
import reactivemongo.api.QueryOpts
import reactivemongo.bson.BSONString

import scala.async.Async._

@Singleton
class BlogPostService @Inject()(mongoConnectionApi: MongoConnectionApi, queryBuilder: QueryBuilder) {

  private implicit lazy val writeUpsDatabaseF = mongoConnectionApi.getDatabase("arpa(n)2et")

  private val writeUpsCollectionName = "write_ups"

  private lazy val writeUpsCollectionF = mongoConnectionApi.getCollection(writeUpsCollectionName)

  def getWriteUps(pageStart: Int, pageLength: Int, topics: List[String]) = async {
    val query = if (topics.isEmpty) queryBuilder.emptyQuery else queryBuilder.findDocumentByTopics(topics)
    val queryOptions = new QueryOpts(skipN = pageStart * pageLength, batchSizeN = pageLength, flagsN = 0)
    val documentsF = await(writeUpsCollectionF).find(query).options(queryOptions).cursor[WriteUp]().collect[List](pageLength)
    Json.toJson(await(documentsF))
  }

  def getTopics = async {
    val topicsF = await(writeUpsCollectionF).distinct("topics")
    val topics = await(topicsF).map { case topic: BSONString => topic.value }
    Json.toJson(topics)
  }
}
