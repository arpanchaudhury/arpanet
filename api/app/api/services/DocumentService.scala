package api.services

import api.models.Document
import api.services.constants.MongoConstants
import api.services.helpers.{MongoConnectionApi, QueryBuilder}
import com.google.inject.{Inject, Singleton}
import play.api.Logger
import play.api.libs.concurrent.Execution.Implicits._
import play.api.libs.json.{Json, JsValue}

import scala.async.Async._
import scala.concurrent.Future


@Singleton
class DocumentService @Inject()(mongoConnectionApi: MongoConnectionApi,
                                mongoConstants: MongoConstants,
                                queryBuilder: QueryBuilder) {

  private val logger = Logger(this.getClass)
  private implicit lazy val documentDatabaseF = mongoConnectionApi.getDatabase(mongoConstants.applicationDatabaseName)
  private lazy val documentsCollectionF = mongoConnectionApi.getCollection(mongoConstants.documentsCollection)

  def getDocument(id: String) = async {
    val eventualDocument = await(documentsCollectionF).find(queryBuilder.findByIdQuery(id)).one[Document]
    await(eventualDocument) match {
      case Some(document) => Json.toJson(document)
      case None =>
        logger.error(s"Error: No document found in ${mongoConstants.documentsCollection} with ID : $id")
        sys.error(s"Error: No document found in ${mongoConstants.documentsCollection} with ID : $id")
    }
  }
}
