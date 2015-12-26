package api.services

import com.google.inject.Singleton
import play.api.Play

import scala.collection.JavaConverters._
import scala.util.Try

@Singleton
class PropertyService {
  private val configuration = Play.current.configuration

  lazy val mongoServers = safeRecovery(
    configuration.underlying.getStringList("mongo.server.uris").asScala.toList,
    List.empty[String]
  )

  private def safeRecovery[T](config: => T, defaultValue: T) = Try(config).getOrElse(defaultValue)
}
