package api.services.helpers

import com.google.inject.Singleton
import play.api.libs.json.JsObject

import scala.collection.Map

@Singleton
class DocumentParser {

  def parse(document:JsObject) = {
    document.value.map {
      case (collectionName, documents) =>
        collectionName -> documents.as[List[JsObject]]
    }
  }
}
