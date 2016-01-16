package api.models

import play.api.libs.json.Json

case class ImageDocument(_id: String, url: String, title: String, description: String)

object ImageDocument {
  implicit val format = Json.format[ImageDocument]
}
