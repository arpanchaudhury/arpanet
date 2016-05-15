package api.services

import api.models.IngestionStatus
import api.services.constants.MongoConstants
import api.services.helpers.{DocumentParser, MongoConnectionApi, QueryBuilder}
import api.{RichFutureCollection, RichJSONObject}
import com.google.inject.{Inject, Singleton}
import play.api.Logger
import play.api.libs.concurrent.Execution.Implicits._
import play.api.libs.json.JsObject
import reactivemongo.api.collections.bson.BSONCollection

import scala.async.Async._
import scala.language.postfixOps

@Singleton
class IngestionService @Inject()(documentParser: DocumentParser,
                                 mongoConnectionApi: MongoConnectionApi,
                                 mongoConstants: MongoConstants,
                                 queryBuilder: QueryBuilder) {
  private val logger = Logger(this.getClass)
  private implicit lazy val databaseF = mongoConnectionApi.getDatabase(mongoConstants.applicationDatabaseName)

  def ingest(jsObject: JsObject) = async {
    logger.info("Ingestion of documents started ...")
    val parsedData = documentParser.parse(jsObject)
    val ingestionStatusesF = parsedData.map {
      case (collectionName, documents) =>
        val collectionF = mongoConnectionApi.getCollection(collectionName)
        collectionF flatMap (bulkInsert(_, documents))
    }.sequence.map(_.flatten)
    logger.info("Ingestion of documents completed ...")
    await(ingestionStatusesF).toList
  }

  private def bulkInsert(collection: BSONCollection, documents: List[JsObject]) = {
    documents.map {
      case document =>
        val documentId = (document \ "_id").as[String]
        collection.update(queryBuilder.findByIdQuery(documentId), document.toBsonDocument, upsert = true) map {
          case updateWriteResult if updateWriteResult.ok => IngestionStatus(documentId, collection.name, api.models.Success)
          case _ => IngestionStatus(documentId, collection.name, api.models.Failure)
        }
    }.sequence
  }
}
