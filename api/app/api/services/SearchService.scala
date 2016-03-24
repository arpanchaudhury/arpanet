package api.services

import api.models.{Photograph, WriteUp}
import api.services.constants.MongoConstants
import api.services.helpers.{MongoConnectionApi, QueryBuilder}
import com.google.inject.{Inject, Singleton}
import play.api.Logger
import play.api.libs.concurrent.Execution.Implicits._
import play.api.libs.json.Json
import reactivemongo.api.QueryOpts

import scala.async.Async._

@Singleton
class SearchService @Inject()(mongoConnectionApi: MongoConnectionApi,
                              mongoConstants: MongoConstants,
                              queryBuilder: QueryBuilder) {

  private val logger = Logger(this.getClass)
  private implicit lazy val searchDatabaseF = mongoConnectionApi.getDatabase(mongoConstants.applicationDatabaseName)
  private lazy val writeUpsCollectionF = mongoConnectionApi.getCollection(mongoConstants.writeUpsCollectionName)
  private lazy val photographyCollectionF = mongoConnectionApi.getCollection(mongoConstants.photographyCollectionName)

  def searchWriteUps(searchTerm: String, pageStart: Int, pageLength: Int) = async {
    val query = if (searchTerm.isEmpty) queryBuilder.emptyQuery else queryBuilder.fullTextSearchQuery(searchTerm)
    val queryOptions = new QueryOpts(skipN = pageStart * pageLength, batchSizeN = pageLength, flagsN = 0)
    val countF = await(writeUpsCollectionF).count(Some(query)).transform(identity, e => {
        logger.error(s"Error: Can not fetch data from ${mongoConstants.writeUpsCollectionName}")
        sys.error(s"Error: Can not fetch data from ${mongoConstants.writeUpsCollectionName}")
      }
    )
    val writeUpSearchResultsF = await(writeUpsCollectionF).find(query)
    .sort(queryBuilder.sortByQuery("timestamp", queryBuilder.Descending)).options(queryOptions)
    .cursor[WriteUp]().collect[List](pageLength).transform(identity, e => {
        logger.error(s"Error: Can not fetch data from ${mongoConstants.writeUpsCollectionName}")
        sys.error(s"Error: Can not fetch data from ${mongoConstants.writeUpsCollectionName}")
      }
    )
    Json.obj("total" -> Json.toJson(await(countF)), "results" -> Json.toJson(await(writeUpSearchResultsF)))
  }

  def searchPhotographs(searchTerm: String, pageStart: Int, pageLength: Int) = async {
    val query = if (searchTerm.isEmpty) queryBuilder.emptyQuery else queryBuilder.fullTextSearchQuery(searchTerm)
    val queryOptions = new QueryOpts(skipN = pageStart * pageLength, batchSizeN = pageLength, flagsN = 0)
    val countF = await(photographyCollectionF).count(Some(query)).transform(identity, e => {
        logger.error(s"Error: Can not fetch data from ${mongoConstants.photographyCollectionName}")
        sys.error(s"Error: Can not fetch data from ${mongoConstants.photographyCollectionName}")
      }
    )
    val photographySearchResultsF = await(photographyCollectionF).find(query)
      .options(queryOptions).cursor[Photograph]().collect[List](pageLength).transform(identity, e => {
      logger.error(s"Error: Can not fetch data from ${mongoConstants.photographyCollectionName}")
      sys.error(s"Error: Can not fetch data from ${mongoConstants.photographyCollectionName}")
    }
    )
    Json.obj("total" -> Json.toJson(await(countF)), "results" -> Json.toJson(await(photographySearchResultsF)))
  }
}
