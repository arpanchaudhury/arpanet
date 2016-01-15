package api.services.helpers

import com.google.inject.Singleton
import reactivemongo.bson.BSONDocument

@Singleton
class QueryBuilder {
  def emptyQuery = BSONDocument.empty
  def findByIdQuery(id: String) = BSONDocument("_id" -> id)
  def findByNameQuery(name: String) = BSONDocument("name" -> name)
}
