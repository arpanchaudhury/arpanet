package api.models

import play.api.libs.json.{Json, Writes}
import reactivemongo.bson.{BSONDocument, BSONDocumentReader, Macros}

sealed trait Document {
  def _id: String
  def uri: String
  def extension: String
  def title: String
}

sealed trait WriteUp {
  def contentType: String
  def title: String
  def content: String
  def topics: List[String]
}

sealed trait ImageDocument extends Document {
  def description: String
}

case class Pdf(_id: String, uri: String, extension: String, title: String) extends Document

case class Image(_id: String, uri: String, extension: String, title: String, description: String) extends ImageDocument

case class Photograph(_id: String, uri: String, extension: String, title: String, description: String, tags: List[String]) extends ImageDocument

case class WriteUpContentOnly(contentType: String, title: String, content: String, topics: List[String]) extends WriteUp

case class WriteUpWithImage(contentType: String, title: String, content: String, imageUrl: String, topics: List[String]) extends WriteUp

case class WriteUpWithVideo(contentType: String, title: String, content: String, videoUrl: String, topics: List[String]) extends WriteUp

case class WriteUpWithSlide(contentType: String, title: String, content: String, slideUrl: String, topics: List[String]) extends WriteUp

object Pdf {
  implicit val format = Json.format[Pdf]
  implicit val documentFormat = Macros.handler[Pdf]
}

object Image {
  implicit val format = Json.format[Image]
  implicit val imageBSONFormat = Macros.handler[Image]
}

object Photograph {
  implicit val format = Json.format[Photograph]
  implicit val photographBSONFormat = Macros.handler[Photograph]
}

object WriteUp {
  implicit object WriteUpReader extends BSONDocumentReader[WriteUp] {
    def read(doc: BSONDocument) = {
      if (doc.getAsTry[String]("image_url").isSuccess) {
        WriteUpWithImage(
          contentType = doc.getAs[String]("type").get,
          title = doc.getAs[String]("title").get,
          content = doc.getAs[String]("content").get,
          imageUrl = doc.getAs[String]("image_url").get,
          topics = doc.getAs[List[String]]("topics").get
        )
      }
      else if (doc.getAsTry[String]("video_url").isSuccess) {
        WriteUpWithVideo(
          contentType = doc.getAs[String]("type").get,
          title = doc.getAs[String]("title").get,
          content = doc.getAs[String]("content").get,
          videoUrl = doc.getAs[String]("video_url").get,
          topics = doc.getAs[List[String]]("topics").get
        )
      }
      else if (doc.getAsTry[String]("video_url").isSuccess) {
        WriteUpWithSlide(
          contentType = doc.getAs[String]("type").get,
          title = doc.getAs[String]("title").get,
          content = doc.getAs[String]("content").get,
          slideUrl = doc.getAs[String]("slide_url").get,
          topics = doc.getAs[List[String]]("topics").get
        )
      }
      else {
        WriteUpContentOnly(
          contentType = doc.getAs[String]("type").get,
          title = doc.getAs[String]("title").get,
          content = doc.getAs[String]("content").get,
          topics = doc.getAs[List[String]]("topics").get
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