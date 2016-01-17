package api.models

import play.api.libs.json.Json

sealed trait Document {
  def _id: String
  def uri: String
  def extension: String
  def title: String
}

case class ImageDocument(_id: String, uri: String, extension: String, title: String, description: String) extends Document

case class PdfDocument(_id: String, uri: String, extension: String, title: String) extends Document

object ImageDocument {
  implicit val format = Json.format[ImageDocument]
}

object PdfDocument {
  implicit val format = Json.format[PdfDocument]
}
