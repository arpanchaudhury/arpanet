package api.models

import api.RichBSONValue
import play.api.libs.json.{JsObject, Json, Writes}
import reactivemongo.bson.{Macros, BSONDocument, BSONDocumentReader}

sealed trait Document {
  def underlying: BSONDocument
}

object Document {
  implicit object DocumentReader extends BSONDocumentReader[Document] {
    def read(doc: BSONDocument) = new Document { override def underlying: BSONDocument = doc }
  }

  implicit val writes = Writes[Document] {
    case document: Document => document.underlying.toJson.as[JsObject]
  }
}

case class Pdf(_id: String, uri: String, extension: String, title: String)

object Pdf {
  implicit val format = Json.format[Pdf]
  implicit val pdfFormat = Macros.handler[Pdf]
}

case class Markdown(_id: String, uri: String, extension: String, title: String)

object Markdown {
  implicit val format = Json.format[Markdown]
  implicit val pdfFormat = Macros.handler[Markdown]
}

case class PublicImage(_id: String, uri: String, extension: String, title: String, description: String)

case class Photograph(_id: String, uri: String, extension: String, title: String, description: String, tags: List[String])

object PublicImage {
  implicit val format = Json.format[PublicImage]
  implicit val imageBSONFormat = Macros.handler[PublicImage]
}

object Photograph {
  implicit val format = Json.format[Photograph]
  implicit val photographBSONFormat = Macros.handler[Photograph]
}

sealed trait WriteUp {
  def _id: String
  def contentType: String
  def title: String
  def content: String
  def topics: List[String]
  def markdown: String
  def timestamp: String
  def code: Option[String]
}

case class WriteUpContentOnly(_id: String, contentType: String, title: String, content: String, topics: List[String], markdown: String, timestamp: String, code: Option[String]) extends WriteUp

case class WriteUpWithImage(_id: String, contentType: String, title: String, content: String, imageId: String, topics: List[String], markdown: String, timestamp: String, code: Option[String]) extends WriteUp

case class WriteUpWithVideo(_id: String, contentType: String, title: String, content: String, videoUrl: String, posterImageUrl: String, topics: List[String], markdown: String, timestamp: String, code: Option[String]) extends WriteUp

case class WriteUpWithSlide(_id: String, contentType: String, title: String, content: String, slideUrl: String, posterImageUrl: String, topics: List[String], markdown: String, timestamp: String, code: Option[String]) extends WriteUp

object WriteUp {
  implicit object WriteUpReader extends BSONDocumentReader[WriteUp] {
    def read(doc: BSONDocument) = {
      if (doc.getAsTry[String]("image_id").isSuccess) {
        WriteUpWithImage(
          _id = doc.getAs[String]("_id").get,
          contentType = doc.getAs[String]("type").get,
          title = doc.getAs[String]("title").get,
          content = doc.getAs[String]("content").get,
          imageId = doc.getAs[String]("image_id").get,
          topics = doc.getAs[List[String]]("topics").get,
          markdown = doc.getAs[String]("markdown").get,
          timestamp = doc.getAs[String]("timestamp").get,
          code = doc.getAs[String]("code")
        )
      }
      else if (doc.getAsTry[String]("video_url").isSuccess) {
        WriteUpWithVideo(
          _id = doc.getAs[String]("_id").get,
          contentType = doc.getAs[String]("type").get,
          title = doc.getAs[String]("title").get,
          content = doc.getAs[String]("content").get,
          videoUrl = doc.getAs[String]("video_url").get,
          posterImageUrl = doc.getAs[String]("poster_image_url").get,
          topics = doc.getAs[List[String]]("topics").get,
          markdown = doc.getAs[String]("markdown").get,
          timestamp = doc.getAs[String]("timestamp").get,
          code = doc.getAs[String]("code")
        )
      }
      else if (doc.getAsTry[String]("slide_url").isSuccess) {
        WriteUpWithSlide(
          _id = doc.getAs[String]("_id").get,
          contentType = doc.getAs[String]("type").get,
          title = doc.getAs[String]("title").get,
          content = doc.getAs[String]("content").get,
          slideUrl = doc.getAs[String]("slide_url").get,
          posterImageUrl = doc.getAs[String]("poster_image_url").get,
          topics = doc.getAs[List[String]]("topics").get,
          markdown = doc.getAs[String]("markdown").get,
          timestamp = doc.getAs[String]("timestamp").get,
          code = doc.getAs[String]("code")
        )
      }
      else {
        WriteUpContentOnly(
          _id = doc.getAs[String]("_id").get,
          contentType = doc.getAs[String]("type").get,
          title = doc.getAs[String]("title").get,
          content = doc.getAs[String]("content").get,
          topics = doc.getAs[List[String]]("topics").get,
          markdown = doc.getAs[String]("markdown").get,
          timestamp = doc.getAs[String]("timestamp").get,
          code = doc.getAs[String]("code")
        )
      }
    }
  }

  implicit val formatWriteUpWithImage   = Json.format[WriteUpWithImage]
  implicit val formatWriteUpWithVideo   = Json.format[WriteUpWithVideo]
  implicit val formatWriteUpWithSlide   = Json.format[WriteUpWithSlide]
  implicit val formatWriteUpContentOnly = Json.format[WriteUpContentOnly]

  implicit val writes = Writes[WriteUp] {
    case w: WriteUpContentOnly  => Json.toJson(w)
    case w: WriteUpWithImage    => Json.toJson(w)
    case w: WriteUpWithVideo    => Json.toJson(w)
    case w: WriteUpWithSlide    => Json.toJson(w)
  }
}