package api.configurations

import com.google.inject.{Singleton, Inject}
import play.api.http.HttpFilters

@Singleton
class Filters @Inject() (accessLogger: AccessLoggingFilter) extends HttpFilters {
  val filters = Seq(accessLogger)
}
