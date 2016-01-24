package api.models

import play.api.libs.json.Json
import reactivemongo.bson.Macros

sealed trait Document {
  def _id: String
  def uri: String
  def extension: String
  def title: String
}

sealed trait ImageDocument extends Document {
  def description: String
}

case class Pdf(_id: String, uri: String, extension: String, title: String) extends Document

case class Image(_id: String, uri: String, extension: String, title: String, description: String) extends ImageDocument

case class Photograph(_id: String, uri: String, extension: String, title: String, description: String, tags: List[String]) extends ImageDocument

object Pdf {
  implicit val format = Json.format[Pdf]
}

object Image {
  implicit val format = Json.format[Image]
  implicit val imageBSONFormat = Macros.handler[Image]
}

object Photograph {
  implicit val format = Json.format[Photograph]
  implicit val photographBSONFormat = Macros.handler[Photograph]
}
