package api.services

import api.models.Document
import api.services.constants.MongoConstants
import api.services.helpers.{MongoConnectionApi, QueryBuilder}
import com.google.inject.{Inject, Singleton}
import play.api.Logger
import play.api.libs.concurrent.Execution.Implicits._
import play.api.libs.json.Json

import scala.async.Async._


@Singleton
class DocumentService @Inject()(mongoConnectionApi: MongoConnectionApi,
                                mongoConstants: MongoConstants,
                                queryBuilder: QueryBuilder) {

  private val logger = Logger(this.getClass)
  private implicit lazy val documentDatabaseF = mongoConnectionApi.getDatabase(mongoConstants.applicationDatabaseName)
  private lazy val documentsCollectionF = mongoConnectionApi.getCollection(mongoConstants.documentsCollectionName)

  def getDocument(id: String) = async {
    val eventualDocument = await(documentsCollectionF).find(queryBuilder.findByIdQuery(id)).one[Document]
    await(eventualDocument) match {
      case Some(document) => Json.toJson(document)
      case None =>
        logger.error(s"Error: No document found in ${mongoConstants.documentsCollectionName} with ID : $id")
        sys.error(s"Error: No document found in ${mongoConstants.documentsCollectionName} with ID : $id")
    }
  }

  def getDocumentsByType(docType: String) = async {
    val documentsF = await(documentsCollectionF).find(queryBuilder.findByTypeQuery(docType))
      .cursor[Document]().collect[List]().transform(identity, e => {
        logger.error(s"Error: Can not fetch data from ${mongoConstants.documentsCollectionName}")
        sys.error(s"Error: Can not fetch data from ${mongoConstants.documentsCollectionName}")
      }
    )
    Json.toJson(await(documentsF))
  }
}
