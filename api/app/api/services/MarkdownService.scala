package api.services

import api.models.Markdown
import api.services.constants.MongoConstants
import api.services.helpers.{MongoConnectionApi, QueryBuilder, ResourceFinder}
import com.google.inject.{Inject, Singleton}
import play.api.Logger
import play.api.libs.concurrent.Execution.Implicits._

import scala.async.Async._

@Singleton
class MarkdownService @Inject()(resourceFinder: ResourceFinder,
                                mongoConnectionApi: MongoConnectionApi,
                                mongoConstants: MongoConstants,
                                queryBuilder: QueryBuilder) {
  private val logger = Logger(this.getClass)
  private implicit lazy val markdownsDatabaseF = mongoConnectionApi.getDatabase(mongoConstants.applicationDatabaseName)
  private lazy val markdownsCollectionF = mongoConnectionApi.getCollection(mongoConstants.markdownsCollectionName)

  def getMarkdown(markdownId: String) = async {
    val eventualMarkdownFile = await(markdownsCollectionF).find(queryBuilder.findByIdQuery(markdownId)).one[Markdown]
    await(eventualMarkdownFile) match {
      case Some(document) => await(resourceFinder.find(document.uri, document.extension))
      case None =>
        logger.error(s"Error: No markdown found in ${mongoConstants.markdownsCollectionName} with ID : $markdownId")
        sys.error(s"Error: No markdown found in ${mongoConstants.markdownsCollectionName} with ID : $markdownId")
    }
  }
}
