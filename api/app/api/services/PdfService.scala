package api.services

import api.models.Pdf
import api.services.constants.MongoConstants
import api.services.helpers.{MongoConnectionApi, QueryBuilder, ResourceFinder}
import com.google.inject.{Inject, Singleton}
import play.api.Logger
import play.api.libs.concurrent.Execution.Implicits._

import scala.async.Async._

@Singleton
class PdfService @Inject()(resourceFinder: ResourceFinder,
                           mongoConnectionApi: MongoConnectionApi,
                           mongoConstants: MongoConstants,
                           queryBuilder: QueryBuilder) {

  private val logger = Logger(this.getClass)
  private implicit lazy val pdfDatabaseF = mongoConnectionApi.getDatabase(mongoConstants.applicationDatabaseName)
  private lazy val pdfCollectionF = mongoConnectionApi.getCollection(mongoConstants.pdfCollectionName)

  def getDocument(documentId: String) = async {
    val eventualDocument = await(pdfCollectionF).find(queryBuilder.findByIdQuery(documentId)).one[Pdf]
    await(eventualDocument) match {
      case Some(document) => await(resourceFinder.find(document.uri, document.extension))
      case None =>
        logger.error(s"Error: No pdf found in ${mongoConstants.pdfCollectionName} with name : $documentId")
        sys.error(s"Error: No pdf found in ${mongoConstants.pdfCollectionName} with name : $documentId")
    }
  }
}
