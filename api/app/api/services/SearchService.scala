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

  def search(searchTerm: String) = async {
    val query = if (searchTerm.isEmpty) queryBuilder.emptyQuery else queryBuilder.fullTextSearchQuery(searchTerm)
    val writeUpSearchResultsF = await(writeUpsCollectionF).find(query).cursor[WriteUp]().collect[List]()
    val photographySearchResultsF = await(photographyCollectionF).find(query).cursor[Photograph]().collect[List]()
    Json.toJson(await(writeUpSearchResultsF)).as[JsArray] ++ Json.toJson(await(photographySearchResultsF)).as[JsArray]
  }
}
