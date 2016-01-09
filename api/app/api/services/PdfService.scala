package api.services

import java.io.File

import api.services.helpers.{MongoConnectionApi, QueryBuilder}
import com.google.inject.{Inject, Singleton}
import play.api.Logger
import play.api.libs.concurrent.Execution.Implicits._
import reactivemongo.bson.BSONDocument

import scala.async.Async._
import scala.util.{Failure, Success}

@Singleton
class PdfService @Inject()(mongoConnectionApi: MongoConnectionApi, queryBuilder: QueryBuilder) {
  private val logger = Logger(this.getClass)

  private implicit lazy val pdfDatabaseF = mongoConnectionApi.getDatabase("arpa(n)2et")

  private val pdfCollectionName = "pdfs"

  private lazy val pdfCollectionF = mongoConnectionApi.getCollection(pdfCollectionName)

  def getDocument(documentName: String) = async {
    val eventualDocument = await(pdfCollectionF).find(queryBuilder.findByNameQuery(documentName)).one[BSONDocument].map {
      case Some(document) =>
        val documentPath = document.getAsTry[String]("url")
        documentPath match {
          case Success(path) => new File(path)
          case Failure(exception) =>
            logger.error(s"Error: url field not available for document in $pdfCollectionName with name : $documentName")
            sys.error(s"Error: url field not available for document in $pdfCollectionName with ID : $documentName")
        }
      case None =>
        logger.error(s"Error: No pdf found in $pdfCollectionName with name : $documentName")
        sys.error(s"Error: No pdf found in $pdfCollectionName with name : $documentName")
    }
    await(eventualDocument)
  }
}
