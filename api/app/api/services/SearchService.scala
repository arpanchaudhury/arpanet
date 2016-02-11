package api.services

import api.models.{Photograph, WriteUp}
import api.services.constants.MongoConstants
import api.services.helpers.{MongoConnectionApi, QueryBuilder}
import com.google.inject.{Inject, Singleton}
import play.api.libs.concurrent.Execution.Implicits._
import play.api.libs.json.{JsArray, Json}
import reactivemongo.api.QueryOpts

import scala.async.Async._

@Singleton
class SearchService @Inject()(mongoConnectionApi: MongoConnectionApi,
                              mongoConstants: MongoConstants,
                              queryBuilder: QueryBuilder) {

  private implicit lazy val writeUpsDatabaseF = mongoConnectionApi.getDatabase(mongoConstants.applicationDatabaseName)
  private lazy val writeUpsCollectionF = mongoConnectionApi.getCollection(mongoConstants.writeUpsCollectionName)
  private lazy val photographyCollectionF = mongoConnectionApi.getCollection(mongoConstants.photographyCollectionName)

  def searchWriteUps(searchTerm: String, pageStart: Int, pageLength: Int) = async {
    val query = if (searchTerm.isEmpty) queryBuilder.emptyQuery else queryBuilder.fullTextSearchQuery(searchTerm)
    val queryOptions = new QueryOpts(skipN = pageStart * pageLength, batchSizeN = pageLength, flagsN = 0)
    val writeUpSearchResultsF = await(writeUpsCollectionF).find(query).options(queryOptions).cursor[WriteUp]().collect[List](pageLength)
    Json.toJson(await(writeUpSearchResultsF))
  }

  def searchPhotographs(searchTerm: String, pageStart: Int, pageLength: Int) = async {
    val query = if (searchTerm.isEmpty) queryBuilder.emptyQuery else queryBuilder.fullTextSearchQuery(searchTerm)
    val queryOptions = new QueryOpts(skipN = pageStart * pageLength, batchSizeN = pageLength, flagsN = 0)
    val photographySearchResultsF = await(photographyCollectionF).find(query).options(queryOptions).cursor[Photograph]().collect[List](pageLength)
    Json.toJson(await(photographySearchResultsF))
  }
}
