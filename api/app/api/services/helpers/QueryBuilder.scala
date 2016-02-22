package api.services.helpers

import com.google.inject.Singleton
import reactivemongo.bson.BSONDocument

@Singleton
class QueryBuilder {
  sealed trait SortOrder {}
  case object Ascending extends SortOrder
  case object Descending extends SortOrder

  def emptyQuery = BSONDocument.empty

  def findByIdQuery(id: String) = BSONDocument("_id" -> id)

  def findByNameQuery(name: String) = BSONDocument("name" -> name)

  def findDocumentByTags(tags: List[String]) = BSONDocument("tags" -> BSONDocument("$in" -> tags))

  def findDocumentByTopics(topics: List[String]) = BSONDocument("topics" -> BSONDocument("$in" -> topics))

  def fullTextSearchQuery(searchTerm: String) = BSONDocument("$text" -> BSONDocument("$search" -> searchTerm))

  def sortByQuery(field: String, order: SortOrder = Ascending) = order match {
    case Ascending => BSONDocument(field -> 1)
    case Descending => BSONDocument(field -> -1)
  }
}