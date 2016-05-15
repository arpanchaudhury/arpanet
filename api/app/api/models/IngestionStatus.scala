package api.models

sealed trait Status
case object Success extends Status
case object Failure extends Status

case class IngestionStatus(documentId: String, documentCollection: String, status: Status)
